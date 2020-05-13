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

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class HttpProcessorImpl implements HttpProcessor {

    //private static Logger logger = LoggerFactory.getLogger(HttpProcessorImpl.class);

    private Vertx vertx;
    private HttpConfiguration configuration;
    private Router router;

    public HttpProcessorImpl(Vertx aVertx) {
        this.vertx = aVertx;
    }

    @Override
    public HttpProcessor configuration(HttpConfiguration aConfiguration) {
        this.configuration = aConfiguration;
        return this;
    }

    @Override
    public void process(RoutingContext context) {
        router.handleContext(context);
    }

    @Override
    public void handle(RoutingContext context) {
        process(context);
    }

    @Override
    public HttpConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void start(@NotNull Future<Void> startFuture) throws Exception {
        router = Router.router(vertx);
        registerRoutes(router);
        startFuture.complete();
    }

    private void registerRoutes(@NotNull Router aRouter) {
        Objects.requireNonNull(aRouter, "param: router");

        for (HttpController controller : configuration.getControllers()) {
            controller.registerRoutes(aRouter);
        }
    }
}
