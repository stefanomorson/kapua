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
 * This interface represents a {@link MessageServiceController}. A service controller 
 * receives messages dispatched by the {@link MessageRouter} and forwards them to the 
 * proper handlers. The handlers in turn may invoke methods of the handled service.
 * The method {@link #registerRoutes(MessageRouter)} is invoked during initialization 
 * phase in order to bind messages that will be handled by the instance to their 
 * handlers.
 * 
 * @param <T> the message routed
 */
public interface MessageServiceController<T> {

    /**
     * Registers handlers for the messages routed to this instance
     * 
     * @param router the router
     */
    public void registerRoutes(MessageRouter<T> router);
}
