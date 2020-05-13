/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.kapua.service.commons.LifecycleHandler;
import org.eclipse.kapua.service.commons.MessageRoutingContext;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;

public class EventBusConsumerImpl<T> extends AbstractEventBusConsumer<T> {

    public static class Builder<T> implements EventBusConsumerBuilder<T> {

        private Vertx vertx;
        private EventBusConsumerOptions config;
        private Map<String, List<LifecycleHandler<MessageRoutingContext<Message<T>>>>> handlers = new HashMap<>();

        public Builder(Vertx aVertx) {
            this(aVertx, EventBusConsumerOptions.create());
        }

        public Builder(Vertx aVertx, EventBusConsumerOptions aConfig) {
            vertx = aVertx;
            config = aConfig;
        }

        public Vertx getVertx() {
            return vertx;
        }

        @Override
        public EventBusConsumerOptions getConfiguration() {
            return config;
        }

        public Map<String, List<LifecycleHandler<MessageRoutingContext<Message<T>>>>> getHandlers() {
            return handlers;
        }

        @Override
        public Builder<T> registerHandler(String address, LifecycleHandler<MessageRoutingContext<Message<T>>> handler) {
            Objects.requireNonNull(address, "param: address must be not null");
            Objects.requireNonNull(handler, "param: handler must be not null");

            if (!handlers.containsKey(address)) {
                handlers.put(address, new ArrayList<>());
            }
            handlers.get(address).add(handler);
            return this;
        }

        @Override
        public EventBusConsumer build() {
            return new EventBusConsumerImpl<T>(this);
        }
    }

    private Builder<T> builder;

    protected EventBusConsumerImpl(Builder<T> aBuilder) {
        super(aBuilder.getVertx(), aBuilder.getConfiguration(), aBuilder.getHandlers());
        builder = aBuilder;
    }

    @Override
    public MessageConsumer<T> consumer(String address) {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public MessageConsumer<T> consumer(String address, Handler<Message<T>> handler) {
        return builder.getVertx().eventBus().consumer(address, handler);
    }

    @Override
    public void connect(Future<Void> startFuture) throws Exception {
        startFuture.complete();
    }

    @Override
    public void close(Future<Void> stopFuture) throws Exception {
        stopFuture.complete();
    }
}
