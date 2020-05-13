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
package org.eclipse.kapua.service.commons;

import io.vertx.ext.healthchecks.HealthCheckHandler;

/**
 * This interface represents an health check provider. It should be implemented by
 * classes that provide liveness checks and/or readiness checks.
 *
 */
public interface HealthCheckProvider {

    /**
     * Registers liveness checks into the provided handler.
     * 
     * @param handler
     */
    public void registerLivenessCheckers(HealthCheckHandler handler);

    /**
     * Registers readiness checks into the provided handler.
     * 
     * @param handler
     */
    public void registerReadinessCheckers(HealthCheckHandler handler);
}
