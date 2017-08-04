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
package org.eclipse.kapua.commons.event.bus;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ComponentProvider;
import org.eclipse.kapua.commons.core.ServiceBundle;
import org.eclipse.kapua.commons.event.bus.jms.JMSEventBus;
import org.eclipse.kapua.locator.inject.MultiService;

@ComponentProvider
@MultiService(provides = ServiceBundle.class)
public class EventBusBundle implements ServiceBundle {

    @Inject
    private JMSEventBus jmsEventBus;
    
    @Override
    public void start() throws KapuaException {
        jmsEventBus.start();
    }

    @Override
    public void stop() throws KapuaException {
        jmsEventBus.stop();
    }
    

}
