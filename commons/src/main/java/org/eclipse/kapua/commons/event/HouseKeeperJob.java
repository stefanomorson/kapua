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

import org.eclipse.kapua.commons.event.bus.EventBus;

public class HouseKeeperJob implements Runnable {

    private EventBus eventbus;
    private String address;
    
    public HouseKeeperJob(EventBus eventbus, String address) {
        this.eventbus = eventbus;
        this.address = address;
    }
    
    @Override
    public void run() {
        // Execute 
        // Check last housekeeper run
        // Send unprocessed events
        // Cleanup event store
        // Update last housekeeper run
    }

}
