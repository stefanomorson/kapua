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

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;

public abstract class ClientKapuaNamedEntity extends ClientKapuaUpdatableEntity implements KapuaNamedEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -1524730576561373654L;

    protected String name;

    public ClientKapuaNamedEntity(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
