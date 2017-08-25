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

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.eclipse.kapua.service.event.KapuaEventBus;
import org.eclipse.kapua.service.event.KapuaEventBusException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.event.service.internal.KapuaEventHousekeeper;
import org.eclipse.kapua.commons.event.service.internal.KapuaEventStoreFactoryImpl;
import org.eclipse.kapua.commons.event.service.internal.KapuaEventStoreServiceImpl;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventListResult;
import org.eclipse.kapua.service.event.KapuaEventStorePredicates;
import org.eclipse.kapua.service.event.KapuaEventStoreQuery;
import org.eclipse.kapua.service.event.KapuaEventStoreService;
import org.eclipse.kapua.service.event.KapuaEvent.EventStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventStoreHouseKeeperJob implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventStoreHouseKeeperJob.class);

    private final static long WAIT_TIME = SystemSetting.getInstance().getLong(SystemSettingKey.HOUSEKEEPER_EXECUTION_WAIT_TIME);
    private final static int EVENT_SCAN_WINDOW = SystemSetting.getInstance().getInt(SystemSettingKey.HOUSEKEEPER_EVENT_SCAN_WINDOW);

    private final Object monitor = new Object();

    private KapuaEventStoreService kapuaEventService;

    private EntityManager manager;

    private KapuaEventBus eventbus;
    private String serviceInternalEventAddress;
    private String[] servicesNames;
    private boolean running;

    public EventStoreHouseKeeperJob(EntityManagerFactory entityManagerFactory, KapuaEventBus eventbus, String serviceInternalEventAddress, List<String> servicesNameList) throws KapuaException {
        this.eventbus = eventbus;
        this.serviceInternalEventAddress = serviceInternalEventAddress;
        servicesNames = new String[0];
        servicesNames = servicesNameList.toArray(servicesNames);
        manager = entityManagerFactory.createEntityManager();
        kapuaEventService = new KapuaEventStoreServiceImpl(entityManagerFactory);
    }

    @Override
    public void run() {
        //TODO handling events table cleanup
        running = true;
        while(running) {
            waitStep();
            for (String serviceName : servicesNames) {
                try {
                    if (running) {
                        KapuaSecurityUtils.doPrivileged(() -> {
                            processServiceEvents(serviceName);
                        });
                    }
                }
                catch (KapuaException e) {
                    LOGGER.warn("Generic error {}", e.getMessage(), e);
                }
                finally {
                    //remove the lock if present
                    if (manager.isTransactionActive()) {
                        manager.rollback();
                    }
                }
            }
        }
        running = false;
    }

    private void processServiceEvents(String serviceName) throws KapuaException {
        try {
            LOGGER.trace("Scan not processed events for service '{}'", serviceName);
            Date startRun = Date.from(KapuaDateUtils.getKapuaSysDate());
            //try to acquire lock
            KapuaEventHousekeeper kapuaEventHousekeeper = getLock(serviceName);
            //scan unprocessed events
            KapuaEventStoreQuery query = new KapuaEventStoreFactoryImpl().newQuery(null);
            AttributePredicate<EventStatus> eventStatusPredicate = new AttributePredicate<>(KapuaEventStorePredicates.EVENT_STATUS, EventStatus.FIRED);
            AttributePredicate<String> serviceNamePredicate = new AttributePredicate<>(KapuaEventStorePredicates.SERVICE_NAME, serviceName);
            AndPredicate andPredicate = new AndPredicate();
            andPredicate.and(eventStatusPredicate);
            andPredicate.and(serviceNamePredicate);
            query.setPredicate(andPredicate);
            query.setLimit(EVENT_SCAN_WINDOW);
            KapuaEventListResult unsentMessagesList = kapuaEventService.query(query);
            //send unprocessed events
            if (!unsentMessagesList.isEmpty()) {
                for (KapuaEvent kapuaEvent : unsentMessagesList.getItems()) {
                    try {
                        LOGGER.info("publish event: service '{}' - operation '{}' - id '{}'", new Object[]{kapuaEvent.getService(), kapuaEvent.getOperation(), kapuaEvent.getContextId()});
                        eventbus.publish(serviceInternalEventAddress, kapuaEvent);
                        //if message was sent successfully then confirm the event in the event table
                        //if something goes wrong during this update the event message may be raised twice (but this condition should happens rarely and it is compliant to the contract of the service events)
                        //this is done in a different transaction
                        kapuaEventService.update(kapuaEvent);
                    } catch (KapuaEventBusException e) {
                        LOGGER.warn("Exception publishing event: {}", e.getMessage(), e);
                    } catch (KapuaException e) {
                      //this may be a valid condition if the HouseKeeper is doing the update concurrently with this task
                        LOGGER.warn("Exception acknowledging event: {}", e.getMessage(), e);
                    }
                }
            }
            //release lock
            updateLock(kapuaEventHousekeeper, serviceName, startRun);
        }
        catch (LockException | NoExecutionNeededException e) {
            LOGGER.trace("The lock is handled by someone else or the last execution was to close");
        }
        finally {
            //remove the lock if present
            if (manager.isTransactionActive()) {
                manager.rollback();
            }
        }
    }

    private void waitStep() {
        try {
            synchronized (monitor) {
                monitor.wait(WAIT_TIME);
            }
        } catch (InterruptedException e) {
            LOGGER.warn("Exception waiting for next scheduled execution: {}", e.getMessage(), e);
        }
    }

    public void stop() {
        running = false;
        synchronized (monitor) {
            monitor.notify();
        }
    }

    private KapuaEventHousekeeper getLock(String serviceName) throws LockException, NoExecutionNeededException {
        KapuaEventHousekeeper kapuaEventHousekeeper = null;
        try {
            manager.beginTransaction();
            kapuaEventHousekeeper = manager.findWithLock(KapuaEventHousekeeper.class, serviceName);
        }
        catch (Exception e) {
            throw new LockException(String.format("Cannot acquire lock: %s", e.getMessage()), e);
        }
        // Check last housekeeper run
        if (KapuaDateUtils.getKapuaSysDate().isBefore(kapuaEventHousekeeper.getLastRunOn().toInstant().plus(Duration.of(WAIT_TIME, ChronoUnit.MILLIS)))) {
            throw new NoExecutionNeededException("Not enough time since the last execution");
        }
        return kapuaEventHousekeeper;
    }

    private void updateLock(KapuaEventHousekeeper kapuaEventHousekeeper, String serviceName, Date startRun) throws KapuaException {
        kapuaEventHousekeeper.setLastRunBy(serviceName);
        kapuaEventHousekeeper.setLastRunOn(startRun);
        manager.persist(kapuaEventHousekeeper);
        manager.commit();
    }

    private class LockException extends Exception {

        private static final long serialVersionUID = 3099804470559976126L;

        public LockException(String msg, Throwable t) {
            super(msg, t);
        }
    }

    private class NoExecutionNeededException extends Exception {

        private static final long serialVersionUID = 7292333466656851052L;

        public NoExecutionNeededException(String msg) {
            super(msg);
        }

    }

}