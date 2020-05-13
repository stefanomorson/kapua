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

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import io.vertx.core.Vertx;

// TODO check synchronization
class MessageRouterImpl<T> implements MessageRouter<T> {

    private Vertx vertx;
    private MessageRoute.Matcher<T> matcher;
    private Set<MessageRouteImpl<T>> routes = new HashSet<>();

    public MessageRouterImpl(Vertx aVertx, MessageRoute.Matcher<T> aMatcher) {
        vertx = aVertx;
        matcher = aMatcher;
    }

    public static <T> MessageRouterImpl<T> router(Vertx aVertx, MessageRoute.Matcher<T> aMatcher) {
        return new MessageRouterImpl<T>(aVertx, aMatcher);
    }

    @Override
    public void handle(T message) {
        Objects.requireNonNull(message);

        MessageRoutingContextImpl<T> routingContext = new MessageRoutingContextImpl<>(vertx, this, message);
        for (MessageRouteImpl<T> route : routes) {
            route.handle(routingContext);
        }
    }

    @Override
    public MessageRoute<T> handleContext(MessageRoutingContext<T> context) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MessageRoute<T> route() {
        MessageRouteImpl<T> route = new MessageRouteImpl<T>(matcher);
        routes.add(route);
        return route;
    }

    public Set<MessageRouteImpl<T>> routes() {
        return Collections.unmodifiableSet(routes);
    }
}
