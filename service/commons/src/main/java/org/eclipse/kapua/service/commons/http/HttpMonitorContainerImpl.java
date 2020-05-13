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
package org.eclipse.kapua.service.commons.http;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.eclipse.kapua.service.commons.AbstractContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class HttpMonitorContainerImpl extends AbstractContainer implements HttpMonitorContainer {

    private static Logger logger = LoggerFactory.getLogger(HttpMonitorContainerImpl.class);

    public static class Builder implements HttpMonitorContainerBuilder {

        private Vertx vertx;
        private HttpMonitorContainerOptions config;
        private HttpMonitorProcessor processor;

        public Builder(Vertx vertx) {
            this(vertx, new HttpMonitorContainerOptions());
        }

        public Builder(Vertx vertx, HttpMonitorContainerOptions config) {
            this.config = config;
        }

        public Vertx getVertx() {
            return vertx;
        }

        @Override
        public HttpMonitorContainerOptions getConfiguration() {
            return config;
        }

        public HttpMonitorProcessor getProcessor() {
            return processor;
        }

        @Override
        public HttpMonitorContainerBuilder processor(HttpMonitorProcessor processor) {
            this.processor = processor;
            return this;
        }

        @Override
        public HttpMonitorContainer build() {
            Objects.requireNonNull(config, "member: config");
            return new HttpMonitorContainerImpl(this);
        }
    }

    private HttpContainer container;
    private Builder builder;

    private HttpMonitorContainerImpl(Builder aBuilder) {
        Objects.requireNonNull(aBuilder, "param: builder");
        builder = aBuilder;
    }

    @Override
    public void internalStart(Future<Void> startFuture) throws Exception {
        Objects.requireNonNull(startFuture, "param: startFuture");
        try {
            HttpContainerOptions httpServiceConfig = new HttpContainerOptions();
            httpServiceConfig.setName(builder.getConfiguration().getName());
            httpServiceConfig.setInstances(1);
            httpServiceConfig.setRootPath(builder.getConfiguration().getRootPath());
            httpServiceConfig.setEndpoint(builder.getConfiguration().getEndpoint());
            container = HttpContainerBuilder.builder(builder.getVertx(), httpServiceConfig)
                    .registerHandler("", builder.getProcessor())
                    .build();
            container.start(startFuture);
        } catch (Exception e) {
            startFuture.fail(e);
        }
    }

    @Override
    public void internalStop(@NotNull Future<Void> stopFuture) throws Exception {
        Objects.requireNonNull(stopFuture, "param: stopFuture");

        // Stop the deployment
        if (container != null) {
            Future<Void> localStopFuture = Future.future();
            localStopFuture.setHandler(stopReq -> {
                if (stopReq.succeeded()) {
                    logger.info("HTTP monitoring service stopped, name {}, address {}, port {}, {}",
                            builder.getConfiguration().getName(),
                            builder.getConfiguration().getEndpoint().getBindAddress(),
                            builder.getConfiguration().getEndpoint().getPort(),
                            builder.getConfiguration().getEndpoint().isSsl() ? "Secure mode" : "Insecure mode");
                    stopFuture.complete();
                } else {
                    logger.warn("Error stopping monitoring service, name {}: {}",
                            builder.getConfiguration().getName(),
                            stopReq.cause().getMessage(),
                            stopReq.cause());
                    stopFuture.fail(stopReq.cause());
                }
            });
            container.stop(stopFuture);
            return;
        }
        stopFuture.complete();
    }
}
