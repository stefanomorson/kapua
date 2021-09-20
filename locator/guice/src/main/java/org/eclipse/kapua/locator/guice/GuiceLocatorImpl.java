/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.locator.guice;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.core.ServiceConfig;
import org.eclipse.kapua.commons.core.ServiceContainer;
import org.eclipse.kapua.commons.core.ServiceContext;
import org.eclipse.kapua.commons.core.ServiceHandler;
import org.eclipse.kapua.commons.core.ServiceModule;
import org.eclipse.kapua.commons.core.ServiceModuleConfiguration;
import org.eclipse.kapua.commons.core.ServiceModuleProvider;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binding;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Kapua locator implementation bases on Guice framework
 */
public class GuiceLocatorImpl extends KapuaLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuiceLocatorImpl.class);

    // Default service resource file from which the managed services are read
    private static final String SERVICE_RESOURCE = "locator.xml";

    private ServiceConfig config;
    private Injector injector;

    public GuiceLocatorImpl() {
        this(SERVICE_RESOURCE);
    }

    public GuiceLocatorImpl(String resourceName) {
        try {
            init(resourceName);
            ServiceModuleConfiguration.setConfigurationProvider(() -> {
                return injector.getInstance(ServiceModuleProvider.class);
            });
        } catch (Exception exc) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.INVALID_CONFIGURATION, exc);
        }
    }

    @Override
    public <S extends KapuaService> S getService(Class<S> serviceClass) {
        try {
            return injector.getInstance(serviceClass);
        } catch (ConfigurationException e) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.SERVICE_UNAVAILABLE, e, serviceClass);
        }
    }

    @Override
    public <F extends KapuaObjectFactory> F getFactory(Class<F> factoryClass) {
        try {
            return injector.getInstance(factoryClass);
        } catch (ConfigurationException e) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.FACTORY_UNAVAILABLE, factoryClass);
        }
    }

    public <T> T getComponent(Class<T> componentClass) {
        try {
            return injector.getInstance(componentClass);
        } catch (ConfigurationException e) {
            throw new KapuaRuntimeException(KapuaLocatorErrorCodes.COMPONENT_UNAVAILABLE, componentClass);
        }
    }

    @Override
    public List<KapuaService> getServices() {
        final List<KapuaService> servicesList = new ArrayList<>();
        final Map<Key<?>, Binding<?>> bindings = injector.getBindings();
        for (Binding<?> binding : bindings.values()) {
            final Class<?> clazz = binding.getKey().getTypeLiteral().getRawType();
            if (KapuaService.class.isAssignableFrom(clazz)) {
                servicesList.add(injector.getInstance(clazz.asSubclass(KapuaService.class)));
            }
        }
        return servicesList;
    }

    private void init(String resourceName) throws Exception {
        // Find locator configuration file. Exactly one non empty is expected.
        List<URL> locatorConfigurations = Arrays.asList(ResourceUtils.getResource(resourceName));
        if (locatorConfigurations.isEmpty()) {
            throw new Exception(String.format("Locator configurations in %s are empty"));
        }

        // Read configurations from locator file
        URL locatorConfigURL = locatorConfigurations.get(0);
        LocatorConfig locatorConfig = LocatorConfig.fromURL(locatorConfigURL);

        ServiceConfig.Builder builder = new ServiceConfig.Builder();
        builder.addIncludedPackages(locatorConfig.getIncludedPackageNames());
        builder.addExcludedPackages(locatorConfig.getExcludedPackageNames());

        // Scan packages listed in to find Kapua modules
        Collection<String> packageNames = locatorConfig.getIncludedPackageNames();
        Reflections reflections = new Reflections(packageNames);
        Set<Class<? extends AbstractKapuaModule>> moduleClazzes = reflections.getSubTypesOf(AbstractKapuaModule.class);
        // Instantiate Kapua modules 
        List<AbstractKapuaModule> modules = new ArrayList<>();
        LOGGER.info("====== Loading Kapua Modules =====");
        for(Class<? extends AbstractKapuaModule> moduleClazz:moduleClazzes) {
            if (isExcluded(moduleClazz.getName(), locatorConfig.getExcludedPackageNames())) {
                builder.addKapuaModuleClass(moduleClazz);
                LOGGER.debug("Module: {}, found .... EXCLUDED", moduleClazz.getSimpleName());
                continue;
            }
            LOGGER.info("Module: {}, found ....", moduleClazz.getSimpleName());
            modules.add(moduleClazz.newInstance());
            LOGGER.info("Module: {}, load DONE", moduleClazz.getSimpleName());
        }
        // KapuaModule will be removed as soon as bindings will be moved to local modules
        LOGGER.info("Module: {}, found ....", KapuaModule.class.getSimpleName());
        modules.add(new KapuaModule(resourceName));
        LOGGER.info("Module: {}, load DONE", KapuaModule.class.getSimpleName());
        LOGGER.info("==================================");

        Set<Class<? extends ServiceHandler>> handlerClazzes = reflections.getSubTypesOf(ServiceHandler.class);
        List<ServiceHandler> handlers = new ArrayList<>();
        LOGGER.info("====== Loading Event Handlers =====");
        for(Class<? extends ServiceHandler> handlerClazz:handlerClazzes) {
            if (isExcluded(handlerClazz.getName(), locatorConfig.getExcludedPackageNames())) {
                LOGGER.debug("Handler: {}, found .... EXCLUDED", handlerClazz.getSimpleName());
                continue;
            }
            LOGGER.info("Handler: {}, found ....", handlerClazz.getSimpleName());
            builder.addServiceHandlerClass(handlerClazz);
            LOGGER.info("Handler: {}, load DONE", handlerClazz.getSimpleName());
        }
        LOGGER.info("==================================");

        injector = Guice.createInjector(modules);

        // Wrap service modules with service handlers
        // To be removed after removal of ServiceModules
        Set<Class<? extends ServiceModule>> serviceModuleClazzes = reflections.getSubTypesOf(ServiceModule.class);
        LOGGER.info("====== Loading Service Module wrappers =====");
        for(Class<? extends ServiceModule> serviceModuleClazz:serviceModuleClazzes) {
            if (isExcluded(serviceModuleClazz.getName(), locatorConfig.getExcludedPackageNames()) || serviceModuleClazz.getAnnotation(KapuaProvider.class) == null) {
                LOGGER.debug("Service Module: {}, found .... EXCLUDED", serviceModuleClazz.getSimpleName());
                continue;
            }
            LOGGER.info("Service Module: {}, found ....", serviceModuleClazz.getSimpleName());
            builder.addServiceHandler(new ServiceHandler() {

                private ServiceModule module;

                @Override
                public void init(ServiceContext context) throws KapuaException {
                    try {
                        module = serviceModuleClazz.newInstance();
                        injector.injectMembers(module);
                        module.start();
                    } catch (InstantiationException | IllegalAccessException e) {
                        new KapuaException(KapuaErrorCodes.SERVICE_HANDLER_ERROR, e);
                    }
                }

                @Override
                public void destroy() {
                    try {
                        module.stop();
                        module = null;
                    } catch (KapuaException e) {
                        throw new KapuaRuntimeException(KapuaErrorCodes.SERVICE_HANDLER_ERROR, e);
                    }
                }
            });
            LOGGER.info("Service Module: {}, wrapping with handler DONE", serviceModuleClazz.getSimpleName());
        }
        LOGGER.info("========================================");
        ////

        config = builder.build();
        ServiceContainer.initializer(config, injector);
    }

    private boolean isExcluded(String className, Collection<String> excludedPkgs) {
        if (className == null || className.isEmpty()) {
            return true;
        }
        if (excludedPkgs == null || excludedPkgs.isEmpty()) {
            return false;
        }
        for(String pkg:excludedPkgs) {
            LOGGER.info("Excluded: {}", pkg);
            if (className.startsWith(pkg)) {
                return true;
            }
        }
        return false;
    }
}
