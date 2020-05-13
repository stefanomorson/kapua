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
package org.eclipse.kapua.service.commons.amqp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.kapua.service.commons.LifecycleHandler;
import org.eclipse.kapua.service.commons.MessageRoutingContext;
import org.eclipse.kapua.service.commons.eventbus.AbstractEventBusConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.amqpbridge.AmqpBridge;
import io.vertx.amqpbridge.AmqpBridgeOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.net.PfxOptions;

public class AmqpBridgeConsumerImpl<T> extends AbstractEventBusConsumer<T> {

    private static Logger logger = LoggerFactory.getLogger(AmqpBridgeConsumerImpl.class);

    public static class Builder<T> implements AmqpBridgeConsumerBuilder<T> {

        private Vertx vertx;
        private AmqpBridgeConsumerOptions options;
        private Map<String, List<LifecycleHandler<MessageRoutingContext<Message<T>>>>> handlers;

        public Builder(Vertx aVertx) {
            this(aVertx, new AmqpBridgeConsumerOptions());
        }

        public Builder(Vertx aVertx, AmqpBridgeConsumerOptions config) {
            this.vertx = aVertx;
            this.options = config;
        }

        @Override
        public AmqpBridgeConsumerOptions getConfiguration() {
            return options;
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
        public AmqpBridgeConsumer build() {
            return null;
        }
    }

    private Builder<T> builder;
    private AmqpBridge bridge;

    protected AmqpBridgeConsumerImpl(Builder<T> aBuilder) {
        super(aBuilder.vertx, aBuilder.options, aBuilder.handlers);
    }

    @Override
    public MessageConsumer<T> consumer(String address) {
        return bridge.createConsumer(address);
    }

    @Override
    public MessageConsumer<T> consumer(String address, Handler<Message<T>> handler) {
        throw new RuntimeException("MethodNotImplemented");
    }

    @Override
    public void connect(Future<Void> startFuture) throws Exception {
        Future.succeededFuture()
                .compose(mapper -> {

                    Vertx vertx = builder.vertx;

                    AmqpBridgeClientOptions options = builder.getConfiguration().getClientOptions();
                    AmqpBridgeOptions bridgeOpts = new AmqpBridgeOptions();
                    bridgeOpts.setSsl(options.getEndpoint().isSsl());
                    bridgeOpts
                            .removeEnabledSecureTransportProtocol("SSLv2Hello")
                            .removeEnabledSecureTransportProtocol("TLSv1")
                            .removeEnabledSecureTransportProtocol("TLSv1.1");

                    if (options.getEndpoint().getKeyStorePath() != null) {
                        bridgeOpts.setPfxKeyCertOptions(new PfxOptions()
                                .setPath(options.getEndpoint().getKeyStorePath())
                                .setPassword(options.getEndpoint().getKeyStorePassword()));
                        logger.info("PFX KeyStore loaded: {}", options.getEndpoint().getKeyStorePath());
                    }

                    // Mutual Auth
                    if (options.getEndpoint().getTrustStorePath() != null) {
                        bridgeOpts.setPfxTrustOptions(new PfxOptions()
                                .setPath(options.getEndpoint().getTrustStorePath())
                                .setPassword(options.getEndpoint().getTrustStorePassword()));
                        logger.info("PFX TrustStore loaded: {}", options.getEndpoint().getTrustStorePath());
                    }
                    bridge = AmqpBridge.create(vertx, bridgeOpts);
                    Future<Void> startBridgeReq = Future.future();
                    bridge.start(options.getEndpoint().getHost(),
                            options.getEndpoint().getPort(),
                            options.getUsername(),
                            options.getPassword(), start -> {
                                if (!start.succeeded()) {
                                    logger.error("Error connecting AMQP bridge to host {} port {}: {}", options.getEndpoint().getHost(), options.getEndpoint().getPort(), start.cause().getMessage());
                                    startBridgeReq.fail(start.cause());
                                    return;
                                }
                                logger.info("AMQP bridge started at host {} port {}", options.getEndpoint().getHost(), options.getEndpoint().getPort());
                                startBridgeReq.complete();
                            });
                    return startBridgeReq;
                })
                .setHandler(handler -> {
                    if (handler.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(handler.cause());
                    }
                });
    }

    @Override
    public void close(Future<Void> stopFuture) throws Exception {
        if (bridge != null) {
            bridge.close(close -> {
                if (!close.succeeded()) {
                    logger.info("Bridge close failed", close.cause());
                    stopFuture.fail(close.cause());
                    return;
                }
            });
        }
        stopFuture.complete();
    }
}
