/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.service.commons.HealthCheckProvider;
import org.eclipse.kapua.service.commons.ServiceConfiguration;

public class HttpMonitorConfiguration implements ServiceConfiguration<HttpMonitorConfiguration> {

    private String name;
    private boolean healthCheckEnable;
    private boolean metricsEnable;
    private Set<HealthCheckProvider> providers = new HashSet<>();

    public String getName() {
        return name;
    }

    @Override
    public HttpMonitorConfiguration name(String name) {
        this.name = name;
        return this;
    }

    public boolean getHealthCheckEnable() {
        return healthCheckEnable;
    }

    public HttpMonitorConfiguration healthCheckEnable(boolean enable) {
        this.healthCheckEnable = enable;
        return this;
    }

    public boolean getMetricsEnable() {
        return metricsEnable;
    }

    public HttpMonitorConfiguration metricsEnable(boolean enable) {
        this.metricsEnable = enable;
        return this;
    }

    public Set<HealthCheckProvider> getProviders() {
        return providers;
    }

    public HttpMonitorConfiguration addHealthCheckProviders(Set<HealthCheckProvider> someProviders) {
        providers.addAll(someProviders);
        return this;
    }

    public HttpMonitorConfiguration addHealthCheckProvider(HealthCheckProvider aProvider) {
        providers.add(aProvider);
        return this;
    }

}
