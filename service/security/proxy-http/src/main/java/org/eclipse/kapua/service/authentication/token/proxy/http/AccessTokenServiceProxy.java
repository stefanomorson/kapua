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
package org.eclipse.kapua.service.authentication.token.proxy.http;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.vertx.AsyncClientProviderImpl;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;
import org.eclipse.kapua.service.authentication.token.AccessTokenListResult;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;

import io.vertx.ext.web.client.WebClient;

@KapuaProvider
public class AccessTokenServiceProxy implements AccessTokenService {

    @Inject
    private AsyncClientProviderImpl webClientProvider;

    private WebClient webClient;

    public AccessTokenServiceProxy() {
    }

    @Override
    public AccessToken create(AccessTokenCreator creator) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessToken find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KapuaListResult<AccessToken> query(KapuaQuery<AccessToken> query) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count(KapuaQuery<AccessToken> query) throws KapuaException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        // TODO Auto-generated method stub
    }

    @Override
    public AccessToken update(AccessToken entity) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessTokenListResult findByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessToken findByTokenId(String tokenId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void invalidate(KapuaId scopeId, KapuaId id) throws KapuaException {
        // TODO Auto-generated method stub
    }

    private WebClient getWebClient() {
        if (webClient == null) {
            webClient = webClientProvider.create();
        }
        return webClient;
    }
}
