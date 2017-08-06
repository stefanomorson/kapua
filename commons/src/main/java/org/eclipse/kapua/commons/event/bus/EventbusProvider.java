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
package org.eclipse.kapua.commons.event.bus;

import org.eclipse.kapua.commons.event.bus.jms.JMSEventbus;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventbus;
import org.eclipse.kapua.service.event.KapuaEventbusException;
import org.eclipse.kapua.service.event.KapuaServiceEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventbusProvider implements KapuaEventbus {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventbusProvider.class);

    private static EventbusProvider instance;
    private static KapuaEventbusException initException;

    private JMSEventbus jmsEventbus;

    static {
        try {
            instance = new EventbusProvider();
        } catch (KapuaEventbusException e) {
            LOGGER.error("Error while initializing EventbusProvider", e);
            instance = null;
            initException = e;
        }
    }

    private EventbusProvider() throws KapuaEventbusException {
        jmsEventbus = new JMSEventbus();
    }

    public static EventbusProvider getInstance() throws KapuaEventbusException {
        if (initException != null) {
            throw initException;
        }
        return instance;
    }

    @Override
    public void publish(String address, KapuaEvent event) throws KapuaEventbusException {
        jmsEventbus.publish(address, event);
    }

    @Override
    public void subscribe(String address, KapuaServiceEventListener eventListener) throws KapuaEventbusException {
        jmsEventbus.subscribe(address, eventListener);
    }

    public void start() throws KapuaEventbusException {
        jmsEventbus.start();
    }

    public void stop() throws KapuaEventbusException {
        jmsEventbus.stop();
    }
}
