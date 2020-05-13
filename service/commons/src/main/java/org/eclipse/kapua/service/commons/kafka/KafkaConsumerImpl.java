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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.eclipse.kapua.service.commons.LifecycleHandler;
import org.eclipse.kapua.service.commons.MessageRoutingContext;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;

class KafkaConsumerImpl<K1, V1> extends AbstractKafkaConsumer<K1, V1> {

    //private static Logger logger = LoggerFactory.getLogger(KafkaConsumerImpl.class);

    public static class Builder<K1, V1> implements KafkaConsumerBuilder<K1, V1> {

        private Vertx vertx;
        private KafkaContainerOptions config;
        private Map<String, List<LifecycleHandler<MessageRoutingContext<KafkaConsumerRecord<K1, V1>>>>> handlers = new HashMap<>();

        public Builder(Vertx aVertx) {
            this(aVertx, new KafkaContainerOptions());
        }

        public Builder(Vertx aVertx, KafkaContainerOptions aConfig) {
            vertx = aVertx;
            config = aConfig;
        }

        public Vertx getVertx() {
            return vertx;
        }

        @Override
        public KafkaContainerOptions getConfiguration() {
            return config;
        }

        public Map<String, List<LifecycleHandler<MessageRoutingContext<KafkaConsumerRecord<K1, V1>>>>> getHandlers() {
            return handlers;
        }

        @Override
        public Builder<K1, V1> registerHandler(String address, LifecycleHandler<MessageRoutingContext<KafkaConsumerRecord<K1, V1>>> handler) {
            Objects.requireNonNull(address, "param: address must be not null");
            Objects.requireNonNull(handler, "param: handler must be not null");

            if (!handlers.containsKey(address)) {
                handlers.put(address, new ArrayList<>());
            }
            handlers.get(address).add(handler);
            return this;
        }

        @Override
        public KafkaConsumerImpl<K1, V1> build() {
            return new KafkaConsumerImpl<K1, V1>(this);
        }
    }

    private Builder<K1, V1> builder;

    protected KafkaConsumerImpl(Builder<K1, V1> aBuilder) {
        super(aBuilder.getVertx(), aBuilder.getConfiguration(), aBuilder.getHandlers());
        builder = aBuilder;
    }

    @Override
    public io.vertx.kafka.client.consumer.KafkaConsumer<K1, V1> consumer(String address) {
        // TODO Add properties
        return io.vertx.kafka.client.consumer.KafkaConsumer.<K1, V1>create(builder.getVertx(), (Properties)null);
    }

    @Override
    public io.vertx.kafka.client.consumer.KafkaConsumer<K1, V1> consumer(String address, Handler<KafkaConsumerRecord<K1, V1>> handler) {
        // TODO Add properties
        return io.vertx.kafka.client.consumer.KafkaConsumer.<K1, V1>create(builder.getVertx(), (Properties)null).handler(handler);
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
