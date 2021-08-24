/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.proxy.http;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModule;
import org.eclipse.kapua.commons.event.ServiceEventModuleConfiguration;
import org.eclipse.kapua.service.authentication.proxy.http.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.proxy.http.setting.KapuaAuthenticationSettingKeys;

//@KapuaProvider
public class AuthenticationServiceModule extends ServiceEventModule {

    @Override
    protected ServiceEventModuleConfiguration initializeConfiguration() {
        KapuaAuthenticationSetting settings = KapuaAuthenticationSetting.getInstance();
        String address = settings.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_EVENT_ADDRESS);
        return new ServiceEventModuleConfiguration(
                address,
                null,
                new ServiceEventClientConfiguration[] {});
    }

}