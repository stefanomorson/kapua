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

import org.eclipse.kapua.service.commons.ContainerBuilder;
import org.eclipse.kapua.service.commons.LifecycleHandler;
import org.eclipse.kapua.service.commons.MessageRoutingContext;

import io.vertx.core.Vertx;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;

public interface KafkaConsumerBuilder<K1,V1> extends ContainerBuilder<KafkaConsumerBuilder<K1,V1>, KafkaConsumer, KafkaContainerOptions> {

    public KafkaConsumerBuilder<K1,V1> registerHandler(String address, LifecycleHandler<MessageRoutingContext<KafkaConsumerRecord<K1,V1>>> handler);

    public static <K1, V1> KafkaConsumerBuilder<K1, V1> builder(Vertx aVertx) {
        return new KafkaConsumerImpl.Builder<K1, V1>(aVertx);
    }

    public static <K1, V1> KafkaConsumerBuilder<K1, V1> builder(Vertx aVertx, KafkaContainerOptions config) {
        return new KafkaConsumerImpl.Builder<K1, V1>(aVertx, config);
    }
}