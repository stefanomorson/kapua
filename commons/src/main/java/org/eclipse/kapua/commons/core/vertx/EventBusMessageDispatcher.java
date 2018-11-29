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

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

/**
 * Dispatches incoming {@link EventBusServerRequest} to registered handlers.
 * <p>
 * Each handler is identified by an action that has to be unique
 * across all the registered handlers. Responses are sent back to the sender
 * replying to the message.
 * 
 *
 */
public class EventBusMessageDispatcher {

    private Map<String, EventBusMessageHandler> handlers = new HashMap<>();
    private Vertx vertx;
    private EventBusServer server;

    private EventBusMessageDispatcher(Vertx aVertx, EventBusServer aServer) {
        vertx = aVertx;
        server = aServer;
        server.setRequestHandler(this::dispatch);
    }

    public static EventBusMessageDispatcher dispatcher(Vertx aVertx, EventBusServer aServer) {
        return new EventBusMessageDispatcher(aVertx, aServer);
    }

    public void registerHandler(String action, EventBusMessageHandler handler) {
        if (!handlers.containsKey(action)) {
            handlers.put(action, handler);
        } // TODO Handle the case when the handler is discarded
    }

    public void registerBlockingHandler(String action, EventBusMessageHandler handler) {
        if (!handlers.containsKey(action)) {
            handlers.put(action, (request,response) -> { 
                vertx.executeBlocking(fut -> {
                    handler.handle(request, response);
                    fut.complete();
                }, 
                ar -> {});
            });
        } // TODO Handle the case when the handler is discardeds
    }

    public void dispatch(EventBusServerRequest request, Handler<AsyncResult<EventBusServerResponse>> handler) {
        String action = request.getHeaders().get(EventBusMessageConstants.ACTION);
        if (action == null || action.isEmpty()) {
            handler.handle(Future.succeededFuture(EventBusServerResponse.create(EventBusMessageConstants.STATUS_BAD_REQUEST)));
            return;
        }
        if (handlers == null || handlers.size() == 0 || !handlers.containsKey(action)) {
            handler.handle(Future.succeededFuture(EventBusServerResponse.create(EventBusMessageConstants.STATUS_NOT_FOUND)));
            return;
        }
        handlers.get(action).handle(request, responseEvt -> {
            handler.handle(Future.succeededFuture(
                    EventBusServerResponse.create(EventBusMessageConstants.STATUS_NOT_FOUND)
                        .setResultCode(responseEvt.result().getResultCode())
                        .setResultCodeMessage(responseEvt.result().getResultCodeMessage())
                        .setBody(responseEvt.result().getBody())));
        });
    }
}
