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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.kapua.service.commons.AbstractContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PfxOptions;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

class HttpContainerImpl extends AbstractContainer implements HttpContainer {

    private static Logger logger = LoggerFactory.getLogger(HttpContainerImpl.class);

    public static class Builder implements HttpContainerBuilder {

        private Vertx vertx;
        private Map<String, List<Handler<RoutingContext>>> handlers = new HashMap<>();
        private HttpContainerOptions config;

        public Builder(Vertx vertx) {
            this(vertx, new HttpContainerOptions());
        }

        public Builder(Vertx vertx, HttpContainerOptions config) {
            this.vertx = vertx;
            this.config = config;
        }

        public Vertx getVertx() {
            return vertx;
        }

        @Override
        public HttpContainerOptions getConfiguration() {
            return config;
        }

        @Override
        public Builder registerHandler(String address, Handler<RoutingContext> handler) {
            Objects.requireNonNull(address, "param: address must be not null");
            Objects.requireNonNull(handler, "param: handler must be not null");

            if (!handlers.containsKey(address)) {
                handlers.put(address, new ArrayList<>());
            }
            handlers.get(address).add(handler);
            return this;
        }

        public Map<String, List<Handler<RoutingContext>>> getHandlers() {
            return handlers;
        }

        @Override
        public HttpContainerImpl build() {
            Objects.requireNonNull(config, "member: config");
            if (config.getInstances() < 1) {
                throw new IllegalArgumentException("Number of instances must be higher than zero");
            }
            return new HttpContainerImpl(this);
        }
    }

    private Builder builder;
    private Router rootRouter;
    private HttpServer server;

    private HttpContainerImpl(Builder aBuilder) {
        builder = aBuilder;
    }

    @Override
    public void internalStart(Future<Void> startFuture) throws Exception {
        Objects.requireNonNull(startFuture, "param: startFuture");

        logger.debug("Starting HTTP service {}", builder.getConfiguration());

        rootRouter = Router.router(builder.getVertx());
        rootRouter.route().handler(BodyHandler.create());
        rootRouter.route().handler(CorsHandler.create(""));
        for (String address:builder.getHandlers().keySet()) {
            String rootPath = "";
            if (builder.getConfiguration().getRootPath() != null && !builder.getConfiguration().getRootPath().isEmpty()) {
                rootPath = builder.getConfiguration().getRootPath();
            }
            for (Handler<RoutingContext> handler:builder.getHandlers().get(address)) {
                rootRouter.route(rootPath + "/" + address).handler(handler);
            }
        }
        rootRouter.route().failureHandler(HttpServiceHandlers::failureHandler);

        if (builder.getConfiguration() == null || builder.getConfiguration().getEndpoint() == null) {
            throw new IllegalStateException("Invalid http service configuration");
        }

        HttpServerOptions serverOpts = new HttpServerOptions()
                .setPort(builder.getConfiguration().getEndpoint().getPort())
                .setHost(builder.getConfiguration().getEndpoint().getBindAddress());

        // TLS
        serverOpts.setSsl(builder.getConfiguration().getEndpoint().isSsl());
        serverOpts
                .removeEnabledSecureTransportProtocol("SSLv2Hello")
                .removeEnabledSecureTransportProtocol("TLSv1")
                .removeEnabledSecureTransportProtocol("TLSv1.1");

        if (builder.getConfiguration().getEndpoint().getKeyStorePath() != null) {
            serverOpts.setPfxKeyCertOptions(new PfxOptions()
                    .setPath(builder.getConfiguration().getEndpoint().getKeyStorePath())
                    .setPassword(builder.getConfiguration().getEndpoint().getKeyStorePassword()));
            logger.info("PFX KeyStore loaded: {}", builder.getConfiguration().getEndpoint().getKeyStorePath());
        }

        // Mutual Auth
        serverOpts.setClientAuth(builder.getConfiguration().getEndpoint().getClientAuth());
        if (builder.getConfiguration().getEndpoint().getTrustStorePath() != null) {
            serverOpts.setPfxTrustOptions(new PfxOptions()
                    .setPath(builder.getConfiguration().getEndpoint().getTrustStorePath())
                    .setPassword(builder.getConfiguration().getEndpoint().getTrustStorePassword()));
            logger.info("PFX TrustStore loaded: {}", builder.getConfiguration().getEndpoint().getTrustStorePath());
        }

        server = builder.getVertx().createHttpServer(serverOpts)
                .requestHandler(rootRouter)
                .listen(listenReq -> {
                    if (listenReq.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail((listenReq.cause()));
                    }
                });
    }

    @Override
    public void internalStop(Future<Void> stopFuture) throws Exception {
        Objects.requireNonNull(stopFuture, "param: stopFuture");

        logger.debug("Stopping HTTP service {}", builder.getConfiguration().getName());

        // Stop the deployment
        if (server != null) {
            server.close(closeReq -> {
                if (closeReq.succeeded()) {
                    logger.debug("HTTP service stopped {}", builder.getConfiguration().getName());
                    stopFuture.complete();
                } else {
                    logger.debug("Error stopping service {}: {}", builder.getConfiguration().getName(), closeReq.cause().getMessage());
                    stopFuture.fail(closeReq.cause());
                }
            });
            return;
        }
        stopFuture.handle(Future.succeededFuture());
        // TODO Fix this
//        if (container != null) {
//            Future<Void> localStopFuture = Future.future();
//            localStopFuture.setHandler(stopReq -> {
//                if (stopReq.succeeded()) {
//                    logger.info("HTTP service stopped, name {}, address {}, port {}, {}",
//                            builder.getConfig().getName(),
//                            builder.getConfig().getEndpoint().getBindAddress(),
//                            builder.getConfig().getEndpoint().getPort(),
//                            builder.getConfig().getEndpoint().isSsl() ? "Secure mode" : "Insecure mode");
//                    stopFuture.complete();
//                } else {
//                    logger.warn("Error stopping monitoring service, name {}: {}",
//                            builder.getConfig().getName(),
//                            stopReq.cause().getMessage(),
//                            stopReq.cause());
//                    stopFuture.fail(stopReq.cause());
//                }
//            });
//            container.stop(localStopFuture);
//            return;
//        }
//        stopFuture.complete();
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
