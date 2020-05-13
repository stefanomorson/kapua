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

import io.vertx.core.Handler;

/**
 * This interface represents a {@link MessageRoute}. A message route forwards the message to the
 * handler if the message satisfies a condition that is checked through the {@link Matcher#match}
 * method. 
 *
 * @param <T>
 */
public interface MessageRoute<T> {

    public interface Matcher<T> {
        public boolean match(String key, String value, T message);
    }

    /**
     * Appends an optional where condition to the route. If the message matches all the where 
     * conditions then the message is forwarded to the configured handler. If there're none
     * every message will match the route.
     * 
     * @param key
     * @param value
     * @return the same {@link MessageRoute} instance
     */
    public MessageRoute<T> where(String key, String value);

    /**
     * Handler that will be invoked if the message matches the route.
     *  
     * @param handler
     * @return the same {@link MessageRoute} instance
     */
    public MessageRoute<T> handler(Handler<MessageRoutingContext<T>> handler);

    /**
     * Handler that will be invoked if the message matches the route but {@link MessageRoute#handler}
     * fails
     * 
     * @param handler
     * @return the same {@link MessageRoute} instance
     */
    public MessageRoute<T> failureHandler(Handler<MessageRoutingContext<T>> handler);
}
