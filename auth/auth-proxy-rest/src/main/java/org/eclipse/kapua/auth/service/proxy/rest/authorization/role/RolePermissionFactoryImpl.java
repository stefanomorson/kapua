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
package org.eclipse.kapua.auth.service.proxy.rest.authorization.role;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionQuery;

@KapuaProvider
public class RolePermissionFactoryImpl implements RolePermissionFactory {

    @Override
    public RolePermission newEntity(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RolePermissionCreator newCreator(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RolePermissionQuery newQuery(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RolePermissionListResult newListResult() {
        // TODO Auto-generated method stub
        return null;
    }

}
