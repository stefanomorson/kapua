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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class RestServiceVerticle extends AbstractVerticle {

    @Inject
    @Named("kapua.restService")
    RestServiceConfig restServiceConfig;

    private RestService restService;
    private List<HealthCheckAdapter> healthCheckAdapters;
    private List<HttpServiceAdapter> httpServiceAdapters;

    public RestServiceVerticle() {
        healthCheckAdapters = new ArrayList<>();
        httpServiceAdapters = new ArrayList<>();
    }

    public void register(HealthCheckAdapter adapter) {
        healthCheckAdapters.add(adapter);
    }

    public void register(HttpServiceAdapter adapter) {
        httpServiceAdapters.add(adapter);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Future.succeededFuture()
        .compose(map-> {
            Future<Void> future = Future.future();
            try {
                super.start(future);
            } catch (Exception e) {
                future.fail(e);
            }
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();
            HttpServiceConfig config = new HttpServiceConfig();
            config.setHost(restServiceConfig.getHost());
            config.setMetricsRoot(restServiceConfig.getMetricsRoot());
            config.setPort(restServiceConfig.getPort());
            restService = new RestService(vertx, config);
            restService.register(EventBusHealthCheckAdapter.create(vertx.eventBus()
                    , restServiceConfig.getEventbusHealthCheckName()
                    , restServiceConfig.getEventbusHealthCheckAddress()));
            for (HealthCheckAdapter adapter:healthCheckAdapters) {
                restService.register(adapter);
            }
            for (HttpServiceAdapter adapter:httpServiceAdapters) {
                restService.register(adapter);
            }
            try {
                restService.start(future);
            } catch (Exception e) {
                future.fail(e);
            }
            return future;
        })
        .setHandler(ar -> {
            if (ar.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(ar.cause());
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        Future.succeededFuture()
        .compose(map -> {
            Future<Void> future = Future.future();
            if (restService != null) {
                try {
                    restService.stop(future);
                } catch (Exception e) {
                    future.fail(e);
                }
            }
            else {
                future.complete();
            }
            return future;
        })
        .compose(map-> {
            Future<Void> future = Future.future();
            try {
                super.stop(future);
            } catch (Exception e) {
                future.fail(e);
            }
            return future;
        })
        .setHandler(ar -> {
            if (ar.succeeded()) {
                stopFuture.complete();
            } else {
                stopFuture.fail(ar.cause());
            }
        });
    }
}
