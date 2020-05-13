/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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

/**
 * This interface represents a service configuration. A service configuration contains all
 * the resources required by a service.
 *
 * @param <C>
 */
public interface ServiceConfiguration<C extends ServiceConfiguration<C>> {

    public C name(String name);
}
