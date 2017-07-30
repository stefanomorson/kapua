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
package org.eclipse.kapua.auth.service.proxy.rest.authentication.credential;

import java.util.Date;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;


@KapuaProvider
public class CredentialFactoryImpl implements CredentialFactory {

    @Override
    public Credential newEntity(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CredentialCreator newCreator(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CredentialQuery newQuery(KapuaId scopeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CredentialListResult newListResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Credential newCredential(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey, CredentialStatus credentialStatus, Date expirationDate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CredentialCreator newCreator(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey, CredentialStatus credentialStatus, Date expirationDate) {
        // TODO Auto-generated method stub
        return null;
    }

}
