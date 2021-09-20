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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The configuration of a Kapua application
 */
public class ServiceConfig {

    public static class Builder {

        private Set<String> includedPackages = new HashSet<>();
        private Set<String> excludedPackages = new HashSet<>();
        private Set<Class<? extends ServiceHandler>> serviceHandlerClasses = new HashSet<>();
        private Set<ServiceHandler> serviceHandlers = new HashSet<>();
        private Set<Class<? extends AbstractKapuaModule>> kapuaModuleClasses = new HashSet<>();
        private Set<AbstractKapuaModule> kapuaModules = new HashSet<>();

        public Builder addIncludedPackage(String packageName) {
            includedPackages.add(packageName);
            return this;
        }

        public Builder addIncludedPackages(Collection<String> packageNames) {
            includedPackages.addAll(packageNames);
            return this;
        }

        public Builder addExcludedPackage(String packageName) {
            excludedPackages.add(packageName);
            return this;
        }

        public Builder addExcludedPackages(Collection<String> packageNames) {
            excludedPackages.addAll(packageNames);
            return this;
        }

        public Builder addServiceHandlerClass(Class<? extends ServiceHandler> handler) {
            serviceHandlerClasses.add(handler);
            return this;
        }

        public Builder addServiceHandler(ServiceHandler handler) {
            serviceHandlers.add(handler);
            return this;
        }

        public Builder addServiceHandlerClasses(Collection<Class<? extends ServiceHandler>> handlers) {
            serviceHandlerClasses.addAll(handlers);
            return this;
        }

        public Builder addServiceHandlers(Collection<ServiceHandler> handlers) {
            serviceHandlers.addAll(handlers);
            return this;
        }

        public Builder addKapuaModuleClass(Class<? extends AbstractKapuaModule> module) {
            kapuaModuleClasses.add(module);
            return this;
        }

        public Builder addKapuaModule(AbstractKapuaModule module) {
            kapuaModules.add(module);
            return this;
        }

        public Builder addKapuaModuleClasses(Collection<Class<? extends AbstractKapuaModule>> modules) {
            kapuaModuleClasses.addAll(modules);
            return this;
        }

        public Builder addKapuaModules(Collection<AbstractKapuaModule> modules) {
            kapuaModules.addAll(modules);
            return this;
        }

        public ServiceConfig build() {
            ServiceConfig config = new ServiceConfig();
            config.includedPackages = includedPackages;
            config.excludedPackages = excludedPackages;
            config.serviceHandlerClasses = serviceHandlerClasses;
            config.serviceHandlers = serviceHandlers;
            config.kapuaModuleClasses = kapuaModuleClasses;
            config.kapuaModules = kapuaModules;
            return config;
        }
    }

    private Set<String> includedPackages;
    private Set<String> excludedPackages;
    private Set<Class<? extends ServiceHandler>> serviceHandlerClasses;
    private Set<ServiceHandler> serviceHandlers;
    private Set<Class<? extends AbstractKapuaModule>> kapuaModuleClasses;
    private Set<AbstractKapuaModule> kapuaModules;

    private ServiceConfig() {}

    public Set<String> getIncludedPackages() {
        return Collections.unmodifiableSet(includedPackages);
    }

    public Set<String> getExcludedPackages() {
        return Collections.unmodifiableSet(excludedPackages);
    }

    public Set<Class<? extends ServiceHandler>> getServiceHandlerClasses() {
        return Collections.unmodifiableSet(serviceHandlerClasses);
    }

    public Set<ServiceHandler> getServiceHandlers() {
        return Collections.unmodifiableSet(serviceHandlers);
    }

    public Set<Class<? extends AbstractKapuaModule>> getKapuaModuleClasses() {
        return Collections.unmodifiableSet(kapuaModuleClasses);
    }

    public Set<AbstractKapuaModule> getKapuaModules() {
        return Collections.unmodifiableSet(kapuaModules);
    }
}
