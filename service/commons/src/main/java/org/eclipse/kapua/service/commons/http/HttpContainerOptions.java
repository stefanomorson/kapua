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

import org.eclipse.kapua.service.commons.ContainerConfiguration;
import org.eclipse.kapua.service.commons.TcpEndpointOptions;

public class HttpContainerOptions implements ContainerConfiguration {

    private String name;
    private int instances = 1;
    private String rootPath = "/";
    private TcpEndpointOptions endpoint = new TcpEndpointOptions();

    @Override
    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public int getInstances() {
        return instances;
    }

    public void setInstances(int noInstances) {
        instances = noInstances;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String aRootPath) {
        rootPath = aRootPath;
    }

    public TcpEndpointOptions getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(TcpEndpointOptions aConfig) {
        endpoint = aConfig;
    }

    @Override
    public String toString() {
        return String.format("\"name\":\"%s\""
                + ", \"instances\":\"%d\""
                + ", \"rootPath\":\"%s\""
                + ", \"endpoint\":{%s}", name, instances, rootPath, endpoint == null ? "null" : endpoint.toString());
    }

    public static HttpContainerOptions from(HttpContainerOptions aConfig) {
        if (aConfig == null) {
            return null;
        }

        HttpContainerOptions copyConfig = new HttpContainerOptions();
        copyConfig.setInstances(aConfig.getInstances());
        copyConfig.setName(aConfig.getName());
        copyConfig.setRootPath(aConfig.getRootPath());
        copyConfig.setEndpoint(TcpEndpointOptions.from(aConfig.getEndpoint()));
        return copyConfig;
    }
}