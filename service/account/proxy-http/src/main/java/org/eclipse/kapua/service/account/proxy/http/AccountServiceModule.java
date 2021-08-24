/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.account.proxy.http;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModule;
import org.eclipse.kapua.commons.event.ServiceEventModuleConfiguration;
import org.eclipse.kapua.service.account.proxy.http.setting.KapuaAccountProxySetting;
import org.eclipse.kapua.service.account.proxy.http.setting.KapuaAccountProxySettingKeys;

//@KapuaProvider
public class AccountServiceModule extends ServiceEventModule {

    @Override
    protected ServiceEventModuleConfiguration initializeConfiguration() {
        KapuaAccountProxySetting settings = KapuaAccountProxySetting.getInstance();
        String address = settings.getString(KapuaAccountProxySettingKeys.ACCOUNT_EVENT_ADDRESS);
        return new ServiceEventModuleConfiguration(
                address,
                null,
                new ServiceEventClientConfiguration[] {});
    }

}