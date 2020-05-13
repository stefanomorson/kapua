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
import java.util.stream.Collectors;

import org.eclipse.kapua.service.commons.amqp.AmqpBridgeConsumerBuilder;
import org.eclipse.kapua.service.commons.app.AbstractKapuaServiceApplication;
import org.eclipse.kapua.service.commons.app.Context;
import org.eclipse.kapua.service.commons.eventbus.DefaultEventBusServiceProcessor;
import org.eclipse.kapua.service.commons.eventbus.EventBusConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.liquibase.LiquibaseServiceLocatorApplicationListener;

import io.vertx.core.buffer.Buffer;

@SpringBootApplication
public class LifecycleConsumerApplication extends AbstractKapuaServiceApplication<Context, LifecycleConsumerConfiguration> {

    Logger logger = LoggerFactory.getLogger(LifecycleConsumerApplication.class);

    private KuraLifecycleController lifecycleController;
    private AmqpBridgeConsumerBuilder<Buffer> lifecycleConsumerBuilder;
    private ErrorHandlerController errorHandlerController;
    private EventBusConsumerBuilder<Buffer> errorHandlerConsumerBuilder;

    @Autowired
    public void setLifecycleController(KuraLifecycleController lifecycleController) {
        this.lifecycleController = lifecycleController;
    }

    @Autowired
    public void setLifecycleConsumerBuilder(AmqpBridgeConsumerBuilder<Buffer> lifecycleConsumerBuilder) {
        this.lifecycleConsumerBuilder = lifecycleConsumerBuilder;
    }

    @Autowired
    public void setErrorHandlerController(ErrorHandlerController errorHandlerController) {
        this.errorHandlerController = errorHandlerController;
    }

    @Autowired
    public void setErrorHandlerConsumerBuilder(EventBusConsumerBuilder<Buffer> errorHandlerConsumerBuilder) {
        this.errorHandlerConsumerBuilder = errorHandlerConsumerBuilder;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LifecycleConsumerApplication.class);
        app.setBannerMode(Banner.Mode.CONSOLE);
        // Removing LiquibaseServiceLocatorApplicationListener avoids SpringPackageScanClassResolver,
        // who is only compatible with Liquibase >= 3.2.3
        app.setListeners(app.getListeners().stream()
                .filter((listener) -> !(listener instanceof LiquibaseServiceLocatorApplicationListener))
                .collect(Collectors.toSet()));
        app.run(args);
    }

    @Override
    protected final void runInternal(Context context, LifecycleConsumerConfiguration config) throws Exception {
        Objects.requireNonNull(context, "param: context");
        Objects.requireNonNull(config, "param: config");

        // Configure Services
        DefaultEventBusServiceProcessor<Buffer> lifecycleProcessor = new DefaultEventBusServiceProcessor<>(context.getVertx());
        lifecycleProcessor.getConfiguration().registerController(lifecycleController);
        lifecycleConsumerBuilder.registerHandler("$EDC", lifecycleProcessor);

        DefaultEventBusServiceProcessor<Buffer> errorProcessor = new DefaultEventBusServiceProcessor<>(context.getVertx());
        errorProcessor.getConfiguration().registerController(errorHandlerController);
        errorHandlerConsumerBuilder.registerHandler("error", errorProcessor);
    }
}