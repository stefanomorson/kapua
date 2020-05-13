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
package org.eclipse.kapua.service.commons.kafka;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.commons.ContainerConfiguration;

public class KafkaContainerOptions implements ContainerConfiguration {

    private int instances = 1;
    private String name;
    private List<String> topics;

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

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public static KafkaContainerOptions from(KafkaContainerOptions aConfig) {
        if (aConfig == null) {
            return null;
        }

        KafkaContainerOptions newConfig = new KafkaContainerOptions();
        newConfig.setInstances(aConfig.getInstances());
        newConfig.setName(aConfig.getName());
        newConfig.setTopics(new ArrayList<String>(aConfig.getTopics()));
        return newConfig;
    }
}
