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
package org.eclipse.kapua.service.commons.amqp;

import org.eclipse.kapua.service.commons.eventbus.EventBusConsumerOptions;

public class AmqpBridgeConsumerOptions extends EventBusConsumerOptions {

    private AmqpBridgeClientOptions clientOptions;

    protected AmqpBridgeConsumerOptions() {

    }

    protected AmqpBridgeConsumerOptions(AmqpBridgeConsumerOptions options) {
        super(options);
        clientOptions = AmqpBridgeClientOptions.from(options.getClientOptions());
    }

    public AmqpBridgeClientOptions getClientOptions() {
        return clientOptions;
    }

    public void setClientOptions(AmqpBridgeClientOptions endpoint) {
        this.clientOptions = endpoint;
    }

    public static AmqpBridgeConsumerOptions create() {
        return new AmqpBridgeConsumerOptions();
    }

    public static AmqpBridgeConsumerOptions from(AmqpBridgeConsumerOptions aConfig) {
        if (aConfig == null) {
            return null;
        }

        AmqpBridgeConsumerOptions newConfig = new AmqpBridgeConsumerOptions(aConfig);
        return newConfig;
    }
}
