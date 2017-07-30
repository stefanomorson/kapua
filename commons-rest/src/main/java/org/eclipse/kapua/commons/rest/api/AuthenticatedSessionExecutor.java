/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.rest.api;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.SessionCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

public class AuthenticatedSessionExecutor {

    public static final Logger logger = LoggerFactory.getLogger(AuthenticatedSessionExecutor.class);

    private Vertx vertx;
    private MultivaluedMap<String, String> headers;
    private boolean sessionCreated;

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AuthenticationService authcService = locator.getService(AuthenticationService.class);
    private final CredentialsFactory credentialsFactory = locator.getFactory(CredentialsFactory.class);

    public AuthenticatedSessionExecutor(Vertx vertx, MultivaluedMap<String, String> headers) {
        this.vertx = vertx;
        this.headers = headers;
    }

    public void exec(final Handler<Future<Object>> blockingCodeHadler, boolean ordered, Handler<AsyncResult<Object>> resultHanlder) {

        vertx.executeBlocking(future -> {
            // Create security context
            sessionCreated = false;
            KapuaSession session;
            session = KapuaSecurityUtils.getSession();
            if (session == null) {
                try {
                    List<String> authHeader = headers.get("Authorization");
                    if (authHeader == null || authHeader.size() == 0) {
                        throw KapuaException.internalError("Invalid header.");
                    }
                    String jwtToken = authHeader.get(0);
                    if (jwtToken != null) {
                        jwtToken = jwtToken.replaceAll("Bearer ", "");
                    }
                    SessionCredentials credentials = credentialsFactory.newAccessTokenCredentials(jwtToken);
                    authcService.authenticate(credentials);
                } catch (KapuaException e) {
                    future.fail(e);
                    return;
                }
            } else {
                future.fail("A session is already open for user: " + session.getUserId());
                return;
            }

            blockingCodeHadler.handle(future);
        }, 
        ordered,
        res -> {
            //TODO Check that the clearSession is executed whatever the result is
            // regardless it succeeded, failed or an exception has been thrown
            if (sessionCreated) {
                KapuaSecurityUtils.clearSession();
            }
            resultHanlder.handle(res);
        });
    }

}
