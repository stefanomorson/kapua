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
package org.eclipse.kapua.service.authorization.role.proxy.http;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.vertx.AsyncClientProviderImpl;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;

import io.vertx.ext.web.client.WebClient;

@KapuaProvider
public class RolePermissionServiceProxy implements RolePermissionService {

    @Inject
    private AsyncClientProviderImpl webClientProvider;

    private WebClient webClient;

    public RolePermissionServiceProxy() {
    }

    @Override
    public RolePermission create(RolePermissionCreator rolePermissionCreator) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RolePermission find(KapuaId scopeId, KapuaId rolePermissionId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RolePermissionListResult findByRoleId(KapuaId scopeId, KapuaId roleId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RolePermissionListResult query(KapuaQuery<RolePermission> query) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count(KapuaQuery<RolePermission> query) throws KapuaException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId rolePermissionId) throws KapuaException {
        // TODO Auto-generated method stub
    }

    private WebClient getWebClient() {
        if (webClient == null) {
            webClient = webClientProvider.create();
        }
        return webClient;
    }
}
