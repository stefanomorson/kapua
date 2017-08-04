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
 *******************************************************************************/
package org.eclipse.kapua.service.account.internal;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ComponentProvider;
import org.eclipse.kapua.commons.core.ServiceBundle;
import org.eclipse.kapua.commons.event.EventStoreListener;
import org.eclipse.kapua.commons.event.HouseKeeperJob;
import org.eclipse.kapua.commons.event.bus.EventBus;
import org.eclipse.kapua.locator.inject.MultiService;
import org.eclipse.kapua.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ComponentProvider
@MultiService(provides = ServiceBundle.class)
public class AccountServiceBundle implements ServiceBundle {

    private final static Logger LOGGER = LoggerFactory.getLogger(AccountServiceBundle.class);

    @Inject
    private EventBus eventbus;

    @Inject
    private AccountService accountService;

    private EventStoreListener eventStoreListener;
    private ScheduledExecutorService houseKeeperScheduler;

    @Override
    public void start() throws KapuaException {
        LOGGER.info("Start ...");
        //eventbus.subscribe("updatream event addresses", accountService);

        // Event store listener 
        String accountEventsAddressSubscribe = "events.account.account";
        eventStoreListener = new EventStoreListener();
        eventbus.subscribe(accountEventsAddressSubscribe, eventStoreListener);

        // House keeper
        houseKeeperScheduler = Executors.newScheduledThreadPool(1);

        String accountEventsAddressPublish = "events.account";
        Runnable houseKeeperJob = new HouseKeeperJob(eventbus, accountEventsAddressPublish);
        // Start time can be made random from 0 to 30 seconds
        final ScheduledFuture<?> beeperHandle = houseKeeperScheduler.scheduleAtFixedRate(houseKeeperJob, 30, 30,
                TimeUnit.SECONDS);
        LOGGER.info("Start ... DONE");
    }

    @Override
    public void stop() throws KapuaException {
        houseKeeperScheduler.shutdown();
    }

}
