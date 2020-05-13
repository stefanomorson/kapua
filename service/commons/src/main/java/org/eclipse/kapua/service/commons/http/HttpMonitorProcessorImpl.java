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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.eclipse.kapua.service.commons.HealthCheckProvider;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

class HttpMonitorProcessorImpl implements HttpMonitorProcessor {

    private Vertx vertx;
    private HttpMonitorConfiguration config;

    public HttpMonitorProcessorImpl(Vertx aVertx) {
        this(aVertx, new HttpMonitorConfiguration());
    }

    public HttpMonitorProcessorImpl(Vertx aVertx, HttpMonitorConfiguration config) {
        this.vertx = aVertx;
        this.config = config;
    }

    public void start(Future<Void> startFuture) throws Exception {
        Objects.requireNonNull(startFuture, "param: startFuture");

        Set<HttpController> controllers = new HashSet<>();

        // Add health checks controllers
        if (config.getHealthCheckEnable()) {
            HealthCheckHandler livenessHandler = HealthCheckHandler.create(vertx);
            HealthCheckHandler readynessHandler = HealthCheckHandler.create(vertx);
            registerCheckers(livenessHandler, readynessHandler);

            HealthCheckController healthCheckController = HealthCheckController.create(livenessHandler, readynessHandler);
            controllers.add(healthCheckController);
        }

        // Configure Metrics
        if (config.getMetricsEnable()) {
            MetricsController meter = MetricsController.create(MetricsService.create(vertx));
            controllers.add(meter);
        }

        HttpProcessor processor = HttpProcessor.create(vertx);
        processor.configuration(new HttpConfiguration()
                .registerControllers(controllers));
    }

    @Override
    public HttpMonitorProcessorImpl configuration(HttpMonitorConfiguration configuration) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void process(RoutingContext aCtx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handle(RoutingContext event) {
        // TODO Auto-generated method stub

    }

    @Override
    public HttpMonitorConfiguration getConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

    private void registerCheckers(HealthCheckHandler livenessHandler, HealthCheckHandler readynessHandler) {
        for (HealthCheckProvider provider : config.getProviders()) {
            provider.registerLivenessCheckers(livenessHandler);
            provider.registerReadinessCheckers(readynessHandler);
        }
    }

    private static class HealthCheckController implements HttpController {

        private static final String HEALTHCHECK_LIVENESS_PATH = "/alive";
        private static final String HEALTHCHECK_READINESS_PATH = "/ready";

        private HealthCheckHandler livenessHandler;
        private HealthCheckHandler readinessHandler;

        private HealthCheckController(HealthCheckHandler livenessHandler, HealthCheckHandler readinessHandler) {
            this.livenessHandler = livenessHandler;
            this.readinessHandler = readinessHandler;
        }

        @Override
        public void registerRoutes(@NotNull Router router) {
            router.get(HEALTHCHECK_READINESS_PATH).handler(readinessHandler);
            router.get(HEALTHCHECK_LIVENESS_PATH).handler(livenessHandler);
        }

        public static HealthCheckController create(HealthCheckHandler livenessHandler, HealthCheckHandler readynessHandler) {
            return new HealthCheckController(livenessHandler, readynessHandler);
        }
    }

    private static class MetricsController implements HttpController {

        private static final String METRICS_PATH = "/metrics";
        private static final String METRICS_BASE_NAME_PARAM = "base";

        private MetricsService service;

        private MetricsController(MetricsService service) {
            this.service = service;
        }

        @Override
        public void registerRoutes(@NotNull Router router) {
            router.get(METRICS_PATH).handler(ctx -> {
                String baseName = "";
                if (ctx.request().params().contains(METRICS_BASE_NAME_PARAM)) {
                    baseName = ctx.request().getParam(METRICS_BASE_NAME_PARAM);
                }
                ctx.response().end(this.service.getMetricsSnapshot(baseName).toBuffer());
            });
        }

        public static MetricsController create(MetricsService service) {
            return new MetricsController(service);
        }
    }
}
