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

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.processor.commons.MessageProcessorConfig;

public class AmqpErrorProcessorConfig extends MessageProcessorConfig<Message, Message> {

    protected AmqpErrorProcessorConfig(String aPrefix, Configuration aConfig) {
        super(aPrefix, aConfig);
    }

    public static AmqpErrorProcessorConfig create(String aPrefix, Configuration aConfig) {
        return new AmqpErrorProcessorConfig(aPrefix, aConfig);
    }
}
