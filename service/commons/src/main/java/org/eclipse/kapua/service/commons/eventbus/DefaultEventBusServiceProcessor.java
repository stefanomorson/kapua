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
package org.eclipse.kapua.service.commons.eventbus;

import org.eclipse.kapua.service.commons.LifecycleHandler;
import org.eclipse.kapua.service.commons.MessageRoutingContext;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;

/**
 * Default EventBus processor. This class extends {@link BaseEventBusServiceProcessor} and implements 
 * {@link LifecycleHandler} so it can be registered to a {@link EventBusConsumer}.
 *
 * @param <T> the type of the message body
 */
public class DefaultEventBusServiceProcessor<T> extends BaseEventBusServiceProcessor<T> implements LifecycleHandler<MessageRoutingContext<Message<T>>> {

    public DefaultEventBusServiceProcessor(Vertx aVertx) {
        super(aVertx);
        // TODO Auto-generated constructor stub
    }

    /**
     *  Messages are forwarded as is to the {@link BaseEventBusServiceProcessor#process} method.
     */
    @Override
    public void handle(MessageRoutingContext<Message<T>> event) {
        process(event);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

}
