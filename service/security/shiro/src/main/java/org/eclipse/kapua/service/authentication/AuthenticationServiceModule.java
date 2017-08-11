/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ServiceModule;
import org.eclipse.kapua.commons.event.bus.EventBusManager;
import org.eclipse.kapua.commons.event.service.EventStoreHouseKeeperJob;
import org.eclipse.kapua.commons.event.service.internal.KapuaEventStoreServiceImpl;
import org.eclipse.kapua.commons.event.service.internal.ServiceMap;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;
import org.eclipse.kapua.service.event.KapuaEventBus;
import org.eclipse.kapua.service.event.KapuaEventStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class AuthenticationServiceModule implements ServiceModule {
    //TODO make sense to create an abstract module to handle the housekeeper executor start and stop?

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceModule.class);

    private final static int MAX_WAIT_LOOP_ON_SHUTDOWN = 30;
    private final static long WAIT_TIME = 1000;

    @Inject
    private CredentialService credentialService;

    @Inject
    private AccessTokenService accessTokenService;

    private KapuaEventStoreService kapuaEventStoreService;

    private ScheduledExecutorService houseKeeperScheduler;
    private ScheduledFuture<?> houseKeeperHandler;
    private EventStoreHouseKeeperJob houseKeeperJob;

    @Override
    public void start() throws KapuaException {

        KapuaEventBus eventbus = EventBusManager.getInstance();

        // Listen to upstream service events

        String upEvUserCredentialSubscribe = KapuaAuthenticationSetting.getInstance().getString(KapuaAuthenticationSettingKeys.USER_CREDENTIAL_UPSTREAM_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvUserCredentialSubscribe, credentialService);

        String upEvUserAccessTokenSubscribe = KapuaAuthenticationSetting.getInstance().getString(KapuaAuthenticationSettingKeys.USER_ACCESS_TOKEN_UPSTREAM_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvUserAccessTokenSubscribe, accessTokenService);

        String upEvAccountCredentialSubscribe = KapuaAuthenticationSetting.getInstance().getString(KapuaAuthenticationSettingKeys.ACCOUNT_CREDENTIAL_UPSTREAM_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvAccountCredentialSubscribe, credentialService);

        String upEvAccountAccessTokenSubscribe = KapuaAuthenticationSetting.getInstance().getString(KapuaAuthenticationSettingKeys.ACCOUNT_ACCESS_TOKEN_UPSTREAM_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvAccountAccessTokenSubscribe, accessTokenService);

        // Event store listener
        EntityManagerFactory entityManagerFactory = AuthenticationEntityManagerFactory.getInstance();
        kapuaEventStoreService = new KapuaEventStoreServiceImpl(entityManagerFactory);

        //the event bus implicitly will add event. as prefix for each publish/subscribe
        List<String> servicesNames = KapuaAuthenticationSetting.getInstance().getList(String.class, KapuaAuthenticationSettingKeys.AUTHENTICATION_SERVICES_NAMES);

        String serviceInternalEventAddress = KapuaAuthenticationSetting.getInstance().getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_INTERNAL_EVENT_ADDRESS);
        eventbus.subscribe(String.format("%s.%s", serviceInternalEventAddress, serviceInternalEventAddress), kapuaEventStoreService);

        //register events to the RaiseKapuaEventInterceptor
        ServiceMap.registerServices(serviceInternalEventAddress, servicesNames);

        // Start the House keeper
        houseKeeperScheduler = Executors.newScheduledThreadPool(1);
        houseKeeperJob = new EventStoreHouseKeeperJob(entityManagerFactory, eventbus, serviceInternalEventAddress, servicesNames);
        // Start time can be made random from 0 to 30 seconds
        houseKeeperHandler = houseKeeperScheduler.scheduleAtFixedRate(houseKeeperJob, 30, 30, TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws KapuaException {
        if (houseKeeperJob!=null) {
            houseKeeperJob.stop();
        }
        int waitLoop = 0;
        while(houseKeeperHandler.isDone()) {
            try {
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                //do nothing
            }
            if (waitLoop++ > MAX_WAIT_LOOP_ON_SHUTDOWN) {
                LOGGER.warn("Cannot cancel the house keeper task afeter a while!");
                break;
            }
        }
        if (houseKeeperScheduler != null) {
            houseKeeperScheduler.shutdown();
        }
    }
}
