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
package org.eclipse.kapua.commons.core.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.WebClient;

public class AsyncWebClient implements AsyncClient {

    private WebClient client;

    private AsyncWebClient(Vertx vertx) {
        client = WebClient.create(vertx);
    }

    public static AsyncWebClient create(Vertx vertx) {
        AsyncWebClient instance = new AsyncWebClient(vertx);
        return instance;
    }

    @Override
    public void send(Object object, Handler<AsyncResult<Buffer>> handler) {
        client.post("").send(sendHandler -> {
            if (sendHandler.succeeded()) {
                handler.handle(Future.succeededFuture(sendHandler.result().body()));
            } else {
                handler.handle(Future.failedFuture(sendHandler.cause()));
            }
        });
    }
}