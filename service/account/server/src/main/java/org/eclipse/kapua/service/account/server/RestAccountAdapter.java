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

import org.eclipse.kapua.commons.core.vertx.EventBusClient;
import org.eclipse.kapua.commons.core.vertx.EventBusClientRequest;
import org.eclipse.kapua.commons.core.vertx.EventBusMessageConstants;
import org.eclipse.kapua.commons.core.vertx.HttpServiceAdapter;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class RestAccountAdapter implements HttpServiceAdapter {

    private EventBusClient eventBusClient;
    private String address;

    public RestAccountAdapter(EventBusClient anEventBusClient, String anAddress) {
       eventBusClient = anEventBusClient;
       address = anAddress;
    }

    @Override
    public void register(Router router) {
        router.get("/accounts/:accountId").handler(this::findById);
        router.get("/accounts").handler(this::findByName);
        router.post("/accounts/_query").handler(this::queryAll);
        router.post("/accounts/_count").handler(this::countAll);
        router.post("/accounts/:scopedId/child-accounts").handler(this::create);
        router.put("/accounts/:scopedId/child-accounts/:accountId").handler(this::update);
        router.delete("/accounts/:scopedId/child-accounts/:accountId").handler(this::delete);
        router.get("/accounts/:scopedId/child-accounts/:accountId").handler(this::find);
        router.post("/accounts/:scopedId/child-accounts/_query").handler(this::query);
        router.post("/accounts/:scopedId/child-accounts/_count").handler(this::count);
    }

    private void findById(RoutingContext context) {
        String accountId = context.request().getParam(AccountRequestConstants.ACTION_PARAM_ENTITY_ID);
        if (accountId == null || accountId.isEmpty()) {
            context.response().setStatusCode(400).end();
            return;
        }
        eventBusClient
           .getRequest(address, AccountRequestConstants.ACTION_FIND_BY_ID)
           .addHeader(
                   EventBusMessageConstants.AUTHORIZATION, 
                   context.request().headers().get(EventBusMessageConstants.AUTHORIZATION))
           .body(new JsonObject().put(AccountRequestConstants.ACTION_PARAM_ENTITY_ID, accountId))
           .send(response -> {
               context.response().end(response.result().getBody().toBuffer());
           });
    }

    private void findByName(RoutingContext context) {
        if (!context.request().params().contains(AccountRequestConstants.ACTION_PARAM_NAME)) {
            context.response().setStatusCode(400).end();
            return;
        }
        String name = context.request().getParam(AccountRequestConstants.ACTION_PARAM_NAME);
        if (name == null || name.isEmpty()) {
            context.response().setStatusCode(400).end();
            return;
        }
        eventBusClient
           .getRequest(address, AccountRequestConstants.ACTION_FIND_BY_NAME)
           .addHeader(
                   EventBusMessageConstants.AUTHORIZATION, 
                   context.request().headers().get(EventBusMessageConstants.AUTHORIZATION))
           .body(new JsonObject().put(AccountRequestConstants.ACTION_PARAM_NAME, name))
           .send(response -> {
               if (response.succeeded()) {
                   context.response().setStatusCode(response.result().getStatusCode()).end();
               } else {
                   response.cause().printStackTrace();
               }
           });
    }

    private void queryAll(RoutingContext context) {
        context.request().bodyHandler(body -> {
            eventBusClient
                .getRequest(address, AccountRequestConstants.ACTION_QUERY_ALL)
                .addHeader(
                        EventBusMessageConstants.AUTHORIZATION, 
                        context.request().headers().get(EventBusMessageConstants.AUTHORIZATION))
                .body(new JsonObject(body))
                .send(response -> {
                    context.response().end(response.result().getBody().toBuffer());
                });
        });
    }

    private void countAll(RoutingContext context) {
        context.request().bodyHandler(body -> {
            eventBusClient
                .getRequest(address, AccountRequestConstants.ACTION_COUNT_ALL)
                .addHeader(
                        EventBusMessageConstants.AUTHORIZATION, 
                        context.request().headers().get(EventBusMessageConstants.AUTHORIZATION))
                .body(new JsonObject(body))
                .send(response -> {
                    context.response().end(response.result().getBody().toBuffer());                    
                });
        });
    }

    private void find(RoutingContext context) {
        if (!context.request().params().contains(AccountRequestConstants.ACTION_PARAM_SCOPE_ID)) {
            context.response().setStatusCode(400).end();
            return;
        }
        String scopeId = context.request().getParam(AccountRequestConstants.ACTION_PARAM_SCOPE_ID);
        if (!context.request().params().contains(AccountRequestConstants.ACTION_PARAM_ENTITY_ID)) {
            context.response().setStatusCode(400).end();
            return;
        }
        String entityId = context.request().getParam(AccountRequestConstants.ACTION_PARAM_ENTITY_ID);
        if (entityId == null || entityId.isEmpty()) {
            context.response().setStatusCode(400).end();
            return;
        }
        eventBusClient
           .getRequest(address, AccountRequestConstants.ACTION_FIND_BY_NAME)
           .addHeader(
                   EventBusMessageConstants.AUTHORIZATION, 
                   context.request().headers().get(EventBusMessageConstants.AUTHORIZATION))
           .body(new JsonObject()
                   .put(AccountRequestConstants.ACTION_PARAM_SCOPE_ID, scopeId)
                   .put(AccountRequestConstants.ACTION_PARAM_ENTITY_ID, entityId))
           .send(response -> {
               context.response().end(response.result().getBody().toBuffer());
        });
    }

    private void create(RoutingContext context) {
        context.request().bodyHandler(body -> {
            EventBusClientRequest request = eventBusClient
                .getRequest(address, AccountRequestConstants.ACTION_CREATE)
                .addHeader(EventBusMessageConstants.ACTION, AccountRequestConstants.ACTION_CREATE);
            if (context.request().headers().contains(EventBusMessageConstants.AUTHORIZATION)) {
                request.addHeader(
                        EventBusMessageConstants.AUTHORIZATION, 
                        context.request().headers().get(EventBusMessageConstants.AUTHORIZATION));
            }
            request    
                .body(new JsonObject(body))
                .send(response -> {
                    context.response().end(response.result().getBody().toBuffer());                    
                });
        });
    }

    private void update(RoutingContext context) {
        context.request().bodyHandler(body -> {
            eventBusClient
                .getRequest(address, AccountRequestConstants.ACTION_UPDATE)
                .addHeader(
                        EventBusMessageConstants.AUTHORIZATION, 
                        context.request().headers().get(EventBusMessageConstants.AUTHORIZATION))
                .body(new JsonObject(body))
                .send(response -> {
                    context.response().end(response.result().getBody().toBuffer());                    
                });
        });
    }

    private void delete(RoutingContext context) {
        if (!context.request().params().contains(AccountRequestConstants.ACTION_PARAM_SCOPE_ID)) {
            context.response().setStatusCode(400).end();
            return;
        }
        String scopeId = context.request().getParam(AccountRequestConstants.ACTION_PARAM_SCOPE_ID);
        if (!context.request().params().contains(AccountRequestConstants.ACTION_PARAM_ENTITY_ID)) {
            context.response().setStatusCode(400).end();
            return;
        }
        String entityId = context.request().getParam(AccountRequestConstants.ACTION_PARAM_ENTITY_ID);
        if (entityId == null || entityId.isEmpty()) {
            context.response().setStatusCode(400).end();
            return;
        }
        eventBusClient
           .getRequest(address, AccountRequestConstants.ACTION_DELETE)
           .addHeader(
                   EventBusMessageConstants.AUTHORIZATION, 
                   context.request().headers().get(EventBusMessageConstants.AUTHORIZATION))
           .body(new JsonObject()
                   .put(AccountRequestConstants.ACTION_PARAM_SCOPE_ID, scopeId)
                   .put(AccountRequestConstants.ACTION_PARAM_ENTITY_ID, entityId))
           .send(response -> {
               context.response().end(response.result().getBody().toBuffer());
           });
    }

    private void query(RoutingContext context) {
        context.request().bodyHandler(body -> {
            eventBusClient
                .getRequest(address, AccountRequestConstants.ACTION_QUERY)
                .addHeader(
                        EventBusMessageConstants.AUTHORIZATION, 
                        context.request().headers().get(EventBusMessageConstants.AUTHORIZATION))
                .body(new JsonObject(body))
                .send(response -> {
                    context.response().end(response.result().getBody().toBuffer());                    
                });
        });
    }

    private void count(RoutingContext context) {
        context.request().bodyHandler(body -> {
            eventBusClient
                .getRequest(address, AccountRequestConstants.ACTION_COUNT)
                .addHeader(
                        EventBusMessageConstants.AUTHORIZATION, 
                        context.request().headers().get(EventBusMessageConstants.AUTHORIZATION))
                .body(new JsonObject(body))
                .send(response -> {
                    context.response().end(response.result().getBody().toBuffer());                    
                });
        });
    }
}
