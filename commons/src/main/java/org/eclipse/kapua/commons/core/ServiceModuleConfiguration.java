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
package org.eclipse.kapua.commons.core;

import java.util.Set;

/**
 * Utility class used by {@link GuiceLocatorImpl} to pass out {@link ServiceModule}(s) 
 * to {@link ServiceModuleBundle}.
 * This class is for internal use, you should not need to use it in client code.
 * @deprecated
 * This class will be removed in a future version of Kapua
 */
@Deprecated
public class ServiceModuleConfiguration {

    public interface ConfigurationProvider {
        ServiceModuleProvider get() ;
    }

    private static ConfigurationProvider cofigurationProvider;

    private ServiceModuleConfiguration() {}

    public static void setConfigurationProvider(ConfigurationProvider aProvider) {
        cofigurationProvider = aProvider;
    }

    public static Set<ServiceModule> getServiceModules() {
        return cofigurationProvider.get().getModules();
    }

 }
