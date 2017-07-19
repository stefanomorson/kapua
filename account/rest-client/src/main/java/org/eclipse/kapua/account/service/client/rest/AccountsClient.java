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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;

public class AccountsClient {

    // TODO search the registry and get the endpoint to connect to
    // as well as the mount point
    private static final int PROXY_PORT = 8182;
    private static final String PROXY_HOST = "localhost";

    private Client client;

    public AccountsClient(Client client) {
        this.client = client;
    }

    // TODO Should throw an exception other than Kapua exception ...
    public Account find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        final String file = String.format("/%s/accounts/%s",scopeId.toCompactId(), entityId.toCompactId());

        Response response = null;
        try {
            WebTarget target = client.target(new URL("http", PROXY_HOST, PROXY_PORT, file).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    //TODO Production code must enable the following and comment the next
                    //.header(HttpHeaders.AUTHORIZATION, KapuaSecurityUtils.getSession().getAccessToken().getTokenId());
                    .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL3d3dy5lY2xpcHNlLm9yZy9rYXB1YSIsImlhdCI6MTUwMDM2OTUzMiwiZXhwIjoxNTAwMzcxMzMyLCJzdWIiOiJBUSIsInNJZCI6IkFRIn0.Iuvw6FL1Z6YXir1sJckSd1nMlcEyGjShlPbr7huyocNIhzd_vZYOGg_PDi2R971jpHy9GjwDxAcnOU3_XiXZxwcjoJRIqLlrQoXZuPx0gyr3g0PBXGeekNnK5SGDLA65s-Iec0a0ULG5n4xmHY89CbpWWJBQyPBEC6YnyYns5-GolqAc-iSeCitXrsFdYLrQkziet1tTcjSk_bMfZKMtDWQDdsuuZmuO8i8ae5cmhL1380p-1_4EspI6NgEXnxaWJ_jw7jc8pap-WuZ78-JEgvHLkODUnkozhLjKgpgpiLUmR6nq-01puMu3EXZsbuS3zWU1b8CbnifL_Ay4IFwhVw")
                    .get();
            Account account = response.readEntity(Account.class);
            return account;
        } catch (MalformedURLException | URISyntaxException e) {
            throw KapuaException.internalError(e);
        } finally {
            response.close();
        }

    }
}
