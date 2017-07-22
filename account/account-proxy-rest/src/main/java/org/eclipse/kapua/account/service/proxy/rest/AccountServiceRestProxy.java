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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.rest.api.KapuaSerializableBodyReader;
import org.eclipse.kapua.commons.rest.api.KapuaSerializableBodyWriter;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
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
public class AccountServiceRestProxy implements AccountService {

    // TODO search the registry and get the endpoint to connect to
    // as well as the mount point
    private final int port = Integer.parseInt(System.getProperty("port", "8182"));
    private final String host = System.getProperty("host", "localhost");

    private Client client;

    public AccountServiceRestProxy() {
        this.client = ClientBuilder.newClient()
                .register(JaxbContextResolver.class)
                .register(KapuaSerializableBodyReader.class)
                .register(KapuaSerializableBodyWriter.class);
    }

    public static void main(String[] args) throws KapuaException {
    }

    @Override
    public Account find(KapuaId id) throws KapuaException {
        // TODO Auto-generated method stub
        throw KapuaException.internalError("Not implemented yet");
    }

    @Override
    public AccountListResult query(KapuaQuery<Account> query) throws KapuaException {

        final String file = String.format("/_/accounts/_query");

        Response response = null;
        try {
            WebTarget target = client.target(new URL("http", host, port, file).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, KapuaSecurityUtils.getSession().getAccessToken().getTokenId())
                    .post(Entity.entity(query, MediaType.APPLICATION_JSON));
            AccountListResult result = response.readEntity(AccountListResult.class);
            return result;
        } catch (MalformedURLException | URISyntaxException e) {
            throw KapuaException.internalError(e);
        } finally {
            response.close();
        }
    }

    @Override
    public AccountListResult findChildsRecursively(KapuaId accountId) throws KapuaException {
        // TODO Auto-generated method stub
        throw KapuaException.internalError("Not implemented yet");
    }

    @Override
    public Account create(AccountCreator creator) throws KapuaException {

        final String file = String.format("/_/accounts/");

        Response response = null;
        try {
            WebTarget target = client.target(new URL("http", host, port, file).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, KapuaSecurityUtils.getSession().getAccessToken().getTokenId())
                    .post(Entity.entity(creator, MediaType.APPLICATION_JSON));
            Account result = response.readEntity(Account.class);
            return result;
        } catch (MalformedURLException | URISyntaxException e) {
            throw KapuaException.internalError(e);
        } finally {
            response.close();
        }
    }

    @Override
    public Account find(KapuaId scopeId, KapuaId entityId) throws KapuaException {

        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "entityId");

        final String file = String.format("/%s/accounts/%s",scopeId.toCompactId(), entityId.toCompactId());

        Response response = null;
        try {
            WebTarget target = client.target(new URL("http", host, port, file).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL3d3dy5lY2xpcHNlLm9yZy9rYXB1YSIsImlhdCI6MTUwMDcxNDA1OCwiZXhwIjoxNTAwNzE1ODU4LCJzdWIiOiJBUSIsInNJZCI6IkFRIn0.d2j_EVJIh301EA6Eu-w62WWFJVYuth2BElSruO1jIgbSpKNmkN67RyB1e9YgnheLsDhdqxg553IApoy_QIo8ZTDVclFwyACLOnH-mRmPqWJitbCZb3vRB8j7g_mFvc7fbD8mAuPS33X_YC9Vn9Bcm62ZcvgzfFwD889dbTB2gLfSgaQRmrKupp1haaLg761IHon19_pjuU-_gBH0pPZ5kUBv4aRTzpnE8l_3hceUaSwnlTyNYjqbQxKipgZ74P2kEeHZLDz_BhhzF-rd-K41iaXG8E6J-rEIUsGG856WLDGt1z8YjgDJXyKVV7XsDCaFLl3N-uKeu2iZm-GCWXF-kg")
                    //.header(HttpHeaders.AUTHORIZATION, KapuaSecurityUtils.getSession().getAccessToken().getTokenId())
                    .get();
            Account account = response.readEntity(Account.class);
            return account;
        } catch (MalformedURLException | URISyntaxException e) {
            throw KapuaException.internalError(e);
        } finally {
            response.close();
        }
    }

    @Override
    public long count(KapuaQuery<Account> query) throws KapuaException {

        final String file = String.format("/_/accounts/_count");

        Response response = null;
        try {
            WebTarget target = client.target(new URL("http", host, port, file).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, KapuaSecurityUtils.getSession().getAccessToken().getTokenId())
                    .post(Entity.entity(query, MediaType.APPLICATION_JSON));
            long count = response.readEntity(Long.class);
            return count;
        } catch (MalformedURLException | URISyntaxException e) {
            throw KapuaException.internalError(e);
        } finally {
            response.close();
        }
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {

        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "entityId");

        final String file = String.format("/%s/accounts/%s", scopeId.toCompactId(), entityId.toCompactId());

        Response response = null;
        try {
            WebTarget target = client.target(new URL("http", host, port, file).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, KapuaSecurityUtils.getSession().getAccessToken().getTokenId())
                    .delete();
            return;
        } catch (MalformedURLException | URISyntaxException e) {
            throw KapuaException.internalError(e);
        } finally {
            response.close();
        }
    }

    @Override
    public Account update(Account entity) throws KapuaException {

        final String file = String.format("/_/accounts/");

        Response response = null;
        try {
            WebTarget target = client.target(new URL("http", host, port, file).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, KapuaSecurityUtils.getSession().getAccessToken().getTokenId())
                    .put(Entity.entity(entity, MediaType.APPLICATION_JSON));
            Account result = response.readEntity(Account.class);
            return result;
        } catch (MalformedURLException | URISyntaxException e) {
            throw KapuaException.internalError(e);
        } finally {
            response.close();
        }
    }

    @Override
    public Account findByName(String name) throws KapuaException {
        // TODO Auto-generated method stub
        throw KapuaException.internalError("Not implemented yet");
    }

    @Override
    public KapuaTocd getConfigMetadata() throws KapuaException {
        // TODO Auto-generated method stub
        throw KapuaException.internalError("Not implemented yet");
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException {
        // TODO Auto-generated method stub
        throw KapuaException.internalError("Not implemented yet");
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) throws KapuaException {
        throw KapuaException.internalError("Not implemented yet");
    }
}
