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
package org.eclipse.kapua.service.commons.kafka;

import java.util.Set;

import org.eclipse.kapua.service.commons.MessageServiceController;
import org.eclipse.kapua.service.commons.ServiceConfiguration;

import io.vertx.kafka.client.consumer.KafkaConsumerRecord;

public interface KafkaConfiguration<K, V> extends ServiceConfiguration<KafkaConfiguration<K, V>> {

    public KafkaConfiguration<K, V> registerControllers(Set<MessageServiceController<KafkaConsumerRecord<K, V>>> someControllers);

    public KafkaConfiguration<K, V> registerController(MessageServiceController<KafkaConsumerRecord<K, V>> controller);
}
