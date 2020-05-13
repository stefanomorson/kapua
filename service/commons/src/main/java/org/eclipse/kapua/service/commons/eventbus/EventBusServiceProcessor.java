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

import org.eclipse.kapua.service.commons.MessageRoutingContext;
import org.eclipse.kapua.service.commons.ServiceProcessor;

import io.vertx.core.eventbus.Message;

/**
 * This interface represents an EventBus service processor. An {@link EventBusServiceProcessor} process EventBus messages wrapped
 * by {@link MessageRoutingContext} and delivers the messages to the service represented by {@link EventBusServiceConfiguration}.
 *
 * @param <T> type of the message body
 */
public interface EventBusServiceProcessor<T> extends ServiceProcessor<EventBusServiceProcessor<T>, EventBusServiceConfiguration<T>, MessageRoutingContext<Message<T>>> {

    public EventBusServiceConfiguration<T> getConfiguration();
}