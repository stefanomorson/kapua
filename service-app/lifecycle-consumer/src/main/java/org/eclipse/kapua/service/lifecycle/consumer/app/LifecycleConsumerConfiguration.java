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

import org.eclipse.kapua.service.commons.amqp.AmqpBridgeConsumerOptions;
import org.eclipse.kapua.service.commons.app.BaseConfiguration;
import org.eclipse.kapua.service.commons.eventbus.EventBusConsumerOptions;
import org.springframework.beans.factory.annotation.Autowired;

public class LifecycleConsumerConfiguration extends BaseConfiguration {

    private AmqpBridgeConsumerOptions lifecycleConsumerConfig;
    private KuraLifecycleController lifecycleController;
    private EventBusConsumerOptions errorHandlerConfig;
    private ErrorHandlerController errorController;

    public AmqpBridgeConsumerOptions getLifecycleConsumerConfig() {
        return lifecycleConsumerConfig;
    }

    @Autowired
    public void setLifecycleConsumerConfig(AmqpBridgeConsumerOptions lifecycleConsumerConfig) {
        this.lifecycleConsumerConfig = lifecycleConsumerConfig;
    }

    public KuraLifecycleController getLifecycleController() {
        return lifecycleController;
    }

    @Autowired
    public void setLifecycleController(KuraLifecycleController aController) {
        lifecycleController = aController;
    }

    public EventBusConsumerOptions getErrorHandlerConfig() {
        return errorHandlerConfig;
    }

    @Autowired
    public void setErrorHandlerConfig(EventBusConsumerOptions errorHandlerConfig) {
        this.errorHandlerConfig = errorHandlerConfig;
    }

    public ErrorHandlerController getErrorController() {
        return errorController;
    }

    @Autowired
    public void setErrorController(ErrorHandlerController errorController) {
        this.errorController = errorController;
    }
}
