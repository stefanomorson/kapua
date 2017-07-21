/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.broker.core;

import static com.google.common.base.MoreObjects.firstNonNull;
import static org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers.resolveJdbcUrl;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_PASSWORD;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_SCHEMA;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_SCHEMA_ENV;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_USERNAME;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.AbstractSessionManager;
import org.apache.shiro.session.mgt.AbstractValidatingSessionManager;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.plugin.KapuaSecurityBrokerFilter;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticator;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Install {@link KapuaSecurityBrokerFilter} into activeMQ filter chain plugin.<BR>
 * <p>
 * Is called by activeMQ broker by configuring plugin tag inside broker tag into activemq.xml.<BR>
 * <BR>
 * <BR>
 * <p>
 * <pre>
 * &lt;plugins&gt;
 *     &lt;bean xmlns="http://www.springframework.org/schema/beans" id="kapuaFilter" class="org.eclipse.kapua.broker.core.KapuaSecurityBrokerFilter"/&gt;
 * &lt;/plugins&gt;
 * </pre>
 *
 * @since 1.0
 */
public class KapuaBrokerSecurityPlugin implements BrokerPlugin {

    private static final Logger logger = LoggerFactory.getLogger(KapuaBrokerSecurityPlugin.class);

    @Override
    public Broker installPlugin(final Broker broker) throws Exception {
        logger.info("Installing Kapua broker plugin.");

        logger.debug("Starting Liquibase embedded client.");
        SystemSetting config = SystemSetting.getInstance();
        String dbUsername = config.getString(DB_USERNAME);
        String dbPassword = config.getString(DB_PASSWORD);
        String schema = firstNonNull(config.getString(DB_SCHEMA_ENV), config.getString(DB_SCHEMA));
        new KapuaLiquibaseClient(resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema)).update();

        try {
            // org.apache.shiro.config.Ini
            // org.apache.shiro.config.IniSecurityManagerFactory
            // org.apache.shiro.util.Factory;
            // Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
            // org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
            // SecurityUtils.setSecurityManager(securityManager);

            // Make the SecurityManager instance available to the entire application:
            Collection<Realm> realms = new ArrayList<>();
            try {
                realms.add(new org.eclipse.kapua.service.authentication.shiro.realm.UserPassAuthenticatingRealm());
                realms.add(new org.eclipse.kapua.service.authorization.shiro.KapuaAuthorizingRealm());
            } catch (KapuaException e) {
                // TODO add default realm???
            }

            DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
            defaultSecurityManager.setAuthenticator(new KapuaAuthenticator());
            logger.info("Set '{}' as shiro authenticator", KapuaAuthenticator.class);
            // if (defaultSecurityManager.getAuthenticator() instanceof ModularRealmAuthenticator) {
            // KapuaAuthenticationStrategy authenticationStrategy = new KapuaAuthenticationStrategy();
            // ((ModularRealmAuthenticator) defaultSecurityManager.getAuthenticator()).setAuthenticationStrategy(authenticationStrategy);
            // logger.info("Set '{}' as shiro authentication strategy ", KapuaAuthenticationStrategy.class);
            // }
            // else {
            // logger.warn("Cannot set '{}' as shiro authentication strategy! Authenticator class is '{}' and this option is only available for ModularRealmAuthenticator!",
            // new Object[]{KapuaAuthenticationStrategy.class, defaultSecurityManager.getAuthenticator() != null ? defaultSecurityManager.getAuthenticator().getClass() : "null"});
            // }

            defaultSecurityManager.setRealms(realms);
            SecurityUtils.setSecurityManager(defaultSecurityManager);

            // TODO in the old application this code was executed only inside the broker context
            // but now it's no more needed since we are using an external service (that returns a token) to authenticate the broker component
            if (defaultSecurityManager.getSessionManager() instanceof AbstractSessionManager) {
                ((AbstractSessionManager) defaultSecurityManager.getSessionManager()).setGlobalSessionTimeout(-1);
                logger.info("Shiro global session timeout set to indefinite.");
            } else {
                logger.warn("Cannot set Shiro global session timeout to indefinite.");
            }
            if (defaultSecurityManager.getSessionManager() instanceof AbstractValidatingSessionManager) {
                ((AbstractValidatingSessionManager) defaultSecurityManager.getSessionManager()).setSessionValidationSchedulerEnabled(false);
                logger.info("Shiro global session validator scheduler disabled.");
            } else {
                logger.warn("Cannot disable Shiro session validator scheduler.");
            }

            // install the filters
            return new KapuaSecurityBrokerFilter(broker);
        } catch (Throwable t) {
            logger.error("Error in plugin installation.", t);
            throw new SecurityException(t);
        }
    }

}
