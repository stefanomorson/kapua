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

import org.eclipse.kapua.service.commons.Container;
import org.eclipse.kapua.service.commons.HealthCheckProvider;
import org.eclipse.kapua.service.commons.LifecycleHandler;

/**
 * This interface represent an EventBus consumer. An EventBus consumer receives messages from the 
 * {@link io.vertx.core.eventbus.EventBus} and delivers messages to its {@link LifecycleHandler}(s). 
 * Each {@link LifecycleHandler} is associated an {@link io.vertx.core.eventbus.EventBus} address that 
 * the consumer will subscribe to.
 * Each address can have multiple handlers associated that can work as a chain. An incoming message 
 * will be delivered to the first handler in the chain, the handler can then forward the message to 
 * the next handler or stop forwarding returning the control to the caller.
 *
 */
public interface EventBusConsumer extends Container, HealthCheckProvider {
}