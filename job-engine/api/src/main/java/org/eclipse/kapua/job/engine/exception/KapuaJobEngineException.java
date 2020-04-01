/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.exception;

import org.eclipse.kapua.KapuaException;

/**
 * The KapuaJobEngineException class is the superclass of {@link Exception}s in {@link org.eclipse.kapua.job.engine.JobEngineService}.<br>
 *
 * @since 1.0.0
 */
public abstract class KapuaJobEngineException extends KapuaException {

    private static final long serialVersionUID = 6422745329878392484L;

    private static final String KAPUA_ERROR_MESSAGES = "kapua-job-engine-service-error-messages";

    /**
     * Builds a new KapuaException instance based on the supplied KapuaErrorCode.
     *
     * @param code
     */
    protected KapuaJobEngineException(KapuaJobEngineErrorCodes code) {
        super(code);
    }

    /**
     * Builds a new KapuaException instance based on the supplied KapuaErrorCode
     * and optional arguments for the associated exception message.
     *
     * @param code
     * @param arguments
     */
    protected KapuaJobEngineException(KapuaJobEngineErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Builds a new KapuaJobEngineException instance based on the supplied KapuaAccountErrorCode,
     * an Throwable cause, and optional arguments for the associated exception message.
     *
     * @param code
     * @param cause
     * @param arguments
     */
    protected KapuaJobEngineException(KapuaJobEngineErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
