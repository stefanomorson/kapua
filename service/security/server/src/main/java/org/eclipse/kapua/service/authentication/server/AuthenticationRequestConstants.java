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
package org.eclipse.kapua.service.authentication.server;

public class AuthenticationRequestConstants {

    public static final String ACTION_LOGIN = "authentication.login";
    public static final String ACTION_AUTHENTICATE = "authentication.authenticate";
    public static final String ACTION_LOGOUT = "authentication.logout";
    public static final String ACTION_FIND_ACCESS_TOKEN = "authentication.findAccessToken";
    public static final String ACTION_REFRESH_ACCESS_TOKEN = "authentication.refreshAccessToken";
    public static final String ACTION_VERIFY_CREDENTIALS = "authentication.verifyCredentials";

    public static final String ACTION_PARAM_SCOPE_ID = "scopeId";
    public static final String ACTION_PARAM_ENTITY_ID = "entityId";
    public static final String ACTION_PARAM_NAME = "name";

    private AuthenticationRequestConstants() {}
}
