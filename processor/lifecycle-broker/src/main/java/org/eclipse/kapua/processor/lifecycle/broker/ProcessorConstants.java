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
package org.eclipse.kapua.processor.lifecycle.broker;

public class ProcessorConstants {

    public static final String CONFIG_PROP_PROCESSOR = "kapua.lifecycleProcessor";
    public static final String CONFIG_PROP_PROCESSOR_MSG_SOURCE_AMQP = "kapua.lifecycleProcessor.messageSource.amqp";
    public static final String CONFIG_PROP_PROCESSOR_ERR_TARGET_AMQP = "kapua.lifecycleProcessor.errorTarget.amqp";

    private ProcessorConstants() {}
}
