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
package org.eclipse.kapua.service.lifecycle.consumer.app;

import org.eclipse.kapua.service.commons.MessageRouter;
import org.eclipse.kapua.service.commons.MessageRoutingContext;
import org.eclipse.kapua.service.commons.MessageServiceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageProducer;

@Component
public class ErrorHandlerController implements MessageServiceController<Message<Buffer>> {

    private MessageProducer<Message<Buffer>> amqpBridgeClient;

    @Qualifier("errorHandler")
    @Autowired
    public void setAmqpBridgeClient(MessageProducer<Message<Buffer>> amqpBridgeClient) {
        this.amqpBridgeClient = amqpBridgeClient;
    }

    @Override
    public void registerRoutes(MessageRouter<Message<Buffer>> router) {

        router.route().where("address", "error").handler(this::handle);
    }

    public void handle(MessageRoutingContext<Message<Buffer>> ctx) {
        amqpBridgeClient.send(ctx.message());
    }
}
