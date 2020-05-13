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

import java.util.Set;

import org.eclipse.kapua.service.commons.MessageServiceController;
import org.eclipse.kapua.service.commons.ServiceConfiguration;

import io.vertx.core.eventbus.Message;

/**
 * This interface represents an EventBus service configuration. It collects a list of {@link MessageServiceController}(s)
 * that implements the message handling logic.
 *
 * @param <T> the type of the EventBus message body
 */
public interface EventBusServiceConfiguration<T> extends ServiceConfiguration<EventBusServiceConfiguration<T>> {

    public EventBusServiceConfiguration<T> registerControllers(Set<MessageServiceController<Message<T>>> someControllers);

    public EventBusServiceConfiguration<T> registerController(MessageServiceController<Message<T>> controller);

    public Set<MessageServiceController<Message<T>>> getControllers();
}
