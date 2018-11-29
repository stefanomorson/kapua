/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.core.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

/**
 * Implements an {@link EventBusService} client.
 * <p>
 * It is used to implement a request/response interaction
 * through the {@link Vertx} {@link EventBus}.
 *
 */
public class EventBusClient {

    private EventBus eventBus;

    private EventBusClient(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public static EventBusClient create(EventBus eventBus) {
        EventBusClient client = new EventBusClient(eventBus);
        return client;
    }

    public EventBusClientRequest getRequest(String address, String action) {
        EventBusClientRequest request = new EventBusClientRequest(eventBus)
                .address(address)
                .addHeader(EventBusMessageConstants.ACTION, action);
        return request;
    }
}
