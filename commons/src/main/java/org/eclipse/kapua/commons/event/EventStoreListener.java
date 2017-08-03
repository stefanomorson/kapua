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
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.commons.event.bus.EventBusException;
import org.eclipse.kapua.service.event.EventBusListener;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.ListenKapuaEvent;

public class EventStoreListener implements EventBusListener {
    
    public EventStoreListener() throws EventBusException {
    }
    
    @ListenKapuaEvent
    public void onKapuaEvent(KapuaEvent event) {
        // Mark the Event entry in the EventStore table
        // as 'processed' successfully.
    }
}
