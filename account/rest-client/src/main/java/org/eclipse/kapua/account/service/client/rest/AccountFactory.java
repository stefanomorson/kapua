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
package org.eclipse.kapua.account.service.client.rest;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.Organization;

@KapuaProvider
public class AccountFactory implements org.eclipse.kapua.service.account.AccountFactory {

    @Override
    public AccountCreator newCreator(KapuaId scopeId) {
        return new org.eclipse.kapua.account.service.client.rest.AccountCreator(scopeId, null);
    }

    @Override
    public AccountCreator newCreator(KapuaId scopeId, String name) {
        AccountCreator creator = newCreator(scopeId);
        creator.setName(name);
        return creator;
    }

    @Override
    public Account newEntity(KapuaId scopeId) {
        return new org.eclipse.kapua.account.service.client.rest.Account(scopeId);
    }

    @Override
    public Organization newOrganization() {
        return new org.eclipse.kapua.account.service.client.rest.Organization();
    }

    @Override
    public AccountQuery newQuery(KapuaId scopeId) {
        return new org.eclipse.kapua.account.service.client.rest.AccountQuery(scopeId);
    }

    @Override
    public AccountListResult newListResult() {
        return new org.eclipse.kapua.account.service.client.rest.AccountListResult();
    }
}
