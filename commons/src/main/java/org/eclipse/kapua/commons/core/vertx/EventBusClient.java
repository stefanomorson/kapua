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

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;

/**
 * Implements an {@link EventBusService} client.
 * <p>
 * It is used to implement a request/response interaction
 * through the {@link Vertx} {@link EventBus}.
 *
 */
public class EventBusClient {

    private MessageProducer<JsonObject> producer;

    private EventBusClient(MessageProducer<JsonObject> producer) {
        this.producer = producer;
    }

    public static EventBusClient create(EventBus eventBus, String address) {
        EventBusClient client = new EventBusClient(eventBus.sender(address));
        return client;
    }

    public static EventBusClientRequest create(EventBus eventBus, String address, DeliveryOptions options) {
        EventBusClientRequest client = new EventBusClientRequest(eventBus.sender(address, options));
        return client;
    }

    public EventBusClientRequest getRequest(String action) {
        EventBusClientRequest request = new EventBusClientRequest(producer).action(action);
        return request;
    }

    public void close() {
        if (producer != null) {
            producer.close();
        }
    }
}
