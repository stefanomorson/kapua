/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.core;

import org.eclipse.kapua.KapuaException;

/**
 * A class that represents a service handlder. Implementations of this interface are 
 * collected by {@link ServiceContainer}.
 * {@link #init()} is invoked during container initializaton while {@link #destroy()} 
 * is invoked during container destruction.
 */
public interface ServiceHandler {

    public void init(ServiceContext context) throws KapuaException;

    public void destroy();
}