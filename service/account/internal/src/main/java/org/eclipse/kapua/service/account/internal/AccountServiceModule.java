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
package org.eclipse.kapua.service.account.internal;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ServiceModule;
import org.eclipse.kapua.commons.event.bus.EventBusManager;
import org.eclipse.kapua.commons.event.service.EventStoreHouseKeeperJob;
import org.eclipse.kapua.commons.event.service.internal.KapuaEventStoreServiceImpl;
import org.eclipse.kapua.commons.event.service.internal.ServiceMap;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.account.internal.setting.KapuaAccountSetting;
import org.eclipse.kapua.service.account.internal.setting.KapuaAccountSettingKeys;
import org.eclipse.kapua.service.event.KapuaEventBus;
import org.eclipse.kapua.service.event.KapuaEventStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class AccountServiceModule implements ServiceModule {
    //TODO make sense to create an abstract module to handle the housekeeper executor start and stop?

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceModule.class);

    private final static int MAX_WAIT_LOOP_ON_SHUTDOWN = 30;
    private final static long WAIT_TIME = 1000;

    private KapuaEventStoreService kapuaEventStoreService;

    private ScheduledExecutorService houseKeeperScheduler;
    private ScheduledFuture<?> houseKeeperHandler;
    private EventStoreHouseKeeperJob houseKeeperJob;

    @Override
    public void start() throws KapuaException {

        KapuaEventBus eventbus = EventBusManager.getInstance();

        // No upstream service events to listen to

        // Event store listener
        EntityManagerFactory entityManagerFactory = AccountEntityManagerFactory.getInstance();
        kapuaEventStoreService = new KapuaEventStoreServiceImpl(entityManagerFactory);

        // the event bus implicitly will add event. as prefix for each publish/subscribe
        List<String> servicesNames = KapuaAccountSetting.getInstance().getList(String.class, KapuaAccountSettingKeys.ACCOUNT_SERVICES_NAMES);

        String serviceInternalEventAddress = KapuaAccountSetting.getInstance().getString(KapuaAccountSettingKeys.ACCOUNT_INTERNAL_EVENT_ADDRESS);
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
