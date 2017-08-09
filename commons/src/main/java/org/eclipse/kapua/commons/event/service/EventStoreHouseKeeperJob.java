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
package org.eclipse.kapua.commons.event.service;

//import java.math.BigInteger;
//import java.time.Duration;
//import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.eclipse.kapua.service.event.KapuaEventBus;
import org.eclipse.kapua.service.event.KapuaEventBusException;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.event.service.internal.KapuaEventStoreFactoryImpl;
import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
//import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventListResult;
import org.eclipse.kapua.service.event.KapuaEventStorePredicates;
import org.eclipse.kapua.service.event.KapuaEventStoreQuery;
import org.eclipse.kapua.service.event.KapuaEventStoreService;
import org.eclipse.kapua.service.event.KapuaEvent.EVENT_STATUS;
import org.eclipse.kapua.service.event.KapuaEventHousekeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventStoreHouseKeeperJob implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventStoreHouseKeeperJob.class);

    final long waitTime = SystemSetting.getInstance().getLong(SystemSettingKey.HOUSEKEEPER_EXECUTION_WAIT_TIME);

    @Inject
    private KapuaEventStoreService kapuaEventService;
    @Inject
    private AbstractEntityManagerFactory entityManagerFactory;
    private KapuaEventBus eventbus;
    private String publishAddress;
    private String serviceName;
    private boolean running;

    public EventStoreHouseKeeperJob(KapuaEventBus eventbus, String publishAddress) {
        this.eventbus = eventbus;
        this.publishAddress = publishAddress;
        this.serviceName = publishAddress;
    }

    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                LOGGER.warn("Exception waiting for next scheduled execution: {}", e.getMessage(), e);
            }
            try {
                Date startRun = Date.from(KapuaDateUtils.getKapuaSysDate());
                //try to acquire lock
                KapuaEventHousekeeper kapuaEventHousekeeper = getLock();
                //scan unprocessed events
                KapuaEventStoreQuery query = new KapuaEventStoreFactoryImpl().newQuery(null);
                AttributePredicate<String> eventStatusPredicate = new AttributePredicate<>(KapuaEventStorePredicates.EVENT_STATUS, EVENT_STATUS.FIRED.name());
                AttributePredicate<String> serviceNamePredicate = new AttributePredicate<>(KapuaEventStorePredicates.SERVICE_NAME, serviceName);
                AndPredicate andPredicate = new AndPredicate();
                andPredicate.and(eventStatusPredicate);
                andPredicate.and(serviceNamePredicate);
                query.setPredicate(andPredicate);
                KapuaEventListResult unsentMessagesList = kapuaEventService.query(query);
                //send unprocessed events
                if (!unsentMessagesList.isEmpty()) {
                    for (KapuaEvent kapuaEvent : unsentMessagesList.getItems()) {
                        try {
                            eventbus.publish(publishAddress, kapuaEvent);
                        } catch (KapuaEventBusException e) {
                            LOGGER.warn("Exception publishing envent: {}", e.getMessage(), e);
                        }
                    }
                }
                //release lock
                updateLock(kapuaEventHousekeeper, startRun);
            }
            catch (LockException e) {
                LOGGER.debug("Cannot acquire lock. The lock is handled by someone else or the last execution was to close");
            }
            catch (KapuaException e) {
                LOGGER.warn("Generic error {}", e.getMessage(), e);
            }
        }
        running = false;
    }

    public void stop() {
        running = false;
    }

    private KapuaEventHousekeeper getLock() throws LockException {
//        KapuaEventHousekeeper kapuaEventHousekeeper = null;
//        EntityManager manager = entityManagerFactory.createEntityManager();
//        try {
//            kapuaEventHousekeeper = manager.find(KapuaEventHousekeeper.class, BigInteger.ONE);
//        }
//        catch (KapuaException e) {
//            throw new LockException(String.format("Cannot acquire lock: %s", e.getMessage()), e);
//        }
//        // Check last housekeeper run
//        if (KapuaDateUtils.getKapuaSysDate().isBefore(kapuaEventHousekeeper.getLastRunOn().toInstant().minus(Duration.of(waitTime, ChronoUnit.MILLIS)))) {
//            throw new LockException("Not enough time since the last execution");
//        }
//        return kapuaEventHousekeeper;
        return null;
    }

    private void updateLock(KapuaEventHousekeeper kapuaEventHousekeeper, Date startRun) throws KapuaException {
//        kapuaEventHousekeeper.setLastRunBy(serviceName);
//        kapuaEventHousekeeper.setLastRunOn(startRun);
//        kapuaEventHousekeeperService.update(kapuaEventHousekeeper);
    }

    private class LockException extends Exception {

        private static final long serialVersionUID = 3099804470559976126L;

        public LockException(String msg) {
            super(msg);
        }

        public LockException(String msg, Throwable t) {
            super(msg, t);
        }
    }
}
