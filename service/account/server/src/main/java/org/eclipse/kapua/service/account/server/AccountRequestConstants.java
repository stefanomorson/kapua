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
package org.eclipse.kapua.service.account.server;

public class AccountRequestConstants {

    public static final String ACTION_CREATE = "account.create";
    public static final String ACTION_UPDATE = "account.update";
    public static final String ACTION_DELETE = "account.delete";
    public static final String ACTION_FIND = "account.find";
    public static final String ACTION_QUERY = "account.query";
    public static final String ACTION_COUNT = "account.count";
    public static final String ACTION_FIND_BY_ID = "account.findById";
    public static final String ACTION_FIND_BY_NAME = "account.findByName";
    public static final String ACTION_QUERY_ALL = "account.queryAll";
    public static final String ACTION_COUNT_ALL = "account.countAll";
    public static final String ACTION_GET_CONFIG_METADATA = "account.getConfigMetadata";
    public static final String ACTION_GET_CONFIG_VALUES = "account.getConfigValues";
    public static final String ACTION_SET_CONFIG_VALUES = "account.setConfigValues";
    public static final String ACTION_FIND_CHILDS_RECURSIVELY = "account.findChildsRecursively";

    public static final String ACTION_PARAM_SCOPE_ID = "scopeId";
    public static final String ACTION_PARAM_ENTITY_ID = "entityId";
    public static final String ACTION_PARAM_NAME = "name";

    private AccountRequestConstants() {}
}
