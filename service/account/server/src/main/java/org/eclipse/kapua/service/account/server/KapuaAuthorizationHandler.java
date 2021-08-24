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

import java.util.Objects;

import org.eclipse.kapua.commons.core.vertx.EventBusMessageConstants;
import org.eclipse.kapua.commons.core.vertx.EventBusMessageHandler;
import org.eclipse.kapua.commons.core.vertx.EventBusServerRequest;
import org.eclipse.kapua.commons.core.vertx.EventBusServerResponse;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public class KapuaAuthorizationHandler implements EventBusMessageHandler {

    private EventBusMessageHandler handler;

    public KapuaAuthorizationHandler(EventBusMessageHandler anHandler) {
        handler = anHandler;
    }

    @Override
    public void handle(EventBusServerRequest request, Handler<AsyncResult<EventBusServerResponse>> response) {
        Objects.requireNonNull(request, "Context must be not null");
        Objects.requireNonNull(request.getBody(), "Context request body must be not null");

        try {
            if (request.getHeaders().contains(EventBusMessageConstants.AUTHORIZATION)) {
                // Set security session
            }

            if (handler != null) {
                handler.handle(request, response);
            }
        } finally {
            // Unset security session
        }
    }
}
