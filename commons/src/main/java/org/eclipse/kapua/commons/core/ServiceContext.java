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

import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.locator.KapuaLocator;

/**
 * The runtime context required by the Kapua application.
 */
public interface ServiceContext {

    public ServiceConfig getServiceConfig();

    public KapuaLocator getKapuaLocator();

    public ServiceEventBus getServiceEventBus();
}
