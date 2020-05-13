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
package org.eclipse.kapua.service.commons;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A utility class that helps to handle a collection of container builders
 *
 */
public class ContainerBuilders {

    private Map<String, ContainerBuilder<?, ? extends Container, ? extends ContainerConfiguration>> builders = new HashMap<>();

    public Set<String> getNames() {
        return Collections.unmodifiableSet(builders.keySet());
    }

    public ContainerBuilders put(String name, ContainerBuilder<?, ? extends Container, ? extends ContainerConfiguration> builder) {
        builders.put(name, builder);
        return this;
    }

    public ContainerBuilder<?, ? extends Container, ? extends ContainerConfiguration> get(String name) {
        return builders.get(name);
    }
}