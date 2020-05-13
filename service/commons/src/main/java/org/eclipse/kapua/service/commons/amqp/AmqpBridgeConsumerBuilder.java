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
package org.eclipse.kapua.service.commons.amqp;

import org.eclipse.kapua.service.commons.ContainerBuilder;
import org.eclipse.kapua.service.commons.LifecycleHandler;
import org.eclipse.kapua.service.commons.MessageRoutingContext;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;

/**
 * This interface represents a {@link AmqpBridgeConsumerBuilder}. An AmqpBridge consumer builder creates an 
 * {@link AmqpBridgeConsumer} using the provided configuration.
 *
 * @param <T>
 */
public interface AmqpBridgeConsumerBuilder<T> extends ContainerBuilder<AmqpBridgeConsumerBuilder<T>, AmqpBridgeConsumer, AmqpBridgeConsumerOptions> {

    /**
     * Binds a {@link LifecycleHandler} to an AMQP address
     * 
     * @param address
     * @param handler
     * @return
     */
    public AmqpBridgeConsumerBuilder<T> registerHandler(String address, LifecycleHandler<MessageRoutingContext<Message<T>>> handler);

    public static <T> AmqpBridgeConsumerBuilder<T> builder(Vertx vertx) {
        return new AmqpBridgeConsumerImpl.Builder<T>(vertx);
    }

    public static <T> AmqpBridgeConsumerBuilder<T> builder(Vertx vertx, AmqpBridgeConsumerOptions options) {
        return new AmqpBridgeConsumerImpl.Builder<T>(vertx, options);
    }
}