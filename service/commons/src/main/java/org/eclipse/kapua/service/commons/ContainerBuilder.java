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

/**
 * This interface represents a container builder. A container builder is used to build the
 * instance of a {@link #Container}. It must be implemented by all protocol specific container
 * builder. It implements a fluent interface.
 *
 * @param <T>
 *            self reference to the builder
 * @param <C>
 *            the container created by the builder
 */
public interface ContainerBuilder<T extends ContainerBuilder<T, C, F>, C extends Container, F extends ContainerConfiguration> {

    /**
     * @return the configuration of the container
     */
    public F getConfiguration();

    /**
     * Creates an instance of the container
     * 
     * @return the instance of the container
     */
    public C build();
}
