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

import io.vertx.core.Handler;

/**
 * This interface represents a lifecycle handler. A Lifecycle handler adds lifecycle methods to
 * a {@link io.vertx.core.Handler}. 
 *
 * @param <E>
 */
public interface LifecycleHandler<E> extends Handler<E> {

    /**
     * Inits the handler. Must be called before start handling events through {@link io.vertx.core.Handler#handle}
     */
    public void init();

    /**
     * Closes the handler. {@link io.vertx.core.Handler#handle} should not be called after this method has been executed
     */
    public void close();
}
