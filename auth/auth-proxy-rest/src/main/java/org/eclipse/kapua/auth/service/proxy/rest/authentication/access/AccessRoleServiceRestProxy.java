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
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;

@KapuaProvider
public class AccessRoleServiceRestProxy implements AccessRoleService {

    @Override
    public AccessRole create(AccessRoleCreator accessRoleCreator) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessRole find(KapuaId scopeId, KapuaId accessRoleId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessRoleListResult findByAccessInfoId(KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessRoleListResult query(KapuaQuery<AccessRole> query) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count(KapuaQuery<AccessRole> query) throws KapuaException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId accessRoleId) throws KapuaException {
        // TODO Auto-generated method stub

    }

}
