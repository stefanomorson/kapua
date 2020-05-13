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

import java.util.Objects;

import io.vertx.core.Handler;

//TODO check synchronization
public class MessageRouteImpl<T> implements MessageRoute<T> {

    private String address;
    private String key;
    private String value;
    private Matcher<T> matcher;
    private Handler<MessageRoutingContext<T>> handler;
    private Handler<MessageRoutingContext<T>> failureHandler;

    // TODO REMOVE?
    //
    // @Override
    // public MessageRoute<T> address(String address) {
    // this.address = address;
    // return this;
    // }

    public MessageRouteImpl(Matcher<T> aMatcher) {
        this(aMatcher, null);
    }

    public MessageRouteImpl(Matcher<T> aMatcher, String anAddress) {
        matcher = aMatcher;
        address = anAddress;
    }

    @Override
    public MessageRoute<T> where(String key, String value) {
        this.key = key;
        this.value = value;
        return this;
    }

    @Override
    public MessageRoute<T> handler(Handler<MessageRoutingContext<T>> handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public MessageRoute<T> failureHandler(Handler<MessageRoutingContext<T>> handler) {
        this.failureHandler = handler;
        return this;
    }

    synchronized void handle(MessageRoutingContext<T> context) {
        Objects.requireNonNull(context);

        // TODO implement address logic
        address.toString();

        if (matcher.match(key, value, context.message())) {
            handler.handle(context);
            return;
        }
    }
}
