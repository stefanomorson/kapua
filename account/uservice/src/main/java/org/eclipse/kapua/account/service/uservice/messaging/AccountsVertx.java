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
package org.eclipse.kapua.account.service.uservice.messaging;

import java.util.Map;

import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountListResult;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface AccountsVertx {

    String ADDRESS = "service.accounts";

    public void find(String id, Handler<AsyncResult<Account>> resultHandler);

    public void query(KapuaQuery query, Handler<AsyncResult<AccountListResult>> resultHandler);

    public void findChildsRecursively(String accountId, Handler<AsyncResult<AccountListResult>> resultHandler);

    public void create(AccountCreator creator, Handler<AsyncResult<Account>> resultHandler);

    public void find(String scopeId, String entityId, Handler<AsyncResult<Account>> resultHandler);

    public void count(KapuaQuery query, Handler<AsyncResult<Long>> resultHandler) ;

    public void delete(String scopeId, String entityId, Handler<AsyncResult<Void>> resultHandler);

    public void update(Account entity, Handler<AsyncResult<Account>> resultHandler);

    public void findByName(String name, Handler<AsyncResult<Account>> resultHandler);

    public void getConfigMetadata(Handler<AsyncResult<KapuaTocd>> resultHandler);

    public void getConfigValues(String scopeId, Handler<AsyncResult<Map<String, Object>>> resultHandler);

    public void setConfigValues(String scopeId, String parentId, Map<String, Object> values, Handler<AsyncResult<Void>> resultHandler) ;
}
