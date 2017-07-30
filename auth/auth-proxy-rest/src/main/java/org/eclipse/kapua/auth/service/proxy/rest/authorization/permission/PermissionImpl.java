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
package org.eclipse.kapua.auth.service.proxy.rest.authorization.permission;

import java.io.Serializable;

import org.apache.shiro.authz.permission.WildcardPermission;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;

public class PermissionImpl extends WildcardPermission implements Permission, org.apache.shiro.authz.Permission, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7340627462746991086L;

    String domain;
    Actions action;
    KapuaId targetScopeId;
    KapuaId groupId;
    boolean forwardable;

    public PermissionImpl(String domain, Actions action, KapuaId targetScopeId, KapuaId groupId) {
        this.domain = domain;
        this.action = action;
        this.targetScopeId = targetScopeId;
        this.groupId = groupId;
    }


    @Override
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public void setAction(Actions action) {
        this.action = action;
    }

    @Override
    public Actions getAction() {
        return action;
    }

    @Override
    public void setTargetScopeId(KapuaId targetScopeId) {
        this.targetScopeId = targetScopeId;
    }

    @Override
    public KapuaId getTargetScopeId() {
        return targetScopeId;
    }

    @Override
    public void setGroupId(KapuaId groupId) {
        this.groupId = groupId;
    }

    @Override
    public KapuaId getGroupId() {
        return this.groupId;
    }

    @Override
    public void setForwardable(boolean forwardable) {
        this.forwardable = forwardable;
    }

    @Override
    public boolean getForwardable() {
        return forwardable;
    }
}
