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
 *******************************************************************************/
package org.eclipse.kapua.commons.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.event.ServiceEventBusManager;
import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;

import com.google.inject.Injector;

/**
 * A class used to deploy resources required by a Kapua application. The container
 * of the Kapua application should call {@link #init()} before start using the 
 * resources and {@link #destroy()} whenresources are no longer needed, e.g. before 
 * shutting down the application.
 */
public class ServiceContainer {

    public static class InternalContext implements ServiceContext {

        private ServiceConfig serviceConfig;
        private KapuaLocator kapuaLocator;
        private ServiceEventBus serviceEventBus;

        public InternalContext(ServiceConfig config, KapuaLocator locator, ServiceEventBus eventBus) {
            serviceConfig = config;
            kapuaLocator = locator;
            serviceEventBus = eventBus;
        }

        @Override
        public ServiceConfig getServiceConfig() {
            return serviceConfig;
        }

        @Override
        public KapuaLocator getKapuaLocator() {
            return kapuaLocator;
        }

        @Override
        public ServiceEventBus getServiceEventBus() {
            return serviceEventBus;
        }
    }

    private static ServiceConfig serviceConfig;
    private static Injector serviceInjector;

    private KapuaLocator kapuaLocator;
    private ServiceEventBus serviceEventBus;
    private InternalContext context;
    private List<ServiceHandler> handlers = new ArrayList<>();

    public static void initializer(ServiceConfig config, Injector injector) {
        serviceConfig = config;
        serviceInjector = injector;
    }

    public void init() throws KapuaException {

        kapuaLocator = KapuaLocator.getInstance();
        ServiceEventBusManager.start();
        serviceEventBus = ServiceEventBusManager.getInstance();
        context = new InternalContext(serviceConfig, kapuaLocator, serviceEventBus);

        handlers.addAll(serviceConfig.getServiceHandlers());
        for(Class<? extends ServiceHandler> eventHandlerClass:serviceConfig.getServiceHandlerClasses()) {
            ServiceHandler eventHandler;
            try {
                eventHandler = eventHandlerClass.newInstance();
                serviceInjector.injectMembers(eventHandler);
                handlers.add(eventHandler);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new KapuaException(KapuaLocatorErrorCodes.INVALID_CONFIGURATION, e);
            }
        }
        ListIterator<ServiceHandler> iter = handlers.listIterator();
        while(iter.hasNext()) {
            iter.next().init(context);
        }

    }

    public void destroy() throws KapuaException {
        ListIterator<ServiceHandler> iter = handlers.listIterator();
        while(iter.hasPrevious()) {
            iter.previous().destroy();
        }

        ServiceEventBusManager.stop();
        handlers.clear();
        context = null;
    }
}
