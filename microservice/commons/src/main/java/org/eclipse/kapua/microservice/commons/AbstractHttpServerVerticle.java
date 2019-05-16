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
package org.eclipse.kapua.microservice.commons;

import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;

public abstract class AbstractHttpServerVerticle extends AbstractVerticle {

    private final KapuaLocator kapuaLocator = KapuaLocator.getInstance();
    private final CredentialsFactory credentialsFactory = kapuaLocator.getFactory(CredentialsFactory.class);
    private final AuthenticationService authenticationService = kapuaLocator.getService(AuthenticationService.class);

    protected abstract List<HttpEndpoint> getHttpEndpoint();

    protected HttpServerOptions getHttpServerOptions() {
        return new HttpServerOptions();
    }

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        // Login
        router.route().blockingHandler(ctx -> {
            String accessToken = StringUtils.removeStart(ctx.request().getHeader("Authorization"), "Bearer ");
            AccessTokenCredentials accessTokenCredentials = credentialsFactory.newAccessTokenCredentials(accessToken);
            try {
                authenticationService.authenticate(accessTokenCredentials);
            } catch (KapuaException ex) {
                ctx.fail(ex);
            }
            ctx.put("kapuaSession", KapuaSecurityUtils.getSession());
            ctx.put("shiroSubject", SecurityUtils.getSubject());
            ctx.next();
        });

        // TODO Put Service Event

        // Register child routes
        getHttpEndpoint().forEach(endpoint -> endpoint.registerRoutes(router));

        // Error handler
        router.route().failureHandler(ctx -> {
            JsonObject error = new JsonObject();
            error.put("error", ctx.failure().getMessage());
            ctx.response().setStatusCode(ctx.statusCode() == -1 ? 500 : ctx.statusCode()).end(error.encode());
        });

        vertx.createHttpServer(getHttpServerOptions()).requestHandler(router).listen();
    }

}