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
package org.eclipse.kapua.service.authentication.client;

import org.eclipse.kapua.service.authentication.JwtCredentials;

public class JwtCredentialsImpl implements JwtCredentials {

    private String jwt;

    public JwtCredentialsImpl(String jwt) {
        setJwt(jwt);
    }

    @Override
    public String getJwt() {
        return jwt;
    }

    @Override
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
