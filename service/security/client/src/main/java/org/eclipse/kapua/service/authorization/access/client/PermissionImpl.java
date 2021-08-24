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
package org.eclipse.kapua.service.authorization.access.client;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * {@link Permission} implementation.
 *
 * @since 1.0.0
 */
@Embeddable
public class PermissionImpl implements Permission, Serializable {

    private static final long serialVersionUID = 6837727352903959978L;

    @Basic
    @Column(name = "domain", nullable = true, updatable = false)
    private String domain;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = true, updatable = false)
    private Actions action;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "target_scope_id", nullable = true, updatable = false))
    })
    private KapuaEid targetScopeId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "group_id", nullable = true, updatable = false))
    })
    private KapuaEid groupId;

    @Basic
    @Column(name = "forwardable", nullable = false, updatable = false)
    private boolean forwardable;

    /**
     * Constructor
     */
    protected PermissionImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param permission The {@link Permission} to parse.
     * @since 1.0.0
     */
    public PermissionImpl(Permission permission) {
        this(
                permission.getDomain(),
                permission.getAction(),
                permission.getTargetScopeId(),
                permission.getGroupId(),
                permission.getForwardable());
    }

    /**
     * Constructor.
     *
     * @param domain
     * @param action
     * @param targetScopeId
     * @param groupId
     * @since 1.0.0
     */
    public PermissionImpl(String domain, Actions action, KapuaId targetScopeId, KapuaId groupId) {
        this(domain, action, targetScopeId, groupId, false);
    }

    /**
     * Constructor.
     *
     * @param domain
     * @param action
     * @param targetScopeId
     * @param groupId
     * @since 1.0.0
     */
    public PermissionImpl(String domain, Actions action, KapuaId targetScopeId, KapuaId groupId, boolean forwardable) {

        setDomain(domain);
        setAction(action);
        setTargetScopeId(targetScopeId);
        setGroupId(groupId);
        setForwardable(forwardable);
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
        this.targetScopeId = KapuaEid.parseKapuaId(targetScopeId);
    }

    @Override
    public KapuaId getTargetScopeId() {
        return targetScopeId;
    }

    @Override
    public void setGroupId(KapuaId groupId) {
        this.groupId = KapuaEid.parseKapuaId(groupId);
    }

    @Override
    public KapuaId getGroupId() {
        return groupId;
    }

    @Override
    public boolean getForwardable() {
        return forwardable;
    }

    @Override
    public void setForwardable(boolean forwardable) {
        this.forwardable = forwardable;
    }
}
