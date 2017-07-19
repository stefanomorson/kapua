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
package org.eclipse.kapua.commons.client;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;

public abstract class ClientKapuaEntity implements KapuaEntity, Serializable {

    protected KapuaEid id;

    protected KapuaEid scopeId;

    protected Date createdOn;

    protected KapuaEid createdBy;

    /**
     * 
     */
    private static final long serialVersionUID = 6834579599324790831L;

    public ClientKapuaEntity(KapuaId scopeId) {
        this.scopeId = KapuaEid.parseKapuaId(scopeId);;
    }

    @Override
    public KapuaId getId() {
        return id;
    }

    @Override
    public void setId(KapuaId id) {
        this.id = KapuaEid.parseKapuaId(id);
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = KapuaEid.parseKapuaId(scopeId);
    }

    @Override
    public Date getCreatedOn() {
        return createdOn;
    }

    @Override
    public KapuaId getCreatedBy() {
        return createdBy;
    }
}
