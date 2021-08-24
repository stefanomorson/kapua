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
package org.eclipse.kapua.service.authorization.group.proxy.http;

import java.util.Map;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.vertx.AsyncClientProviderImpl;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupCreator;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.GroupService;

import io.vertx.ext.web.client.WebClient;

@KapuaProvider
public class GroupServiceProxy implements GroupService {

    @Inject
    private AsyncClientProviderImpl webClientProvider;

    private WebClient webClient;

    public GroupServiceProxy() {
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
    public Group create(GroupCreator groupCreator) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Group update(Group group) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Group find(KapuaId scopeId, KapuaId groupId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GroupListResult query(KapuaQuery<Group> query) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count(KapuaQuery<Group> query) throws KapuaException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId groupId) throws KapuaException {
        // TODO Auto-generated method stub
    }

    private WebClient getWebClient() {
        if (webClient == null) {
            webClient = webClientProvider.create();
        }
        return webClient;
    }
}
