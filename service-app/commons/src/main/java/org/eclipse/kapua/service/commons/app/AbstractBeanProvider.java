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
package org.eclipse.kapua.service.commons.app;

import java.util.Objects;

import org.eclipse.kapua.service.commons.http.HttpMonitorContainerBuilder;
import org.eclipse.kapua.service.commons.http.HttpMonitorContainerOptions;
import org.eclipse.kapua.service.commons.http.HttpServiceHandlers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.codahale.metrics.SharedMetricRegistries;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.web.RoutingContext;

/**
 * This class defines the beans used by {@link BaseApplication} and several other classes (e.g. 
 * {@link BaseConfiguration}). It must be extended by a bean provider class associated with your 
 * own Vertx based application. 
 * 
 * @param <C> the configuration class associated with your own Vertx based application 
 */
public abstract class AbstractBeanProvider<C extends Configuration> {

    private static final String DEFAULT_AUTH_HANDLER = "defaultAuthHandler";

    @Bean
    @ConfigurationProperties(prefix = "vertx.metrics")
    public MetricsConfig metricsConfig() {
        return new MetricsConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "vertx")
    public VertxConfig vertxConfig() {
        return new VertxConfig();
    }

    @Bean
    @Qualifier("monitoring")
    @ConfigurationProperties(prefix = "monitoring")
    public HttpMonitorContainerOptions httpMonitoringServiceConfig() {
        return new HttpMonitorContainerOptions();
    }

    @Autowired
    @Bean
    @Qualifier("monitoring")
    public HttpMonitorContainerBuilder getHttpMonitoringServiceVerticleBuilder(Vertx vertx, HttpMonitorContainerOptions config) {
        return HttpMonitorContainerBuilder.builder(vertx, config);
    }

    @Bean(DEFAULT_AUTH_HANDLER)
    public Handler<RoutingContext> defaultAuthHandler() {
        return HttpServiceHandlers.authenticationHandler();
    }

    @Autowired
    @Bean
    public Vertx vertx(VertxConfig aVertexConfig) {
        Objects.requireNonNull(aVertexConfig, "param: config");
        VertxConfig vertxConfig = aVertexConfig;

        DropwizardMetricsOptions metrOpts = new DropwizardMetricsOptions();
        metrOpts.setEnabled(aVertexConfig.getMetrics().isEnable());
        metrOpts.setRegistryName(vertxConfig.getMetrics().getRegistryName());
        if (vertxConfig.getMetrics().getRegistryName() != null) {
            SharedMetricRegistries.getOrCreate(vertxConfig.getMetrics().getRegistryName());
            SharedMetricRegistries.setDefault(vertxConfig.getMetrics().getRegistryName());
        }

        VertxOptions opts = new VertxOptions();
        opts.setWarningExceptionTime(vertxConfig.getWarningExceptionTime());
        opts.setBlockedThreadCheckInterval(vertxConfig.getBlockedThreadCheckInterval());
        opts.setMetricsOptions(metrOpts);
        Vertx vertx = Vertx.vertx(opts);
        return vertx;
    }
//
//    @Bean
//    public InitContext<C> initContext() {
//        return new InitContextImpl<C>();
//    }

    private String applicationName;
    private long startupTimeout;

    @Value("${applicationName}")
    public void setSpplicationName(String anApplicationName) {
        applicationName = anApplicationName;
    }

    @Bean
    public String applicationName() {
        return applicationName;
    }

    @Value("${startupTimeout}")
    public void setStartupTimeout(long aStartupTimeout) {
        startupTimeout = aStartupTimeout;
    }

    @Bean
    public long startupTimeout() {
        return startupTimeout;
    }

    @Bean
    public Context context() {
        return new BaseContext(null,null);
    }

    @Bean
    public abstract C configuration();
}
