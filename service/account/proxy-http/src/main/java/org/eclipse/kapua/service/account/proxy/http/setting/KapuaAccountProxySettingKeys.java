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

import org.eclipse.kapua.commons.setting.SettingKey;
import org.eclipse.kapua.service.account.proxy.http.setting.KapuaAccountProxySettingKeys;

/**
 * Available settings key for account service
 * 
 * @since 1.0
 *
 */
public enum KapuaAccountProxySettingKeys implements SettingKey {
    /**
     * The key value in the configuration resources.
     */
    ACCOUNT_PROXY_HOST("account.proxy.host"),
    ACCOUNT_PROXY_PORT("account.proxy.port"),
    ACCOUNT_EVENT_ADDRESS("account.eventAddress");

    private String key;

    /**
     * Set up the {@code enum} with the key value provided
     * 
     * @param key
     *            The value mapped by this {@link Enum} value
     */
    private KapuaAccountProxySettingKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the key for this {@link KapuaAccountProxySettingKeys}
     * 
     */
    public String key() {
        return key;
    }
}
