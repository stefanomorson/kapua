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

import org.eclipse.kapua.commons.core.vertx.EventBusServerRequest;
import org.eclipse.kapua.commons.core.vertx.EventBusServerResponse;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface AccountResource {

    public void findById(EventBusServerRequest findByIdRequest, Handler<AsyncResult<EventBusServerResponse>> response);

    public void findByName(EventBusServerRequest findByNameRequest, Handler<AsyncResult<EventBusServerResponse>> response);

    public void queryAll(EventBusServerRequest queryAllRequest, Handler<AsyncResult<EventBusServerResponse>> response);

    public void countAll(EventBusServerRequest countAllRequest, Handler<AsyncResult<EventBusServerResponse>> response);

    public void create(EventBusServerRequest createRequest, Handler<AsyncResult<EventBusServerResponse>> response);

    public void update(EventBusServerRequest updateRequest, Handler<AsyncResult<EventBusServerResponse>> response);

    public void delete(EventBusServerRequest deleteRequest, Handler<AsyncResult<EventBusServerResponse>> response);

    public void find(EventBusServerRequest findRequest, Handler<AsyncResult<EventBusServerResponse>> response);

    public void query(EventBusServerRequest queryRequest, Handler<AsyncResult<EventBusServerResponse>> response);

    public void count(EventBusServerRequest countRequest, Handler<AsyncResult<EventBusServerResponse>> response);
}
