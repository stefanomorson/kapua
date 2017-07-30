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
package org.eclipse.kapua.auth.service.proxy.rest.authentication.token;

import java.util.Date;

import org.eclipse.kapua.commons.client.ClientKapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.token.AccessToken;

public class AccessTokenImpl extends ClientKapuaUpdatableEntity implements AccessToken {

    /**
     * 
     */
    private static final long serialVersionUID = -1455688984680414517L;

    private String topkenId;

    public AccessTokenImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public String getTokenId() {
        return topkenId;
    }

    @Override
    public void setTokenId(String tokenId) {
        this.topkenId = tokenId;
    }

    @Override
    public KapuaId getUserId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setUserId(KapuaId userId) {
        // TODO Auto-generated method stub

    }

    @Override
    public Date getExpiresOn() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setExpiresOn(Date expiresOn) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getRefreshToken() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        // TODO Auto-generated method stub

    }

    @Override
    public Date getRefreshExpiresOn() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRefreshExpiresOn(Date refreshExpiresOn) {
        // TODO Auto-generated method stub

    }

    @Override
    public Date getInvalidatedOn() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setInvalidatedOn(Date invalidatedOn) {
        // TODO Auto-generated method stub

    }

}
