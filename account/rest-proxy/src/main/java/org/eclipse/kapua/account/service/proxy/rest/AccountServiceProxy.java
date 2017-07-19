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
package org.eclipse.kapua.account.service.proxy.rest;

import java.util.Map;

import javax.ws.rs.client.ClientBuilder;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.account.service.client.rest.AccountsClient;
import org.eclipse.kapua.account.service.commons.jaxb.JaxbContextResolver;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.rest.api.KapuaSerializableBodyReader;
import org.eclipse.kapua.commons.rest.api.KapuaSerializableBodyWriter;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.access.AccessInfo;

/**
 * {@link AccessInfo} REST API resource.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class AccountServiceProxy implements AccountService {

    private AccountsClient client;

    public AccountServiceProxy() {
        this.client = new AccountsClient(ClientBuilder.newClient()
                .register(JaxbContextResolver.class)
                .register(KapuaSerializableBodyReader.class)
                .register(KapuaSerializableBodyWriter.class));
    }

    public static void main(String[] args) throws KapuaException {

        AccountServiceProxy proxy = new AccountServiceProxy();
        Account account = proxy.find(KapuaEid.parseCompactId("AQ"), KapuaEid.parseCompactId("AQ"));
        System.out.println(account);
    }

    @Override
    public Account find(KapuaId id) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccountListResult query(KapuaQuery<Account> query) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccountListResult findChildsRecursively(KapuaId accountId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Account create(AccountCreator creator) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Account find(KapuaId scopeId, KapuaId entityId) throws KapuaException {

        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "entityId");
        return client.find(scopeId, entityId);
    }

    @Override
    public long count(KapuaQuery<Account> query) throws KapuaException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
    }

    @Override
    public Account update(Account entity) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Account findByName(String name) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KapuaTocd getConfigMetadata() throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) throws KapuaException {
    }
}
