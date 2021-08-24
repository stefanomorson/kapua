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
package org.eclipse.kapua.service.authentication.credential.proxy.http;

import java.util.Map;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.vertx.AsyncClientProviderImpl;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialService;

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.WebClient;

@KapuaProvider
public class CredentialServiceProxy implements CredentialService {

    @Inject
    private AsyncClientProviderImpl webClientProvider;

    private WebClient webClient;

    public CredentialServiceProxy() {
    }

    @Override
    public Credential create(CredentialCreator creator) throws KapuaException {
        try {
            String creatorString = XmlUtil.marshalJson(creator);
            webClient
            .post("")
            .sendBuffer(Buffer.buffer(creatorString), responseEvt -> {
            });;
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Credential find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count(KapuaQuery<Credential> query) throws KapuaException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        // TODO Auto-generated method stub
    }

    @Override
    public Credential update(Credential entity) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KapuaTocd getConfigMetadata(KapuaId scopeId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) throws KapuaException {
        // TODO Auto-generated method stub
    }

    @Override
    public CredentialListResult findByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Credential findByApiKey(String tokenApiKey) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CredentialListResult query(KapuaQuery<Credential> query) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void unlock(KapuaId scopeId, KapuaId credentialId) throws KapuaException {
        // TODO Auto-generated method stub
    }

    private WebClient getWebClient() {
        if (webClient == null) {
            webClient = webClientProvider.create();
        }
        return webClient;
    }
}
