/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationCredentials;

/**
 * Access token {@link AuthenticationCredentials} implementation.
 * 
 * @since 1.0
 * 
 */
public class AccessTokenCredentialsImpl implements AccessTokenCredentials {

    private String tokenId;

    private AccessTokenCredentialsImpl() {
        super();
    }

    /**
     * Constructor
     * 
     * @param tokenId
     */
    public AccessTokenCredentialsImpl(String tokenId) {
        this();
        this.tokenId = tokenId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
