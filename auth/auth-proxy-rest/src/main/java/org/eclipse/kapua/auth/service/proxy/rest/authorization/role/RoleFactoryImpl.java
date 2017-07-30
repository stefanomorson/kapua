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
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RoleQuery;


@KapuaProvider
public class RoleFactoryImpl implements RoleFactory {

    @Override
    public Role newEntity(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RoleCreator newCreator(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RoleQuery newQuery(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RoleListResult newListResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RolePermission newRolePermission() {
        // TODO Auto-generated method stub
        return null;
    }

}
