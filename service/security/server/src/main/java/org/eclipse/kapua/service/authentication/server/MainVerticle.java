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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.vertx.AbstractMainVerticle;
import org.eclipse.kapua.commons.core.vertx.EventBusClient;
import org.eclipse.kapua.commons.core.vertx.RestServiceVerticle;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;

public class MainVerticle extends AbstractMainVerticle {

    private final static Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    @Inject
    private JAXBContextProvider jaxbContextProvider;

    private KapuaServiceContext kapuaServiceCtx;

    @Inject
    private RestServiceVerticle httpAuthServiceVerticle;

    @Inject
    private EventBusAuthenticationServiceVerticle eventbusAuthServiceVerticle;

    @Inject
    @Named("kapua.authenticationService.eventBusServer.defaultAddress")
    private String eventBusAddress;

    private EventBusClient eventBusClient;

    @Override
    protected void internalStart(Future<Void> startFuture) throws Exception {
        logger.info("Starting Authentication Service...");
        Future.succeededFuture()
        .compose(map-> {
            Future<Void> future = Future.future();
            try {
                super.internalStart(future);
            } catch (Exception e) {
                future.fail(e);
            }
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();
            vertx.executeBlocking(fut -> {
                try {

                    Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
                    SecurityManager securityManager = factory.getInstance();
                    SecurityUtils.setSecurityManager(securityManager);
                    kapuaServiceCtx = KapuaServiceContext.create(SystemSetting.getInstance(), jaxbContextProvider);
                    fut.complete();
               } catch (KapuaException e) {
                   fut.fail(e);
               }
            }, ar -> {
                if (ar.succeeded()) {
                    future.complete();
                }
                else {
                    future.fail(ar.cause());
                }
            });
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();
            vertx.deployVerticle(eventbusAuthServiceVerticle, ar -> {
                if (ar.succeeded()) {
                    future.complete();
                }
                else {
                    future.fail(ar.cause());
                }
            });
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();
            eventBusClient = EventBusClient.create(vertx.eventBus());
            RestAuthenticationAdapter adapter = new RestAuthenticationAdapter(eventBusClient, eventBusAddress);
            httpAuthServiceVerticle.register(adapter);
            vertx.deployVerticle(httpAuthServiceVerticle, ar -> {
                if (ar.succeeded()) {
                    future.complete();
                }
                else {
                    future.fail(ar.cause());
                }
            });
            return future;
        })
        .setHandler(result -> {
            if (result.succeeded()) {
                logger.info("Starting Authentication Service...DONE");
                startFuture.complete();
            } else {
                logger.error("Starting Authentication Service...FAILED", result.cause());
                startFuture.fail(result.cause());
            }
        });
    }

    @Override
    public void internalStop(Future<Void> closeFuture) {
        logger.info("Closing Authentication Service...");
        Future.succeededFuture()
        .compose(map -> {
            Future<Void> future = Future.future();
            vertx.executeBlocking(fut -> {
                try {
                    if (kapuaServiceCtx != null) {
                        kapuaServiceCtx.close();
                        kapuaServiceCtx = null;
                    }
                    fut.complete();
                } catch (KapuaException e) {
                    fut.fail(e);
                }
            }, ar -> {
                if (ar.succeeded()) {
                    future.complete();;
                }
                else {
                    future.fail(ar.cause());
                }
            });
            return future;
        })
        .compose(map -> {
            Future<Void> future = Future.future();
            try {
                super.internalStop(future);
            } catch (Exception e) {
                future.fail(e);
            }
            return future;
        })
        .setHandler(ar -> {
            if (ar.succeeded()) {
                logger.info("Closing Authentication Service...DONE");
                closeFuture.handle(Future.succeededFuture());
            }
            else {
                logger.info("Closing Authentication Service...FAILED", ar.cause());
                closeFuture.handle(Future.failedFuture(ar.cause()));
            }
        });
    }
}
