/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.proxy.http.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Authentication setting key
 *
 * @since 1.0
 *
 */
public enum KapuaAuthenticationSettingKeys implements SettingKey {
    AUTHENTICATION_PROXY_HOST("authentication.proxy.host"),
    AUTHENTICATION_PROXY_PORT("authentication.proxy.port"),

    //event queues
    AUTHENTICATION_EVENT_ADDRESS("authentication.eventAddress");

    private String key;

    private KapuaAuthenticationSettingKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
