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
package org.eclipse.kapua.service.commons.kafka;

import java.util.HashSet;
import java.util.Set;

import org.apache.kafka.common.header.Headers;
import org.eclipse.kapua.service.commons.LifecycleHandler;
import org.eclipse.kapua.service.commons.MessageRouter;
import org.eclipse.kapua.service.commons.MessageRoutingContext;
import org.eclipse.kapua.service.commons.MessageServiceController;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;

public abstract class AbstractKafkaProcessor<K1, V1> implements KafkaProcessor<K1, V1>, LifecycleHandler<MessageRoutingContext<KafkaConsumerRecord<K1, V1>>> {

    private Vertx vertx;
    private MessageRouter<KafkaConsumerRecord<K1, V1>> router;
    private Set<MessageServiceController<KafkaConsumerRecord<K1, V1>>> controllers = new HashSet<>();

    public AbstractKafkaProcessor(Vertx aVertx, MessageRoutingContext<KafkaConsumerRecord<K1, V1>> aConverter) {

    }

    public AbstractKafkaProcessor(Vertx aVertx, KafkaContainerOptions config, MessageRoutingContext<KafkaConsumerRecord<K1, V1>> aConverter) {

    }

    @Override
    public KafkaConfiguration<K1, V1> getConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KafkaProcessor<K1, V1> configuration(KafkaConfiguration<K1, V1> aConfiguration) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public final void process(MessageRoutingContext<KafkaConsumerRecord<K1, V1>> aCtx) {
        router.handleContext(aCtx);
    }

    @Override
    public void handle(MessageRoutingContext<KafkaConsumerRecord<K1, V1>> event) {
        process(event);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        router = MessageRouter.router(vertx, (key, value, message) -> {
            Headers headers = message.record().headers();
            if (key == null && (headers == null || headers.headers(key) == null)) {
                return true;
            }
            // TODO fix  headers.headers(key).equals(value)
            if (key != null && headers != null && headers.headers(key).equals(value)) {
                return true;
            }
            return false;
        });
        registerRoutes(router, controllers);
    }

    private void registerRoutes(MessageRouter<KafkaConsumerRecord<K1, V1>> router, Set<MessageServiceController<KafkaConsumerRecord<K1, V1>>> someControllers) {
        for (MessageServiceController<KafkaConsumerRecord<K1, V1>> controller : someControllers) {
            controller.registerRoutes(router);
        }
    }
}
