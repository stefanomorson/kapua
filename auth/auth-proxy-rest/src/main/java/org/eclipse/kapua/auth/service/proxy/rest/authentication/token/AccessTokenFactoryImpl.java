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
package org.eclipse.kapua.auth.service.proxy.rest.authentication.token;

import java.util.Date;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;
import org.eclipse.kapua.service.authentication.token.AccessTokenFactory;
import org.eclipse.kapua.service.authentication.token.AccessTokenListResult;
import org.eclipse.kapua.service.authentication.token.AccessTokenQuery;

@KapuaProvider
public class AccessTokenFactoryImpl implements AccessTokenFactory {

    @Override
    public AccessToken newEntity(KapuaId scopeId) {
        return new AccessTokenImpl(scopeId);
    }

    @Override
    public AccessTokenCreator newCreator(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessTokenQuery newQuery(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessTokenListResult newListResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessTokenCreator newCreator(KapuaId scopeId, KapuaId userId, String tokenId, Date expiresOn, String refreshToken, Date refreshExpiresOn) {
        // TODO Auto-generated method stub
        return null;
    }

}
