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
package org.eclipse.kapua.service.event;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.kapua.model.id.KapuaId;

public interface KapuaEvent extends Serializable {

    public String getContextId();

    public void setContextId(String contextId);

    public Date getTimestamp();
    
    public void setTimestamp(Date timestamp);

    public KapuaId getUserId();

    public void setUserId(KapuaId userId);

    public String getService();

    public void setService(String servie);

    public String getEntityType();

    public void setEntityType(String entityType);

    public KapuaId getScopeId();

    public void setScopeId(KapuaId scopeId);

    public KapuaId getEntityId();

    public void setEntityId(KapuaId entityId);

    public String getOperation();

    public void setOperation(String operation);

    public String getInputs();

    public void setInputs(String inputs);

    public String getOutputs();

    public void setOutputs(String outputs);

    public String getProperties();

    public void setProperties(String properties);

    public String getNote();

    public void setNote(String note);
}
