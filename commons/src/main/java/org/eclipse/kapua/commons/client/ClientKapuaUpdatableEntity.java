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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

public abstract class ClientKapuaUpdatableEntity extends ClientKapuaEntity implements KapuaUpdatableEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -3329633470418927087L;

    protected Date modifiedOn;

    protected KapuaEid modifiedBy;

    protected int optlock;

    protected String attributes;

    protected String properties;

    public ClientKapuaUpdatableEntity(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Override
    public KapuaId getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(KapuaId modifiedBy) {
        this.modifiedBy = KapuaEid.parseKapuaId(modifiedBy);
    }

    @Override
    public int getOptlock() {
        return optlock;
    }

    @Override
    public void setOptlock(int optlock) {
        this.optlock = optlock;
    }

    @Override
    public Properties getEntityAttributes() throws KapuaException {
        Properties props = new Properties();
        if (attributes != null) {
            try {
                props.load(new StringReader(attributes));
            } catch (IOException e) {
                KapuaException.internalError(e);
            }
        }
        return props;
    }

    @Override
    public void setEntityAttributes(Properties props) throws KapuaException {
        if (props != null) {
            try {
                StringWriter writer = new StringWriter();
                props.store(writer, null);
                attributes = writer.toString();
            } catch (IOException e) {
                KapuaException.internalError(e);
            }
        }
    }

    @Override
    public Properties getEntityProperties() throws KapuaException {
        Properties props = new Properties();
        if (properties != null) {
            try {
                props.load(new StringReader(properties));
            } catch (IOException e) {
                KapuaException.internalError(e);
            }
        }
        return props;
    }

    @Override
    public void setEntityProperties(Properties props) throws KapuaException {
        if (props != null) {
            try {
                StringWriter writer = new StringWriter();
                props.store(writer, null);
                properties = writer.toString();
            } catch (IOException e) {
                KapuaException.internalError(e);
            }
        }
    }

}
