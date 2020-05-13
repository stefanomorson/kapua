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

import org.eclipse.kapua.service.commons.eventbus.EventBusConsumer;

/**
 * This interface represent an AmqpBridge consumer. An AmqpBridge consumer is an {@link EventBusConsumer}
 * whose messages are received from an AMQP source (e.g. a broker) through a {@link io.vertx.amqpbridge.AmqpBridge}. 
 *
 */
public interface AmqpBridgeConsumer extends EventBusConsumer {
}