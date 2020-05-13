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

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class that helps to handle a collection of containers
 *
 */
public class Containers {

    private Map<String, Container> services = new HashMap<>();

    public Map<String, Container> getContainers() {
        return services;
    }
}