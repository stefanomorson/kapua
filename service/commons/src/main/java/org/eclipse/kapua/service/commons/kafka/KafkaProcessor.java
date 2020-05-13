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

import org.eclipse.kapua.service.commons.MessageRoutingContext;
import org.eclipse.kapua.service.commons.ServiceProcessor;

import io.vertx.kafka.client.consumer.KafkaConsumerRecord;

public interface KafkaProcessor<K1, V1> extends ServiceProcessor<KafkaProcessor<K1, V1>, KafkaConfiguration<K1, V1>, MessageRoutingContext<KafkaConsumerRecord<K1, V1>>> {

    public KafkaConfiguration<K1, V1> getConfiguration();
}
