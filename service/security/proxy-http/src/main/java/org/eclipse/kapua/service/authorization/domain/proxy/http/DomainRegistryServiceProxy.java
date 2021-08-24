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
package org.eclipse.kapua.service.authorization.domain.proxy.http;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.vertx.AsyncClientProviderImpl;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainRegistryService;

import io.vertx.ext.web.client.WebClient;

@KapuaProvider
public class DomainRegistryServiceProxy implements DomainRegistryService {

    @Inject
    private AsyncClientProviderImpl webClientProvider;

    private WebClient webClient;

    public DomainRegistryServiceProxy() {
    }

    @Override
    public Domain create(DomainCreator domainCreator) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Domain find(KapuaId scopeId, KapuaId domainId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DomainListResult query(KapuaQuery<Domain> query) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count(KapuaQuery<Domain> query) throws KapuaException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId roleId) throws KapuaException {
        // TODO Auto-generated method stub
    }

    private WebClient getWebClient() {
        if (webClient == null) {
            webClient = webClientProvider.create();
        }
        return webClient;
    }
}
