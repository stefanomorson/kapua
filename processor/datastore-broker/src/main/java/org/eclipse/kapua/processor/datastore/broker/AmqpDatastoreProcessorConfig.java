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

import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.processor.commons.MessageProcessorConfig;

public class AmqpDatastoreProcessorConfig extends MessageProcessorConfig<byte[], TransportMessage> {

    private AmqpDatastoreProcessorConfig(String aPrefix, Configuration aConfig) {
        super(aPrefix, aConfig);
    }

    public static AmqpDatastoreProcessorConfig create(String aPrefix, Configuration aConfig) {
        return new AmqpDatastoreProcessorConfig(aPrefix, aConfig);
    }
}
