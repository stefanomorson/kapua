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
package org.eclipse.kapua.service.authentication.proxy.http;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.vertx.AsyncClientProviderImpl;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.SessionCredentials;
import org.eclipse.kapua.service.authentication.token.AccessToken;

import io.vertx.ext.web.client.WebClient;

@KapuaProvider
public class AuthenticationServiceProxy implements AuthenticationService {

    @Inject
    private AsyncClientProviderImpl webClientProvider;

    private WebClient webClient;

    public AuthenticationServiceProxy() {
    }


    @Override
    public AccessToken login(LoginCredentials loginCredentials) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void authenticate(SessionCredentials sessionCredentials) throws KapuaException {
        // TODO Auto-generated method stub
    }


    @Override
    public void logout() throws KapuaException {
        // TODO Auto-generated method stub
    }


    @Override
    public AccessToken findAccessToken(String tokenId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public AccessToken refreshAccessToken(String tokenId, String refreshToken) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void verifyCredentials(LoginCredentials loginCredentials) throws KapuaException {
        // TODO Auto-generated method stub
    }

    private WebClient getWebClient() {
        if (webClient == null) {
            webClient = webClientProvider.create();
        }
        return webClient;
    }
}
