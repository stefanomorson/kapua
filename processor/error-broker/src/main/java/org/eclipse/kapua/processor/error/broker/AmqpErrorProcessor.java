/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.processor.error.broker;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.processor.commons.MessageProcessor;
import org.eclipse.kapua.processor.commons.MessageProcessorConfig;

import io.vertx.core.Vertx;

public class AmqpErrorProcessor extends MessageProcessor<Message, Message> {

    protected AmqpErrorProcessor(Vertx aVertx, MessageProcessorConfig<Message, Message> aConfig) {
        super(aVertx, aConfig);
    }

    public static AmqpErrorProcessor create(Vertx aVertx, MessageProcessorConfig<Message, Message> aConfig) {
        return new AmqpErrorProcessor(aVertx, aConfig);
    }
}
