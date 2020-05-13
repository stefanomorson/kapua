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
package org.eclipse.kapua.service.commons.eventbus;

import org.eclipse.kapua.service.commons.ContainerBuilder;
import org.eclipse.kapua.service.commons.LifecycleHandler;
import org.eclipse.kapua.service.commons.MessageRoutingContext;

import com.google.common.eventbus.EventBus;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;

/**
 * This interface represents an {@link EventBusConsumerBuilder}. An {@link EventBus} consumer builder creates an 
 * {@link EventBusConsumer} using the provided configuration.
 *
 * @param <T> the type of the message body
 */
public interface EventBusConsumerBuilder<T> extends ContainerBuilder<EventBusConsumerBuilder<T>, EventBusConsumer, EventBusConsumerOptions> {

    public EventBusConsumerBuilder<T> registerHandler(String address, LifecycleHandler<MessageRoutingContext<Message<T>>> handler);

    public static <T> EventBusConsumerBuilder<T> builder(Vertx aVertx) {
        return new EventBusConsumerImpl.Builder<T>(aVertx);
    }

    public static <T> EventBusConsumerBuilder<T> builder(Vertx aVertx, EventBusConsumerOptions config) {
        return new EventBusConsumerImpl.Builder<T>(aVertx, config);
    }
}