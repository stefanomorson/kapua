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

import org.eclipse.kapua.commons.core.vertx.EventBusMessageDispatcher;
import org.eclipse.kapua.commons.core.vertx.EventBusServiceAdapter;

public class EventBusAccountAdapter implements EventBusServiceAdapter {

    private AccountResource accountResource;

    public EventBusAccountAdapter(AccountResource anAccountResource) {
        accountResource = anAccountResource;
    }

    @Override
    public void register(EventBusMessageDispatcher dispatcher) {
        dispatcher.registerBlockingHandler(AccountRequestConstants.ACTION_FIND_BY_ID, new KapuaAuthorizationHandler(accountResource::findById));
        dispatcher.registerBlockingHandler(AccountRequestConstants.ACTION_FIND_BY_NAME, new KapuaAuthorizationHandler(accountResource::findByName));
        dispatcher.registerBlockingHandler(AccountRequestConstants.ACTION_QUERY_ALL, new KapuaAuthorizationHandler(accountResource::queryAll));
        dispatcher.registerBlockingHandler(AccountRequestConstants.ACTION_COUNT_ALL, new KapuaAuthorizationHandler(accountResource::countAll));
        dispatcher.registerBlockingHandler(AccountRequestConstants.ACTION_CREATE, new KapuaAuthorizationHandler(accountResource::create));
        dispatcher.registerBlockingHandler(AccountRequestConstants.ACTION_UPDATE, new KapuaAuthorizationHandler(accountResource::update));
        dispatcher.registerBlockingHandler(AccountRequestConstants.ACTION_DELETE, new KapuaAuthorizationHandler(accountResource::delete));
        dispatcher.registerBlockingHandler(AccountRequestConstants.ACTION_FIND, new KapuaAuthorizationHandler(accountResource::find));
        dispatcher.registerBlockingHandler(AccountRequestConstants.ACTION_QUERY, new KapuaAuthorizationHandler(accountResource::query));
        dispatcher.registerBlockingHandler(AccountRequestConstants.ACTION_COUNT, new KapuaAuthorizationHandler(accountResource::count));
    }
}
