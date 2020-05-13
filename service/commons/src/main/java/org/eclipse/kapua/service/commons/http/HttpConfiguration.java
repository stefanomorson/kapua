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
package org.eclipse.kapua.service.commons.http;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.service.commons.ServiceConfiguration;

public class HttpConfiguration implements ServiceConfiguration<HttpConfiguration> {

    private String name;
    private Set<HttpController> controllers = new HashSet<>();

    public String getName() {
        return name;
    }

    @Override
    public HttpConfiguration name(String name) {
        this.name = name;
        return this;
    }

    public Set<HttpController> getControllers() {
        return controllers;
    }

    public HttpConfiguration registerControllers(Set<HttpController> someControllers) {
        controllers.addAll(someControllers);
        return this;
    }

    public HttpConfiguration registerController(HttpController controller) {
        controllers.add(controller);
        return this;
    }
}
