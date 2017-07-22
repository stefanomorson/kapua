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
package org.eclipse.kapua.account.service.proxy.model;

import org.eclipse.kapua.commons.client.ClientKapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;

public class AccountImpl extends ClientKapuaNamedEntity implements org.eclipse.kapua.service.account.Account {

    /**
     * 
     */
    private static final long serialVersionUID = 1335774617480154837L;

    private org.eclipse.kapua.account.service.proxy.model.OrganizationImpl organization;
    private String parentAccountPath;

    public AccountImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public org.eclipse.kapua.service.account.Organization getOrganization() {
        return organization;
    }

    @Override
    public void setOrganization(org.eclipse.kapua.service.account.Organization organization) {
        this.organization = (org.eclipse.kapua.account.service.proxy.model.OrganizationImpl)organization;
    }

    @Override
    public String getParentAccountPath() {
        return parentAccountPath;
    }

    @Override
    public void setParentAccountPath(String parentAccountPath) {
        this.parentAccountPath = parentAccountPath;
    }
}
