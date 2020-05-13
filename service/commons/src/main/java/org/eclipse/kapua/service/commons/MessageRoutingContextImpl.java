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

public class MessageRoutingContextImpl<T> implements MessageRoutingContext<T> {

    Vertx vertx;
    MessageRouter<T> router;
    T message;

    public MessageRoutingContextImpl(Vertx aVertx, MessageRouterImpl<T> router,T message) {
        this.vertx = aVertx;
        this.router = router;
        this.message = message;
    }

    public Vertx vertx() {
        return vertx;
    }

    public T message() {
        return message;
    }

    @Override
    public void next() {
        // TODO Auto-generated method stub

    }

}
