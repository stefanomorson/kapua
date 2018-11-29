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

import java.util.Objects;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

/**
 * Implements an {@link EventBusService} client.
 * <p>
 * It is used to implement a request/response interaction
 * through the {@link Vertx} {@link EventBus}.
 *
 */
public class EventBusClientRequest {

    private EventBus eventBus;
    private String address;
    private DeliveryOptions options;

    private JsonObject request;

    public EventBusClientRequest(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public EventBusClientRequest addHeader(String name, String value) {
        getOptions().addHeader(name, value);
        return this;
    }

    public EventBusClientRequest property(String name, String value) {
        if (Objects.requireNonNull(name).equals(EventBusMessageConstants.BODY)) {
            throw new IllegalArgumentException(String.format("Invalid property name %s", name));
        }
        getRequest().put(name, Objects.requireNonNull(value));
        return this;
    }

    public EventBusClientRequest address(String anAddress) {
        this.address = anAddress;
        return this;
    }

    public EventBusClientRequest body(JsonObject aBody) {
        getRequest().put(EventBusMessageConstants.BODY, aBody);
        return this;
    }

    public void send(Handler<AsyncResult<EventBusClientResponse>> response) {
        eventBus.send(address, getRequest(), getOptions(), replyEvent -> {
            if (replyEvent.succeeded()) {
                JsonObject reply = (JsonObject) replyEvent.result().body();
                if (reply != null) {
                    EventBusClientResponse replyResponse = new EventBusClientResponse(reply);
                    response.handle(Future.succeededFuture(replyResponse));
                } else {
                    response.handle(Future.succeededFuture(new EventBusClientResponse(EventBusMessageConstants.STATUS_INTERNAL_ERROR)));
                }
            } else {
                response.handle(Future.failedFuture(replyEvent.cause()));
            }
        });
    }

    private JsonObject getRequest() {
        if (request == null) {
            request = new JsonObject();
        }
        return request;
    }

    private DeliveryOptions getOptions() {
        if (options == null) {
            options = new DeliveryOptions();
        }
        return options;
    }
}
