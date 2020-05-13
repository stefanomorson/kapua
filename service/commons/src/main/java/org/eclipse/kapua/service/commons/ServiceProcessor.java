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

import io.vertx.core.Future;

/**
 * This interface represents a {@link ServiceProcessor} service processor dispatches the messages 
 * to the service represented by the {@link ServiceConfiguration} object.
 *
 * @param <T> self reference to the container
 * @param <C> the service configuration
 * @param <M> the message
 */
public interface ServiceProcessor<T extends ServiceProcessor<T, C, M>, C extends ServiceConfiguration<C>, M> {

    public T configuration(C configuration);

    public void process(M message);

    public void start(Future<Void> startFuture) throws Exception;
}
