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
import io.vertx.core.Vertx;

/**
 * This interface implements a message router. A message router dispatches a message to the
 * configured routes. The router is message agnostic in that it uses a {@link MessageRoute.Matcher} to
 * implement the specific matching logic.
 *
 * @param <T>
 */
public interface MessageRouter<T> extends Handler<T> {

    /**
     * Creates a route that matches every message
     * 
     * @return the same {@link MessageRouter} instance
     */
    public MessageRoute<T> route();

    /**
     * Handles a context.
     * 
     * @param context
     * 
     * @return the same {@link MessageRouter} instance
     */
    public MessageRoute<T> handleContext(MessageRoutingContext<T> context);

    /**
     * Creates a router instance
     * 
     * @param <T>
     *            the type of the messages handled by this router
     * @param vertx
     *            the vertx instance
     * @param matcher
     *            the matcher used for the message type T
     * @return the router instance
     */
    public static <T> MessageRouter<T> router(Vertx vertx, MessageRoute.Matcher<T> matcher) {
        return new MessageRouterImpl<T>(vertx, matcher);
    }
}