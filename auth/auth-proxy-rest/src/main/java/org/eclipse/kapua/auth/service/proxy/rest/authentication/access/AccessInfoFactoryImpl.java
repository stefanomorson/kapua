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

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;

@KapuaProvider
public class AccessInfoFactoryImpl implements AccessInfoFactory {

    @Override
    public AccessInfo newEntity(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessInfoCreator newCreator(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessInfoQuery newQuery(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessInfoListResult newListResult() {
        // TODO Auto-generated method stub
        return null;
    }

}
