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

import java.util.Objects;

import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.service.commons.amqp.AmqpBridgeClientOptions;
import org.eclipse.kapua.service.commons.amqp.AmqpBridgeConsumerBuilder;
import org.eclipse.kapua.service.commons.amqp.AmqpBridgeConsumerOptions;
import org.eclipse.kapua.service.commons.app.AbstractBeanProvider;
import org.eclipse.kapua.service.commons.eventbus.EventBusConsumerBuilder;
import org.eclipse.kapua.service.commons.eventbus.EventBusConsumerOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

@Configuration
@ComponentScan("org.eclipse.kapua.service.commons")
@ComponentScan("org.eclipse.kapua.service.lifecycle.consumer.app")
public class LifecycleConsumerBeanProvider extends AbstractBeanProvider<LifecycleConsumerConfiguration> {

    private static final String LIFECYCLE_CONSUMER_BUILDER_BEAN = "lifecycleConsumerBuilder";
    private static final String ERROR_HANDLER_BUILDER_BEAN = "errorHandlerBuilder";

    @Bean
    public JAXBContextProvider jaxbContextProvider() {
        return new LifecycleConsumerJaxbContextProvider();
    }

    @Qualifier("kapuaServiceApp")
    @Bean
    public Module module() {
        return new LifecycleConsumerModule();
    }

    @Qualifier("lifecycle")
    @Bean
    @ConfigurationProperties(prefix = "lifecycle-consumer.amqp")
    public @Autowired AmqpBridgeConsumerOptions lifecycleConsumerConfig() {
        AmqpBridgeConsumerOptions config = AmqpBridgeConsumerOptions.create();
        config.setName("lifecycle-consumer.amqp");
        return config;
    }

    @Autowired
    @Bean(LIFECYCLE_CONSUMER_BUILDER_BEAN)
    public AmqpBridgeConsumerBuilder<Buffer> lifecycleConsumerBuilder(Vertx aVertx, AmqpBridgeConsumerOptions options) {
        Objects.requireNonNull(options, "param: aConfig");
        AmqpBridgeConsumerBuilder<Buffer> builder = AmqpBridgeConsumerBuilder.<Buffer>builder(aVertx, options);
        return builder;
    }

    @Qualifier("services")
    @Bean
    public ObjectFactoryCreatingFactoryBean lifecycleConsumerBuilderFactory() {
        final ObjectFactoryCreatingFactoryBean factory = new ObjectFactoryCreatingFactoryBean();
        factory.setTargetBeanName(LIFECYCLE_CONSUMER_BUILDER_BEAN);
        return factory;
    }

    @Qualifier("errorHandler")
    @Bean
    @ConfigurationProperties(prefix = "error-handler")
    public @Autowired EventBusConsumerOptions errorHandlerConfig() {
        EventBusConsumerOptions config = EventBusConsumerOptions.create();
        config.setName("error-handler.eventBus");
        return config; 
    }

    @Autowired
    @Bean(ERROR_HANDLER_BUILDER_BEAN)
    public EventBusConsumerBuilder<Buffer> errorHandlerBuilder(Vertx aVertx, EventBusConsumerOptions options) {
        Objects.requireNonNull(options, "param: aConfig");
        EventBusConsumerBuilder<Buffer> builder = EventBusConsumerBuilder.<Buffer>builder(aVertx, options);
        return builder;
    }

    @Qualifier("services")
    @Bean
    public ObjectFactoryCreatingFactoryBean errorHandlerBuilderFactory() {
        final ObjectFactoryCreatingFactoryBean factory = new ObjectFactoryCreatingFactoryBean();
        factory.setTargetBeanName(ERROR_HANDLER_BUILDER_BEAN);
        return factory;
    }

    @Qualifier("errorHandler")
    @Bean
    @ConfigurationProperties(prefix = "error-handler.client")
    public @Autowired AmqpBridgeClientOptions errorHandlerClientOptions() {
        return AmqpBridgeClientOptions.from();
    }

    @Override
    public LifecycleConsumerConfiguration configuration() {
        return new LifecycleConsumerConfiguration();
    }
}
