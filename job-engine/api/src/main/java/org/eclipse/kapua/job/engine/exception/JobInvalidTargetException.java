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

import org.eclipse.kapua.model.id.KapuaId;

import java.util.Set;

public class JobInvalidTargetException extends JobEngineException {

    public JobInvalidTargetException(KapuaId scopeId, KapuaId jobId, Set<KapuaId> targetSublist) {
        super(KapuaJobEngineErrorCodes.JOB_TARGET_INVALID, scopeId, jobId, targetSublist);
    }
}
