/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.commons.eventbus;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.service.commons.MessageRouter;
import org.eclipse.kapua.service.commons.MessageRoutingContext;
import org.eclipse.kapua.service.commons.MessageServiceController;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;

/**
 * Base class for implementation of an EventBus processor.
 * 
 * @param <T> type of the message body
 */
public class BaseEventBusServiceProcessor<T> implements EventBusServiceProcessor<T> {

    private Vertx vertx;
    private MessageRouter<Message<T>> router;
    private Set<MessageServiceController<Message<T>>> controllers = new HashSet<>();

    public BaseEventBusServiceProcessor(Vertx aVertx) {
        vertx = aVertx;
    }

    @Override
    public BaseEventBusServiceProcessor<T> configuration(EventBusServiceConfiguration<T> aContext) {
        // serviceListeners.add(listener);
        return this;
    }

    @Override
    public final void process(MessageRoutingContext<Message<T>> aMessage) {
        router.handleContext(aMessage);
    }

    @Override
    public EventBusServiceConfiguration<T> getConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        router = MessageRouter.router(vertx, (key, value, message) -> {
            MultiMap headers = message.headers();
            if (key == null && (headers == null || headers.get(key) == null)) {
                return true;
            }
            if (key != null && headers != null && headers.get(key).equals(value)) {
                return true;
            }
            return false;
        });
        registerRoutes(router, controllers);
    }

    private void registerRoutes(MessageRouter<Message<T>> router, Set<MessageServiceController<Message<T>>> someControllers) {
        for (MessageServiceController<Message<T>> controller : someControllers) {
            controller.registerRoutes(router);
        }
    }
}
