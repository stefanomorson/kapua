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
package org.eclipse.kapua.service.authorization.proxy.http;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.vertx.AsyncClientProviderImpl;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;

import io.vertx.ext.web.client.WebClient;

@KapuaProvider
public class AuthorizationServiceProxy implements AuthorizationService {

    @Inject
    private AsyncClientProviderImpl webClientProvider;

    private WebClient webClient;

    public AuthorizationServiceProxy() {
    }

    @Override
    public boolean isPermitted(Permission permission) throws KapuaException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void checkPermission(Permission permission) throws KapuaException {
        // TODO Auto-generated method stub
    }

    private WebClient getWebClient() {
        if (webClient == null) {
            webClient = webClientProvider.create();
        }
        return webClient;
    }
}
