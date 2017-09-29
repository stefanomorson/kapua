/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.shared.model;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;

import java.util.Date;

public abstract class GwtEntityModel extends KapuaBaseModel {

    private static final long serialVersionUID = -1718291615353609658L;

    public GwtEntityModel() {
        super();
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("createdOnFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime(getCreatedOn()));
        } else {
            return super.get(property);
        }
    }

    public String getScopeId() {
        return get("scopeId");
    }

    public void setScopeId(String scopeId) {
        set("scopeId", scopeId);
    }

    public String getId() {
        return get("id");
    }

    public void setId(String id) {
        set("id", id);
    }

    public Date getCreatedOn() {
        return get("createdOn");
    }

    public String getCreatedOnFormatted() {
        return get("createdOnFormatted");
    }

    public void setCreatedOn(Date createdOn) {
        set("createdOn", createdOn);
    }

    public String getCreatedBy() {
        return get("createdBy");
    }

    public void setCreatedBy(String createdBy) {
        set("createdBy", createdBy);
    }
}
