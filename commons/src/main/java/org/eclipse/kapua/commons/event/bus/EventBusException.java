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
package org.eclipse.kapua.commons.event.bus;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;


public class EventBusException extends KapuaException {

    /**
     * 
     */
    private static final long serialVersionUID = -5552411080939727864L;

    public EventBusException(Throwable cause) {
        // TODO Add error code for event bus
        super(KapuaErrorCodes.INTERNAL_ERROR, cause, (Object[])null);
     }

}
