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
package org.eclipse.kapua.service.commons.kafka;

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
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;

public abstract class AbstractKafkaConsumer<K1, V1> extends AbstractContainer implements KafkaConsumer {

    private static Logger logger = LoggerFactory.getLogger(AbstractKafkaConsumer.class);

    private Vertx vertx;
    private KafkaContainerOptions config;
    private Map<String, List<LifecycleHandler<MessageRoutingContext<KafkaConsumerRecord<K1, V1>>>>> handlers;
    private Map<String, io.vertx.kafka.client.consumer.KafkaConsumer<K1, V1>> consumers = new HashMap<>();

    protected AbstractKafkaConsumer(Vertx vertx, KafkaContainerOptions config, Map<String, List<LifecycleHandler<MessageRoutingContext<KafkaConsumerRecord<K1, V1>>>>> handlers) {
        this.vertx = vertx;
        this.config = config;
        this.handlers = handlers;
    }

    public abstract io.vertx.kafka.client.consumer.KafkaConsumer<K1, V1> consumer(String address);

    public abstract io.vertx.kafka.client.consumer.KafkaConsumer<K1, V1> consumer(String address, Handler<KafkaConsumerRecord<K1, V1>> handler);

    public abstract void connect(Future<Void> startFuture) throws Exception;

    public abstract void close(Future<Void> stopFuture) throws Exception;

    @Override
    public void registerLivenessCheckers(HealthCheckHandler anHandler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerReadinessCheckers(HealthCheckHandler anHandler) {
        // TODO Auto-generated method stub

    }

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

            MessageRouter<KafkaConsumerRecord<K1, V1>> router = MessageRouter.router(vertx, new Matcher<KafkaConsumerRecord<K1, V1>>() {

                @Override
                public boolean match(String key, String value, KafkaConsumerRecord<K1, V1> message) {
                    // TODO Auto-generated method stub
                    return false;
                }

            });

            Future<Void> clientFuture = Future.future();
            for (String address : handlers.keySet()) {
                io.vertx.kafka.client.consumer.KafkaConsumer<K1, V1> consumer = consumer(address);
                consumer.handler(router::handle);
                consumers.put(address, consumer);
                for (LifecycleHandler<MessageRoutingContext<KafkaConsumerRecord<K1, V1>>> handler : handlers.get(address)) {
                    router.route().where("topic", address).handler(handler);
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
        for (io.vertx.kafka.client.consumer.KafkaConsumer<K1, V1> consumer : consumers.values()) {
            consumer.unsubscribe();
            consumer.close();
        }
        close(stopFuture);
    }
}
