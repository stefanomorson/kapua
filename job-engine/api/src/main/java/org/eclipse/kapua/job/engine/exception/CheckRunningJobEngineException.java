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

public class CheckRunningJobEngineException extends KapuaException {

    protected CheckRunningJobEngineException(KapuaJobEngineErrorCodes code) {
        super(code);
    }

    protected CheckRunningJobEngineException(KapuaJobEngineErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    protected CheckRunningJobEngineException(KapuaJobEngineErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

}