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

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.kapua.commons.core.vertx.EventBusServiceConfig;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authentication.AuthenticationService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class EventBusAuthenticationServiceVerticle extends AbstractVerticle {

    @Inject
    @Named("kapua.authenticationService.eventBusServer.defaultAddress")
    private String ebAddress;

    @Inject
    @Named("kapua.authenticationService.eventBusServer.healthAddress")
    private String healthCheckEBAddress;

    private EventBusAuthenticationService eventbusAuthService;

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

            EventBusServiceConfig config = new EventBusServiceConfig();
            config.setAddress(ebAddress);
            config.setHealthCheckAddress(healthCheckEBAddress);
            eventbusAuthService = EventBusAuthenticationService.create(vertx, config);

            AuthenticationService authService = KapuaLocator.getInstance().getService(AuthenticationService.class);
            EventBusAuthenticationAdapter adapter = new EventBusAuthenticationAdapter(authService);
            eventbusAuthService.register(adapter);
            try {
                eventbusAuthService.start(future);
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
            if (eventbusAuthService != null) {
                try {
                    eventbusAuthService.stop(future);
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
