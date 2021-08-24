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

import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.vertx.EventBusMessageConstants;
import org.eclipse.kapua.commons.core.vertx.EventBusMessageDispatcher;
import org.eclipse.kapua.commons.core.vertx.EventBusServerRequest;
import org.eclipse.kapua.commons.core.vertx.EventBusServerResponse;
import org.eclipse.kapua.commons.core.vertx.EventBusServiceAdapter;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.SessionCredentials;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public class EventBusAuthenticationAdapter implements EventBusServiceAdapter {

    private final static Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    private AuthenticationService authenticationService;

    public EventBusAuthenticationAdapter(AuthenticationService anAuthenticationService) {
        authenticationService = anAuthenticationService;
    }

    @Override
    public void register(EventBusMessageDispatcher dispatcher) {
        dispatcher.registerBlockingHandler(AuthenticationRequestConstants.ACTION_LOGIN, this::login);
        dispatcher.registerBlockingHandler(AuthenticationRequestConstants.ACTION_AUTHENTICATE, this::authenticate);
        dispatcher.registerBlockingHandler(AuthenticationRequestConstants.ACTION_LOGOUT, this::logout);
        dispatcher.registerBlockingHandler(AuthenticationRequestConstants.ACTION_FIND_ACCESS_TOKEN, this::findAccessToken);
        dispatcher.registerBlockingHandler(AuthenticationRequestConstants.ACTION_REFRESH_ACCESS_TOKEN, this::refreshAccessToken);
        dispatcher.registerBlockingHandler(AuthenticationRequestConstants.ACTION_VERIFY_CREDENTIALS, this::verifyCredentials);
    }

    private void login(EventBusServerRequest loginRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
        try {
            LoginCredentials credentials = XmlUtil.unmarshalJson(loginRequest.getBody().encode(), UsernamePasswordCredentials.class, null);
            AccessToken token = authenticationService.login(credentials);
            String tokenString = XmlUtil.marshalJson(token);
            EventBusServerResponse.create(EventBusMessageConstants.STATUS_OK).setBody(new JsonObject(tokenString));
        } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException | KapuaException e) {
            EventBusServerResponse.create(500).setResultCodeMessage(e.getMessage());
            logger.error("Failed to login!", e);
        }
    }

    private void authenticate(EventBusServerRequest authenticateRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
        try {
            SessionCredentials credentials;
            credentials = XmlUtil.unmarshalJson(authenticateRequest.getBody().encode(), AccessTokenCredentials.class, null);
            authenticationService.authenticate(credentials);
            EventBusServerResponse.create(EventBusMessageConstants.STATUS_OK);
        } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException | KapuaException e) {
            EventBusServerResponse.create(500).setResultCodeMessage(e.getMessage());
            logger.error("Failed to authenticate!", e);
        }
    }

    private void logout(EventBusServerRequest logoutRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
        try {
            SessionCredentials credentials;
            credentials = XmlUtil.unmarshalJson(logoutRequest.getBody().encode(), AccessTokenCredentials.class, null);
            authenticationService.authenticate(credentials);
            handler.handle(Future.succeededFuture(EventBusServerResponse.create(EventBusMessageConstants.STATUS_OK)));
        } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException | KapuaException e) {
            handler.handle(Future.succeededFuture(EventBusServerResponse.create(500).setResultCodeMessage(e.getMessage())));
            logger.error("Failed to authenticate!", e);
        }
    }

    private void findAccessToken(EventBusServerRequest findRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
    }

    private void refreshAccessToken(EventBusServerRequest refreshRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
    }

    private void verifyCredentials(EventBusServerRequest verifyRequest, Handler<AsyncResult<EventBusServerResponse>> handler) {
        try {
            LoginCredentials loginCredentials;
            loginCredentials = XmlUtil.unmarshal(verifyRequest.getBody().encode(), LoginCredentials.class);
            authenticationService.verifyCredentials(loginCredentials);
            handler.handle(Future.succeededFuture(EventBusServerResponse.create(EventBusMessageConstants.STATUS_OK)));
        } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException | KapuaException e) {
            handler.handle(Future.succeededFuture(EventBusServerResponse.create(500).setResultCodeMessage(e.getMessage())));
            logger.error("Failed to verify credentials!", e);
        }
    }
}
