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

/**
 * An event bus response used to answer a matching event bus request
 * within an event bus request/response interaction.
 *
 */
public class EventBusServerResponse {

    private JsonObject response;

    public static EventBusServerResponse create(int resultCode, JsonObject body) {
        EventBusServerResponse res = new EventBusServerResponse();
        res.setResultCode(resultCode);
        if (body != null) {
            res.setBody(body);
        }
        return res;
    }

    public static EventBusServerResponse create(int resultCode) {
        EventBusServerResponse res = new EventBusServerResponse();
        res.setResultCode(resultCode);
        return res;
    }

    public static EventBusServerResponse create(Object object) {
        if (object == null || !(object instanceof JsonObject)) {
            return null;
        }
        JsonObject response = (JsonObject) object;
        if (!response.containsKey(EventBusMessageConstants.STATUS_CODE)) {
            return null;
        }
        int resultCode = response.getInteger(EventBusMessageConstants.STATUS_CODE);
        JsonObject body = null;
        if (response.containsKey(EventBusMessageConstants.BODY)) {
            body = response.getJsonObject(EventBusMessageConstants.BODY);
        }
        return EventBusServerResponse.create(resultCode, body);
    }

    public JsonObject asJsonObject() {
        return response;
    }

    public int getResultCode() {
        return response.getInteger(EventBusMessageConstants.STATUS_CODE);
    }

    public EventBusServerResponse setResultCode(int resultCode) {
        response.put(EventBusMessageConstants.STATUS_CODE, resultCode);
        return this;
    }

    public String getResultCodeMessage() {
        return response.getString(EventBusMessageConstants.STATUS_MSG);
    }

    public EventBusServerResponse setResultCodeMessage(String resultCodeMsg) {
        response.put(EventBusMessageConstants.STATUS_MSG, resultCodeMsg);
        return this;
    }

    public JsonObject getBody() {
        return response.getJsonObject(EventBusMessageConstants.BODY);
    }

    public EventBusServerResponse setBody(JsonObject body) {
        response.put(EventBusMessageConstants.BODY, body);
        return this;
    }

    public boolean hasBody() {
        return (this.getBody() == null);
    }
}
