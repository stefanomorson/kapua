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
package org.eclipse.kapua.service.authorization.group.role;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.client.PermissionImpl;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.role.RolePermission;

/**
 * {@link RolePermission} implementation.
 *
 * @since 1.0.0
 */
@Entity(name = "RolePermission")
@Table(name = "athz_role_permission")
public class RolePermissionImpl extends AbstractKapuaEntity implements RolePermission {

    private static final long serialVersionUID = -4107313856966377197L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "role_id"))
    })
    private KapuaEid roleId;

    @Embedded
    private PermissionImpl permission;

    protected RolePermissionImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param rolePermission
     */
    public RolePermissionImpl(RolePermission rolePermission) {
        super(rolePermission);

        setId(rolePermission.getId());
        setRoleId(rolePermission.getRoleId());
        setPermission(rolePermission.getPermission());
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public RolePermissionImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor
     *
     * @param scopeId
     * @param permission
     */
    public RolePermissionImpl(KapuaId scopeId, Permission permission) {
        this(scopeId);
        setPermission(permission);
    }

    @Override
    public void setRoleId(KapuaId roleId) {
        this.roleId = KapuaEid.parseKapuaId(roleId);
    }

    @Override
    public KapuaId getRoleId() {
        return roleId;
    }

    @Override
    public void setPermission(Permission permission) {
        PermissionImpl permissionImpl = null;
        if (permission != null) {
            permissionImpl = permission instanceof PermissionImpl ? (PermissionImpl) permission : new PermissionImpl(permission);
        }
        this.permission = permissionImpl;
    }

    @Override
    public Permission getPermission() {
        return permission != null ? permission : new PermissionImpl(null, null, null, null);
    }
}
