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

import io.vertx.core.json.JsonObject;

public class EventBusClientResponse {

    private JsonObject response;

    public EventBusClientResponse(int statusCode) {
        this(statusCode, (String)null);
    }

    public EventBusClientResponse(int statusCode, String statusMessage) {
        response = new JsonObject();
        setStatusCode(statusCode);
        if (statusMessage != null) {
            setStatusMessage(statusMessage);
        }
    }

    public EventBusClientResponse(int statusCode, JsonObject body) {
        response = new JsonObject();
        setStatusCode(statusCode);
        setBody(body);
    }

    public EventBusClientResponse(JsonObject response) {
        this.response = response;
    }

    public JsonObject asJsonObject() {
        return response;
    }

    public Integer getStatusCode() {
        return response.getInteger(EventBusMessageConstants.STATUS_CODE);
    }

    public void setStatusCode(Integer statusCode) {
        response.put(EventBusMessageConstants.STATUS_CODE, statusCode);
    }

    public void setStatusMessage(String statusMessage) {
        response.put(EventBusMessageConstants.STATUS_MSG, statusMessage);
    }

    public String getStatusMessage() {
        return response.getString(EventBusMessageConstants.STATUS_MSG);
    }

    public JsonObject getBody() {
        return response.getJsonObject(EventBusMessageConstants.BODY);
    }

    public void setBody(JsonObject body) {
        response.put(EventBusMessageConstants.BODY, body);
    }
}
