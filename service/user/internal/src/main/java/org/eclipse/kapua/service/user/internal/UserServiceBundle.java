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
package org.eclipse.kapua.service.user.internal;

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
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.inject.MultiService;
import org.eclipse.kapua.service.user.UserService;

@ComponentProvider
@MultiService(provides = ServiceBundle.class)
public class UserServiceBundle implements ServiceBundle {

    @Inject
    private EventBus eventbus;
    private UserService userService;

    private EventStoreListener eventStoreListener;
    private ScheduledExecutorService houseKeeperScheduler;

    @Override
    public void start() throws KapuaException {

        userService = KapuaLocator.getInstance().getService(UserService.class);
        // Listen to upstream service events
        eventbus.subscribe("account.user", userService); //the event bus implicitly will add event. as prefix for each publish/subscribe
        
        // Event store listener
        String userEventsAddressSubscribe = "user.user"; //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventStoreListener = new EventStoreListener();
        eventbus.subscribe(userEventsAddressSubscribe, eventStoreListener);

        // Start the House keeper
        houseKeeperScheduler = Executors.newScheduledThreadPool(1);

        String userEventsAddressPublish = "user"; //the event bus implicitly will add event. as prefix for each publish/subscribe
        Runnable houseKeeperJob = new HouseKeeperJob(eventbus, userEventsAddressPublish);
        // Start time can be made random from 0 to 30 seconds
        final ScheduledFuture<?> beeperHandle = houseKeeperScheduler.scheduleAtFixedRate(houseKeeperJob, 30, 30, TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws KapuaException {
        houseKeeperScheduler.shutdown();
    }

}
