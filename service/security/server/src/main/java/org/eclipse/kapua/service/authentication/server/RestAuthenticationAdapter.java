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
package org.eclipse.kapua.service.authentication.server;

import org.eclipse.kapua.commons.core.vertx.EventBusClient;
import org.eclipse.kapua.commons.core.vertx.HttpServiceAdapter;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class RestAuthenticationAdapter implements HttpServiceAdapter {

    private EventBusClient eventbusClient;
    private String address;

    public RestAuthenticationAdapter(EventBusClient anEventBusClient, String anAddress) {
        eventbusClient = anEventBusClient;
        address = anAddress;
    }

    @Override
    public void register(Router router) {
        router.post("/authentication/login").handler(this::login);
        router.post("/authentication/authenticate").handler(this::authenticate);
        router.post("/authentication/logout").handler(this::logout);
        router.get("/authentication/findAccessToken").handler(this::findAccessToken);
        router.get("/authentication/refreshAccessToken").handler(this::refreshAccessToken);
        router.get("/authentication/verifyCredentials").handler(this::verifyCredentials);
    }

    private void processRequest(RoutingContext context, Handler<JsonObject> handler) {
        context.request().bodyHandler(buff -> {
            JsonObject creds = null;
            creds = new JsonObject(buff);
            handler.handle(creds);
        });
    }

    private void login(RoutingContext loginContext) {
        processRequest(loginContext, loginBody -> {
            eventbusClient
                .getRequest(address, AuthenticationRequestConstants.ACTION_LOGIN)
                .body(loginBody)
                .send(responseEvt -> {
                    if (responseEvt.succeeded()) {
                        loginContext.response().setStatusCode(responseEvt.result().getStatusCode()).end(responseEvt.result().getBody().toBuffer());
                    } else {
                        loginContext.response().setStatusCode(500).end();
                    }
                });
        });
    }

    private void authenticate(RoutingContext authenticateContext) {
        processRequest(authenticateContext, request -> {
            eventbusClient
                .getRequest(address, AuthenticationRequestConstants.ACTION_AUTHENTICATE)
                .send(responseEvt -> {
                    if (responseEvt.succeeded()) {
                        authenticateContext.response().setStatusCode(responseEvt.result().getStatusCode()).end();
                    } else {
                        authenticateContext.response().setStatusCode(500).end();
                    }
                });
        });
    }

    private void logout(RoutingContext logoutContext) {
    }

    private void findAccessToken(RoutingContext findContext) {
    }

    private void refreshAccessToken(RoutingContext refreshContext) {
    }

    private void verifyCredentials(RoutingContext verifyContext) {
    }
}
