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

public class HttpMonitorContainerOptions implements ContainerConfiguration {

    private String name = "monitoringService";
    private String rootPath = "/monitoring";
    private boolean enableHealthCheck;
    private boolean enableMetrics;
    private TcpEndpointOptions endpoint = new TcpEndpointOptions();

    @Override
    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String aBasePath) {
        rootPath = aBasePath;
    }

    public boolean isHealthCheckEnable() {
        return enableHealthCheck;
    }

    public void setHealthCheckEnable(boolean enable) {
        enableHealthCheck = enable;
    }

    public boolean isMetricsEnable() {
        return enableMetrics;
    }

    public void setMetricsEnable(boolean enable) {
        enableMetrics = enable;
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
                + ", \"basePath\":\"%s\""
                + ", \"enableHealthCheck\":\"%b\""
                + ", \"enableMetrics\":\"%b\""
                + ", \"endpoint\":{%s}", 
                getName(), getRootPath(), isHealthCheckEnable(), isMetricsEnable(), 
                endpoint == null ? "null" : endpoint.toString());
    }
}
