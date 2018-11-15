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

import java.util.Objects;

import io.vertx.core.json.JsonObject;

/**
 * An event bus request used to trigger an event bus request/response
 * interaction.
 * <p>
 * The action is a string in dotted notation (e.g. "service.method")
 *
 */
public class EventBusServerRequest {

    private JsonObject request;

    public static EventBusServerRequest create(String action, JsonObject body) {
        EventBusServerRequest req = new EventBusServerRequest();
        req.setAction(action);
        if (body != null) {
            req.setBody(body);
        }
        return req;
    }

    public static EventBusServerRequest create(JsonObject request) {
        Objects.requireNonNull(request, "Invalid null request");
        EventBusServerRequest req = new EventBusServerRequest();
        // Action
        if (!request.containsKey(EventBusMessageConstants.ACTION)) {
            throw new RuntimeException("Request Action is mandatory");
        } else if (!(request.getValue(EventBusMessageConstants.ACTION) instanceof String)){
            throw new RuntimeException("Request Action must be a string");
        } else {
            req.setAction(request.getString(EventBusMessageConstants.ACTION));
        }
        // Authorization
        if (request.containsKey(EventBusMessageConstants.AUTHORIZATION) 
                && !(request.getValue(EventBusMessageConstants.AUTHORIZATION) instanceof String)) {
            throw new RuntimeException("Request Action is not a string");
        }
        if (request.containsKey(EventBusMessageConstants.AUTHORIZATION)) {
            req.setAuthorization(request.getString(EventBusMessageConstants.AUTHORIZATION));
        }
        // Body
        if (!request.containsKey(EventBusMessageConstants.BODY) 
                || request.getValue(EventBusMessageConstants.BODY) == null 
                || !(request.getValue(EventBusMessageConstants.BODY) instanceof JsonObject)) {
            throw new RuntimeException("Request Body is not a json object");
        }
        req.setBody((JsonObject) request.getValue(EventBusMessageConstants.BODY));
        return req;
    }

    public JsonObject asJsonObject() {
        return request;
    }

    public boolean hasAuthorization() {
        return request.containsKey(EventBusMessageConstants.AUTHORIZATION);
    }

    public String getAuthorization() {
        return request.getString(EventBusMessageConstants.AUTHORIZATION);
    }

    public EventBusServerRequest setAuthorization(String authorization) {
        request.put(EventBusMessageConstants.AUTHORIZATION, authorization);
        return this;
    }

    public String getAction() {
        return request.getString(EventBusMessageConstants.ACTION);
    }

    public EventBusServerRequest setAction(String action) {
        request.put(EventBusMessageConstants.ACTION, action);
        return this;
    }

    public JsonObject getBody() {
        return request.getJsonObject(EventBusMessageConstants.BODY);
    }

    public EventBusServerRequest setBody(JsonObject body) {
        request.put(EventBusMessageConstants.BODY, body);
        return this;
    }

    public boolean hasBody() {
        return (this.getBody() == null);
    }
}
