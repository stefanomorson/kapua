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
package org.eclipse.kapua.auth.service.proxy.rest.authentication.access;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;

@KapuaProvider
public class AccessPermissionServiceRestProxy implements AccessPermissionService {

    @Override
    public AccessPermission create(AccessPermissionCreator accessPermissionCreator) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessPermission find(KapuaId scopeId, KapuaId accessPermissionId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessPermissionListResult findByAccessInfoId(KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessPermissionListResult query(KapuaQuery<AccessPermission> query) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count(KapuaQuery<AccessPermission> query) throws KapuaException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId accessPermissionId) throws KapuaException {
        // TODO Auto-generated method stub

    }

}
