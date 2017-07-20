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
package org.eclipse.kapua.auth.service.client.proxy;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.SessionCredentials;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authorization.access.AccessInfo;

/**
 * {@link AccessInfo} REST API resource.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class AuthenticationServiceProxy implements AuthenticationService {

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
    public boolean verifyCredentials(LoginCredentials loginCredentials) throws KapuaException {
        // TODO Auto-generated method stub
        return false;
    }
}
