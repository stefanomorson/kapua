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
package org.eclipse.kapua.service.account.proxy.http.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;
import org.eclipse.kapua.service.account.proxy.http.setting.KapuaAccountProxySetting;
import org.eclipse.kapua.service.account.proxy.http.setting.KapuaAccountProxySettingKeys;

/**
 * Class that offers access to account settings
 * 
 * @since 1.0
 *
 */
public class KapuaAccountProxySetting extends AbstractKapuaSetting<KapuaAccountProxySettingKeys> {

    /**
     * Resource file from which source properties.
     * 
     */
    private static final String ACCOUNT_CONFIG_RESOURCE = "kapua-account-proxy-setting.properties";

    private static final KapuaAccountProxySetting INSTANCE = new KapuaAccountProxySetting();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link KapuaAccountProxySettingKeys#ACCOUNT_KEY} value.
     * 
     */
    private KapuaAccountProxySetting() {
        super(ACCOUNT_CONFIG_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link KapuaAccountProxySetting}.
     * 
     * @return A singleton instance of KapuaAccountSetting.
     */
    public static KapuaAccountProxySetting getInstance() {
        return INSTANCE;
    }
}
