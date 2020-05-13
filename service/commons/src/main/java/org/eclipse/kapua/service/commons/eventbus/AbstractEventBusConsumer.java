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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.service.commons.AbstractContainer;
import org.eclipse.kapua.service.commons.LifecycleHandler;
import org.eclipse.kapua.service.commons.MessageRoute.Matcher;
import org.eclipse.kapua.service.commons.MessageRouter;
import org.eclipse.kapua.service.commons.MessageRoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.ext.healthchecks.HealthCheckHandler;

public abstract class AbstractEventBusConsumer<T> extends AbstractContainer implements EventBusConsumer {

    private static Logger logger = LoggerFactory.getLogger(AbstractEventBusConsumer.class);

    private Vertx vertx;
    private EventBusConsumerOptions config;
    private Map<String, List<LifecycleHandler<MessageRoutingContext<Message<T>>>>> handlers;
    private Map<String, MessageConsumer<T>> consumers = new HashMap<>();

    protected AbstractEventBusConsumer(Vertx vertx, EventBusConsumerOptions config, Map<String, List<LifecycleHandler<MessageRoutingContext<Message<T>>>>> handlers) {
        this.vertx = vertx;
        this.config = config;
        this.handlers = handlers;
    }

    public abstract MessageConsumer<T> consumer(String address);

    public abstract MessageConsumer<T> consumer(String address, Handler<Message<T>> handler);

    public abstract void connect(Future<Void> startFuture) throws Exception;

    public abstract void close(Future<Void> stopFuture) throws Exception;

    @Override
    public void internalStart(Future<Void> startFuture) throws Exception {

        try {
            Future<Void> localStartFuture = Future.future();
            localStartFuture.setHandler(startReq -> {
                if (startReq.succeeded()) {
                    logger.info("Consumer service started, name {}", config.getName());
                    startFuture.complete();
                } else {
                    logger.warn("Error starting consumer service, name {}: {}",
                            config.getName(),
                            startReq.cause().getMessage(),
                            startReq.cause());
                    startFuture.fail(startReq.cause());
                }
            });

            MessageRouter<Message<T>> router = MessageRouter.router(vertx, new Matcher<Message<T>>() {

                @Override
                public boolean match(String key, String value, Message<T> message) {
                    // TODO Auto-generated method stub
                    return false;
                }

            });

            Future<Void> clientFuture = Future.future();
            for (String address : handlers.keySet()) {
                MessageConsumer<T> consumer = consumer(address, router::handle);
                consumers.put(address, consumer);
                for (LifecycleHandler<MessageRoutingContext<Message<T>>> handler:handlers.get(address)) {
                    router.route().where("address", address).handler(handler);
                    handler.init();
                }
            }
            try {
                connect(clientFuture);
            } catch (Exception e) {
                clientFuture.fail(e);
            }
        } catch (Exception e) {
            startFuture.fail(e);
        }
    }

    @Override
    public void internalStop(Future<Void> stopFuture) throws Exception {
        for (MessageConsumer<T> consumer:consumers.values()) {
            consumer.unregister();
        }
        close(stopFuture);
    }

    @Override
    public void registerLivenessCheckers(HealthCheckHandler anHandler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerReadinessCheckers(HealthCheckHandler anHandler) {
        // TODO Auto-generated method stub

    }
}
