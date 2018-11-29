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

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;

/**
 * An event bus request used to trigger an event bus request/response
 * interaction.
 * <p>
 * The action is a string in dotted notation (e.g. "service.method")
 *
 */
public class EventBusServerRequest {

    private MultiMap headers;
    private JsonObject request;

    public static EventBusServerRequest create(MultiMap headers, JsonObject request) {
        Objects.requireNonNull(headers, "Invalid null headers");
        Objects.requireNonNull(request, "Invalid null request");
        EventBusServerRequest req = new EventBusServerRequest();
        // Check Action
        if (!headers.contains(EventBusMessageConstants.ACTION)) {
            throw new RuntimeException("Request Action is mandatory");
        }
        // Check Authorization
        if (request.containsKey(EventBusMessageConstants.AUTHORIZATION)) {
            throw new RuntimeException("Request Action is not a string");
        }   

        headers.forEach(entry -> {
            req.getHeaders().add(entry.getKey(), entry.getValue());
        });

        // Body
        if (!request.containsKey(EventBusMessageConstants.BODY) 
                || request.getValue(EventBusMessageConstants.BODY) == null 
                || !(request.getValue(EventBusMessageConstants.BODY) instanceof JsonObject)) {
            throw new RuntimeException("Request Body is not a json object");
        }
        req.setBody((JsonObject) request.getValue(EventBusMessageConstants.BODY));
        return req;
    }

    public MultiMap getHeaders() {
        if (headers == null) {
            headers = MultiMap.caseInsensitiveMultiMap();
        }
        return headers;
    }

    public String getProperty(String name) {
        if (Objects.requireNonNull(name).equals(EventBusMessageConstants.BODY)) {
            throw new IllegalArgumentException(String.format("Invalid property name %s", name));
        }
        return getRequest().getString(name);
    }

    public JsonObject getBody() {
        return getRequest().getJsonObject(EventBusMessageConstants.BODY);
    }

    public EventBusServerRequest setBody(JsonObject body) {
        getRequest().put(EventBusMessageConstants.BODY, body);
        return this;
    }

    public boolean hasBody() {
        return (this.getBody() == null);
    }

    private JsonObject getRequest() {
        if (request == null) {
            request = new JsonObject();
        }
        return request;
    }
}
