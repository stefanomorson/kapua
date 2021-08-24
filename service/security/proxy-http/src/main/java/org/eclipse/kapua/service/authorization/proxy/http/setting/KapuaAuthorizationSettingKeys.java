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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.proxy.http.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Authorization setting key
 */
public enum KapuaAuthorizationSettingKeys implements SettingKey {
    AUTHORIZATION_PROXY_HOST("authorization.proxy.host"),
    AUTHORIZATION_PROXY_PORT("authorization.proxy.port"),
    AUTHORIZATION_EVENT_ADDRESS("authorization.eventAddress"),
    AUTHORIZATION_ACCOUNT_PROXY_HOST("account.proxy.host"),
    AUTHORIZATION_ACCOUNT_PROXY_PORT("account.proxy.port");

    private String key;

    private KapuaAuthorizationSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
