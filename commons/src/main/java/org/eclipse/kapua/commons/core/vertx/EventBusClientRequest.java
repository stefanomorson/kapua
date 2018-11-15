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

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Implements an {@link EventBusService} client.
 * <p>
 * It is used to implement a request/response interaction
 * through the {@link Vertx} {@link EventBus}.
 *
 */
public class EventBusClientRequest {

    private static Logger logger = LoggerFactory.getLogger(EventBusClientRequest.class);

    private MessageProducer<JsonObject> producer;

    private JsonObject request;

    public EventBusClientRequest(MessageProducer<JsonObject> producer) {
        this.producer = producer;
    }

    public JsonObject asJsonObject() {
        return request;
    }

    public EventBusClientRequest putHeader(String name, String value) {
        getHeaders().put(name, value);
        return this;
    }

    public EventBusClientRequest action(String anAction) {
        getRequest().put(EventBusMessageConstants.ACTION, anAction);
        return this;
    }

    public EventBusClientRequest body(JsonObject aBody) {
        getRequest().put(EventBusMessageConstants.BODY, aBody);
        return this;
    }

    public void send(Handler<AsyncResult<EventBusClientResponse>> response) {
        producer.send(request, replyEvent -> {
            if (replyEvent.succeeded()) {
                JsonObject reply = (JsonObject) replyEvent.result().body();
                if (reply != null) {
                    EventBusClientResponse replyResponse = new EventBusClientResponse(reply);
                    response.handle(Future.succeededFuture(replyResponse));
                } else {
                    response.handle(Future.failedFuture(new EventBusServerResponseException(EventBusMessageConstants.STATUS_INTERNAL_ERROR)));
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

    private JsonObject getHeaders() {
        JsonObject headers;
        if (!getRequest().containsKey("headers")) {
            headers = new JsonObject();
            getRequest().put("headers", headers);
        } else {
            headers = getRequest().getJsonObject("headers");
        }
        return headers;
    }
}
