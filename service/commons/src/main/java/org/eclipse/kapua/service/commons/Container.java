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

import io.vertx.core.Future;

/**
 * This interface represents a {@link Container}. The purpose of a container is host anything that is subject to a 
 * life cycle. It should be created only through a {@link #ContainerBuilder} which will provide the configurations 
 * that may be necessary in order to create the container.
 *
 */
public interface Container {

    /**
     * Invoked when the client code that instantiated the container, starts it. The method 
     * implementation should either call {@link io.vertx.core.Future#complete} or {@link io.vertx.core.Future#fail} 
     * the future.
     * 
     * @param startFuture
     * @throws Exception
     */
    public void start(Future<Void> startFuture) throws Exception;

    /**
     * Invoked when the client code that instantiated the container, stops it. The method 
     * implementation should either call {@link io.vertx.core.Future#complete} or {@link io.vertx.core.Future#fail} 
     * the future.
     * 
     * @param stopFuture
     * @throws Exception
     */
    public void stop(Future<Void> stopFuture) throws Exception;
}
