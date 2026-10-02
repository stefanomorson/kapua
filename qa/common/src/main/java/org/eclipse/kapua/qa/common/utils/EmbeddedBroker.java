/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.common.utils;

import java.time.Duration;

import cucumber.api.java.en.Given;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.eclipse.kapua.broker.BrokerDomains;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.qa.common.BasicSteps;
import org.eclipse.kapua.qa.common.Ports;
import org.eclipse.kapua.qa.common.Suppressed;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class EmbeddedBroker {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedBroker.class);

    private static final String DEFAULT_DATA_DIRECTORY_PREFIX = "target/activemq/" + UUID.randomUUID();
    private static final String DEFAULT_KAHA_DB_DIRECTORY = DEFAULT_DATA_DIRECTORY_PREFIX + "/kahaDB";
    private static final String DEFAULT_DATA_DIRECTORY = DEFAULT_DATA_DIRECTORY_PREFIX + "/data";
    private static final String KAHA_DB_DIRECTORY = "KAHA_DB_DIRECTORY";
    /**
     * Embedded broker configuration file from classpath resources.
     */
    public static final String ACTIVEMQ_XML = "xbean:activemq.xml";

    private static final int EXTRA_STARTUP_DELAY = Integer.getInteger("org.eclipse.kapua.qa.broker.extraStartupDelay", 0);

    private static final boolean NO_EMBEDDED_SERVERS = Boolean.getBoolean("org.eclipse.kapua.qa.noEmbeddedServers");

    private Map<String, List<AutoCloseable>> closables = new HashMap<>();

    private static BrokerService broker;

    public EmbeddedBroker() {
    }

    @Given("^Start Broker$")
    public void start() {

        if (NO_EMBEDDED_SERVERS) {
            return;
        }
        logger.info("Starting new Broker instance");
        try {
            // Seed kapua-broker user for test that are using broker
            KapuaLocator locator = KapuaLocator.getInstance();
            UserService userService = locator.getService(UserService.class);
            UserFactory userFactory = locator.getFactory(UserFactory.class);
            CredentialService credentialService = locator.getService(CredentialService.class);
            CredentialFactory credentialFactory = locator.getFactory(CredentialFactory.class);
            AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
            AccessInfoFactory accessInfoFactory = locator.getFactory(AccessInfoFactory.class);
            PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

            logger.info("Seeding {} user...", BasicSteps.KAPUA_BROKER_USERNAME);
            KapuaSecurityUtils.doPrivileged(() -> {
                if (userService.findByName(BasicSteps.KAPUA_BROKER_USERNAME) != null) {
                    logger.info("Seeding {} user... SKIPPED (already present)", BasicSteps.KAPUA_BROKER_USERNAME);
                    return;
                }

                UserCreator userCreator = userFactory.newCreator(BasicSteps.KAPUA_BROKER_SCOPE_ID, BasicSteps.KAPUA_BROKER_USERNAME);
                userCreator.setDisplayName(BasicSteps.KAPUA_BROKER_USERNAME);
                User user = userService.create(userCreator);

                credentialService.create(credentialFactory.newCreator(BasicSteps.KAPUA_BROKER_SCOPE_ID, user.getId(), CredentialType.PASSWORD, BasicSteps.KAPUA_BROKER_PASSWORD, CredentialStatus.ENABLED, null));

                AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(BasicSteps.KAPUA_BROKER_SCOPE_ID);
                accessInfoCreator.setUserId(user.getId());
                accessInfoCreator.setPermissions(Collections.singleton(permissionFactory.newPermission(BrokerDomains.BROKER_DOMAIN, Actions.connect, BasicSteps.KAPUA_BROKER_SCOPE_ID)));
                accessInfoService.create(accessInfoCreator);

                logger.info("Seeding {} user... DONE", BasicSteps.KAPUA_BROKER_USERNAME);
            });

            // test if port is already open
            if (Ports.isPortOpen(1883)) {
                throw new IllegalStateException("Broker port is already in use");
            }

            // start the broker
            System.setProperty(KAHA_DB_DIRECTORY, DEFAULT_KAHA_DB_DIRECTORY);
            broker = BrokerFactory.createBroker(ACTIVEMQ_XML);
            broker.setDataDirectory(DEFAULT_DATA_DIRECTORY);
            logger.info("Setting ActiveMQ data directory to {}", broker.getBrokerDataDirectory());
            broker.start();

            // wait for the broker

            if (!broker.waitUntilStarted(Duration.ofSeconds(20).toMillis())) {
                throw new IllegalStateException("Failed to start up broker in time");
            }

            if (EXTRA_STARTUP_DELAY > 0) {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to start broker", e);
        }
    }

    @Given("^Stop Broker$")
    public void stop() {

        if (NO_EMBEDDED_SERVERS) {
            return;
        }
        logger.info("Stopping Broker instance ...");
        try (final Suppressed<RuntimeException> s = Suppressed.withRuntimeException()) {

            // close all resources

            closables.values().stream().flatMap(values -> values.stream()).forEach(s::closeSuppressed);

            // shut down broker

            if (broker != null) {
                broker.stop();
                broker.waitUntilStopped();
                broker = null;
            }

        } catch (Exception e) {
            logger.error("Failed to stop Broker!");
            e.printStackTrace();
        }

        DatastoreMediator.getInstance().clearCache();

        if (EXTRA_STARTUP_DELAY > 0) {
            try {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("Stopping Broker instance ... done!");
    }

}
