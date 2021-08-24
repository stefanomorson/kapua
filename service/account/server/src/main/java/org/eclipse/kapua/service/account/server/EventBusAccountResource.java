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

import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.vertx.EventBusServerRequest;
import org.eclipse.kapua.commons.core.vertx.EventBusServerResponse;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class EventBusAccountResource implements AccountResource {

    private final static Logger logger = LoggerFactory.getLogger(EventBusAccountResource.class);

//    private AuthorizationService authorizationService;
//    private PermissionFactory permissionFactory;
    private AccountService accountService = KapuaLocator.getInstance().getService(AccountService.class);

    public EventBusAccountResource(AuthorizationService anAuthorizationService) {
        //authorizationService = anAuthorizationService;
    }

    @Override
    public void findById(EventBusServerRequest findByIdRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
    }

    @Override
    public void findByName(EventBusServerRequest findByNameRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
        handler.handle(Future.succeededFuture(EventBusServerResponse.create(200)));
    }

    @Override
    public void queryAll(EventBusServerRequest queryAllRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
    }

    @Override
    public void countAll(EventBusServerRequest countAllRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
    }

    @Override
    public void create(EventBusServerRequest createRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
//        Permission permission = permissionFactory.newPermission(null, null, null);
        try {
//            authorizationService.checkPermission(permission);
            try {
                String str = createRequest.getBody().toString();
                AccountCreator accountCreator = XmlUtil.unmarshalJson(str, AccountCreator.class, null);
                accountService.create(accountCreator);
            } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (KapuaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void update(EventBusServerRequest updateRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
    }

    @Override
    public void delete(EventBusServerRequest deleteRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
    }

    @Override
    public void find(EventBusServerRequest findRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
    }

    @Override
    public void query(EventBusServerRequest queryRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
    }

    @Override
    public void count(EventBusServerRequest countRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
    }
}
