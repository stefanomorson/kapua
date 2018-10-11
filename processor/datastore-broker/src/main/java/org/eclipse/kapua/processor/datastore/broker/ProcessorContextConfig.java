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
package org.eclipse.kapua.processor.datastore.broker;

import javax.inject.Named;

import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.ObjectContextConfig;
import org.eclipse.kapua.commons.core.vertx.RestServiceConfig;
import org.eclipse.kapua.commons.core.vertx.RestServiceVerticle;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.processor.commons.AmqpConsumerConfig;
import org.eclipse.kapua.processor.commons.CommonConstants;
import org.eclipse.kapua.processor.commons.JAXBContextProviderImpl;
import org.eclipse.kapua.processor.commons.MessageProcessorVerticle;

import com.google.inject.Provides;

public class ProcessorContextConfig extends ObjectContextConfig {

    public ProcessorContextConfig() {
    }

    @Override
    protected void configure() {
        super.configure();
        bind(MainVerticle.class);
        bind(RestServiceVerticle.class);
        bind(JAXBContextProvider.class).to(JAXBContextProviderImpl.class);
        bind(MessageProcessorVerticle.class).to(AmqpDatastoreProcessorVerticle.class);
        bind(AmqpDatastoreProcessorConfigFactory.class);
    }

    @Provides
    @Named(CommonConstants.CONFIG_PROP_REST)
    RestServiceConfig provideHttpServiceImplConfig(Configuration config) {
        return RestServiceConfig.create(CommonConstants.CONFIG_PROP_REST, config);
    }

    @Provides
    @Named(ProcessorConstants.CONFIG_PROP_PROCESSOR_MSG_SOURCE_AMQP)
    AmqpConsumerConfig provideMessageSourceAmqpConfig(Configuration config) {
        return AmqpConsumerConfig.create(ProcessorConstants.CONFIG_PROP_PROCESSOR_MSG_SOURCE_AMQP, config);
    }

    @Provides
    @Named(ProcessorConstants.CONFIG_PROP_PROCESSOR_ERR_TARGET_AMQP)
    AmqpConsumerConfig provideErrorTargetAmqpConfig(Configuration config) {
        return AmqpConsumerConfig.create(ProcessorConstants.CONFIG_PROP_PROCESSOR_ERR_TARGET_AMQP, config);
    }

    @Provides
    AmqpDatastoreProcessorConfig provideDatastoreProcessorConfig(AmqpDatastoreProcessorConfigFactory factory) {
        return factory.create();
    }
}
