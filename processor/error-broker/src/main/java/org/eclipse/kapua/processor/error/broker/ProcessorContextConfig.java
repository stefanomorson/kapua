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

import javax.inject.Named;

import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.ObjectContextConfig;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.processor.commons.AmqpConsumerConfig;
import org.eclipse.kapua.processor.commons.HttpServiceImplConfig;
import org.eclipse.kapua.processor.commons.HttpServiceVerticle;
import org.eclipse.kapua.processor.commons.JAXBContextProviderImpl;
import org.eclipse.kapua.processor.commons.MessageProcessorVerticle;

import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ProcessorContextConfig extends ObjectContextConfig {

    private static final String CONFIG_PROP_PROCESSOR = "kapua.errorProcessor";
    private static final String CONFIG_PROP_PROCESSOR_MSG_SOURCE_AMQP = "kapua.errorProcessor.messageSource.amqp";
    private static final String CONFIG_PROP_REST = "kapua.restService";

    public ProcessorContextConfig() {
    }

    @Override
    protected void configure() {
        super.configure();
        bind(MainVerticle.class);
        bind(HttpServiceVerticle.class);
        bind(JAXBContextProvider.class).to(JAXBContextProviderImpl.class).in(Singleton.class);
        bind(MessageProcessorVerticle.class).to(AmqpErrorProcessorVerticle.class);
        bind(AmqpErrorProcessorConfigFactory.class);
    }

    @Provides
    @Named(CONFIG_PROP_REST)
    HttpServiceImplConfig provideHttpServiceImplConfig(Configuration config) {
        return HttpServiceImplConfig.create(CONFIG_PROP_REST, config);
    }

    @Provides
    @Named(CONFIG_PROP_PROCESSOR)
    AmqpErrorProcessorConfig provideDatastoreProcessorConfig(Configuration config) {
        return AmqpErrorProcessorConfig.create(CONFIG_PROP_PROCESSOR, config);
    }

    @Provides
    @Named(CONFIG_PROP_PROCESSOR_MSG_SOURCE_AMQP)
    AmqpConsumerConfig provideMessageSourceAmqpConfig(Configuration config) {
        return AmqpConsumerConfig.create(CONFIG_PROP_PROCESSOR_MSG_SOURCE_AMQP, config);
    }
}
