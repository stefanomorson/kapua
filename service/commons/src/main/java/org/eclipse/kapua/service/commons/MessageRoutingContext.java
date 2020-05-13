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

import io.vertx.core.Vertx;

/**
 * This interface represents a {@link MessageRoutingContext}. A message routing context hosts the message and holds
 * a reference to the Vertx instance. If an {@link MessageRoute#handler} invokes the next method the context is passed
 * to the next handler in the list of configured handlers.
 *  
 *
 * @param <T> the type of the message
 */
public interface MessageRoutingContext<T> {

    /**
     * @return the Vertx instance used to create the Router
     */
    public Vertx vertx();

    /**
     * @return the message hold by this context
     */
    public T message();

    /**
     * pass the context to the next handler in the list
     */
    public void next();
}
