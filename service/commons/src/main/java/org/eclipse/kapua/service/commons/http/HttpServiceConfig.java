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

import org.eclipse.kapua.service.commons.HttpEndpointConfig;

public class HttpServiceConfig {


    private String name;
    private String basePath = "";
    private int instances = 1;
    private HttpEndpointConfig endpoint = new HttpEndpointConfig();

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String aBasePath) {
        basePath = aBasePath;
    }

    public int getInstances() {
        return instances;
    }

    public void setInstances(int noInstances) {
        instances = noInstances;
    }

    public HttpEndpointConfig getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(HttpEndpointConfig aConfig) {
        endpoint = aConfig;
    }

    @Override
    public String toString() {
        return String.format("\"name\":\"%s\""
                + ", \"basePath\":\"%s\""
                + ", \"instances\":\"%d\""
                + ", \"endpoint\":{%s}", name, basePath, instances, endpoint == null ? "null" : endpoint.toString());
    }
}
