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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.kapua.service.commons.Container;
import org.eclipse.kapua.service.commons.ContainerBuilder;
import org.eclipse.kapua.service.commons.ContainerBuilders;
import org.eclipse.kapua.service.commons.Containers;
import org.eclipse.kapua.service.commons.HealthCheckProvider;
import org.eclipse.kapua.service.commons.http.HttpMonitorContainer;
import org.eclipse.kapua.service.commons.http.HttpMonitorContainerBuilder;
import org.eclipse.kapua.service.commons.http.HttpMonitorProcessor;
import org.eclipse.kapua.service.commons.http.HttpMonitorContainerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

/**
 * Base class that can be extended to implement your own Vertx based application. Derived classes can add
 * their own behavior by overriding methods {@link #init(InitContext,C)}/{@link #init(InitContext,C,Future)}
 * and/or {@link #run(Context,C)}/{@link #run(Context,C,Future)}.
 * The class holds a reference to the root Vertx instance that is used to deploy the service verticles.
 *
 * @param <C>
 *            the configuration class associated with your own Vertx based application
 */
public class BaseApplication<X extends Context, C extends Configuration> implements ApplicationRunner {

    private static Logger logger = LoggerFactory.getLogger(BaseApplication.class);

    private InitContext<C> initContext;
    private X context;
    private Containers services;
    private HttpMonitorContainer monitorContainer;

    /**
     * Sets the initialization context of the application. Must be called before {@link #run(ApplicationArguments)} is run.
     * If the application instance is created using Spring Boot, the initialization context is injected using the configuration
     * defined in {@link AbstractBeanProvider}.
     * 
     * @param context
     *            the application initialization context
     */
    @Autowired
    public final void setInitContext(InitContext<C> context) {
        initContext = context;
    }

    @Autowired
    public final void setContext(X context) {
        this.context = context;
    }

    /**
     * Run the application. If the application startup does not complete within the time configured in {@link BaseConfiguration#getStartupTimeout()}
     * an exception is thrown and application is expected to exit.
     * If monitoring is enabled all services implementing the HealthCheckProvider interface will add their health checks to the monitoring service.
     */
    @Override
    public final void run(ApplicationArguments args) throws Exception {

        logger.info("Starting application {}", initContext.getConfig());
        Future<Void> startupFuture = Future.future();
        CountDownLatch startupLatch = new CountDownLatch(1);
        ContainerBuilders serviceBuilders = initContext.getContainerBuilders();

        Future.succeededFuture().compose(map -> {
            try {
                Set<ObjectFactory<ContainerBuilder<?, ?, ?>>> serviceBuilderFactories = initContext.getContainerBuilderFactories();
                if (serviceBuilderFactories != null && serviceBuilderFactories.size() > 0) {
                    for (ObjectFactory<ContainerBuilder<?, ?, ?>> factory : serviceBuilderFactories) {
                        ContainerBuilder<?, ?, ?> builder = factory.getObject();
                        serviceBuilders.put(builder.getConfiguration().getName(), builder);
                    }
                }

                Future<Void> initInternalReq = Future.future();
                init(initContext, initInternalReq);
                boolean isMonitoringEnabled = isMonitoringEnabled(initContext.getConfig().getHttpMonitorServiceConfig());
                HttpMonitorContainerBuilder httpMonitorBuilder = isMonitoringEnabled ? initContext.getMonitorContainerBuilder() : null;
                return initInternalReq;
            } catch (Exception exc) {
                return Future.failedFuture(exc);
            }
        }).compose(map -> {
            try {
                Future<Void> runInternalReq = Future.future();
                run(context, initContext.getConfig(), runInternalReq);
                return runInternalReq;
            } catch (Exception exc) {
                return Future.failedFuture(exc);
            }
        }).compose(map -> {

            // Create services
            HttpMonitorContainerBuilder monitorContainerBuilder = initContext.getMonitorContainerBuilder();
            HttpMonitorProcessor monitorProcessor = null;
            if (monitorContainerBuilder != null) {
                monitorProcessor = HttpMonitorProcessor.create(initContext.getVertx());
            }
            monitorContainerBuilder.processor(monitorProcessor);
            ContainerBuilders containerBuilders = initContext.getContainerBuilders();
            services = new Containers();
            for (String name : containerBuilders.getNames()) {
                Container container = containerBuilders.get(name).build();
                if (monitorProcessor != null && container instanceof HealthCheckProvider) {
                    monitorProcessor.getConfiguration().addHealthCheckProvider((HealthCheckProvider) container);
                }
                services.getContainers().put(name, container);
            }

            // Start service monitoring
            if (monitorContainerBuilder != null) {
                monitorContainer = monitorContainerBuilder.build();
                Future<String> monitorDeployFuture = Future.future();
                initContext.getVertx().deployVerticle(new AbstractVerticle() {

                    @Override
                    public void start(Future<Void> startFuture) {

                    }
                }, monitorDeployFuture);
                return monitorDeployFuture;
            } 
            return Future.succeededFuture();
        }).compose(map -> {
            // Start services
            @SuppressWarnings("rawtypes")
            List<Future> deployFutures = new ArrayList<>();
            for (String name : serviceBuilders.getNames()) {
                Future<String> serviceDeployFuture = Future.future();
                deployFutures.add(serviceDeployFuture);
 // TODO need to provide verticle
 //               initContext.getVertx().deployVerticle(services.getServices().get(name), serviceDeployFuture);
            }

            // All services must be started before proceeding
            return CompositeFuture.all(deployFutures);
        }).setHandler(startup -> {
            if (startup.succeeded()) {
                startupFuture.complete();
            } else {
                startupFuture.fail(startup.cause());
            }
            startupLatch.countDown();
        });

        C configuration = initContext.getConfig();
        if (!startupLatch.await(initContext.getConfig().getStartupTimeout(), TimeUnit.MILLISECONDS)) {
            String msg = String.format("Timeout expired %ds", configuration.getStartupTimeout());
            logger.debug("Error running application {}: {}", configuration.getApplicationName(), msg);
            throw new Exception(msg);
        } else {
            if (startupFuture.succeeded()) {
                logger.info("Application {} running", configuration.getApplicationName());
            } else {
                logger.error("Error running application {}: {}", configuration.getApplicationName(), startupFuture.cause().getMessage());
                throw new Exception(startupFuture.cause());
            }
        }
    }

    /**
     * Do not call this method yourself. Override it if you expect that it will take a short time to execute
     * 
     * @param context
     * @param config
     * @throws Exception
     */
    public void init(InitContext<C> context) throws Exception {
    }

    /**
     * This method is called by {@link #run(ApplicationArguments)} before creation of service verticles. Override
     * this method if you want for example to add ServiceVerticleBuilders to the application.
     * Do not call this method yourself. Override it if you expect that initInternal will take long time to execute
     * 
     * @param context
     *            the initialization context of the application
     * @param initFuture
     *            a future that should be called when init is complete
     * @throws Exception
     */
    public void init(InitContext<C> context, Future<Void> initFuture) throws Exception {
        init(context);
        initFuture.complete();
    }

    /*
     * Do not call this method yourself. Override it if you expect that it will take a short time to execute
     */
    public void run(X context, C config) throws Exception {
    }

    /**
     * This method is called by {@link #run(ApplicationArguments)} before deployment of service verticles. Override
     * this method if you want for example to add controllers to a specific service.
     * Do not call this method yourself. Override it if you expect that initInternal will take long time to execute
     * 
     * @param context
     *            the context of the application
     * @param config
     *            the configuration of the application
     * @param runFuture
     *            a future that should be called when run is complete
     * @throws Exception
     */
    public void run(X context, C config, Future<Void> runFuture) throws Exception {
        run(context, config);
        runFuture.complete();
    }

    private boolean isMonitoringEnabled(HttpMonitorContainerOptions aMonitorConfig) {
        Objects.requireNonNull(aMonitorConfig);
        return aMonitorConfig.isHealthCheckEnable() || aMonitorConfig.isMetricsEnable();
    }
}
