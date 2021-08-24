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
package org.eclipse.kapua.service.account.server;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.kapua.commons.core.vertx.EventBusServiceConfig;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authorization.AuthorizationService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class EventBusAccountServiceVerticle extends AbstractVerticle {

    @Inject
    @Named("kapua.accountService.eventBusServer.defaultAddress")
    private String ebAddress;

    @Inject
    @Named("kapua.accountService.eventBusServer.healthAddress")
    private String healthCheckEBAddress;

    private EventBusAccountService eventbusAuthService;

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
            eventbusAuthService = EventBusAccountService.create(vertx, config);

            AuthorizationService authService = KapuaLocator.getInstance().getService(AuthorizationService.class);
            AccountResource accountResource = new EventBusAccountResource(authService);
            EventBusAccountAdapter adapter = new EventBusAccountAdapter(accountResource);
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
