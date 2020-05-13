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
package org.eclipse.kapua.service.commons.eventbus;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.commons.ContainerConfiguration;

public class EventBusConsumerOptions implements ContainerConfiguration {

    private int instances;
    private String name;
    private List<String> addresses;

    protected EventBusConsumerOptions() {
        setInstances(1);
    }

    protected EventBusConsumerOptions(EventBusConsumerOptions aConfig) {
        setInstances(aConfig.getInstances());
        setName(aConfig.getName());
        setAddresses(new ArrayList<String>(aConfig.getAddresses()));
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInstances() {
        return instances;
    }

    public void setInstances(int instances) {
        this.instances = instances;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public static EventBusConsumerOptions create() {
        EventBusConsumerOptions config = new EventBusConsumerOptions();
        return config;
    }

    public static EventBusConsumerOptions from(EventBusConsumerOptions aConfig) {
        if (aConfig == null) {
            return null;
        }
        EventBusConsumerOptions newConfig = new EventBusConsumerOptions(aConfig);
        return newConfig;
    }
}
