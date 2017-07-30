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
package org.eclipse.kapua.auth.service.proxy.rest.authorization;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.auth.service.proxy.rest.authentication.JaxbContextResolver;
import org.eclipse.kapua.commons.rest.api.KapuaSerializableBodyReader;
import org.eclipse.kapua.commons.rest.api.KapuaSerializableBodyWriter;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;

@KapuaProvider
public class AuthorizationServiceRestProxy implements AuthorizationService {

    // TODO search the registry and get the endpoint to connect to
    // as well as the mount point
    private final int port = Integer.parseInt(System.getProperty("auth_port", "8183"));
    private final String host = System.getProperty("auth_host", "localhost");

    private Client client;

    public AuthorizationServiceRestProxy() {
    }

    @Override
    public boolean isPermitted(Permission permission) throws KapuaException {

        Client client = getClient();

        String path = String.format("/authorization/_isPermitted");

        Response response = null;
        try {
            WebTarget target = client.target(new URL("http", host, port, path).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, KapuaSecurityUtils.getSession().getAccessToken().getTokenId())
                    .post(Entity.entity(permission, MediaType.APPLICATION_JSON));
            checkResponse(response);
            boolean result = response.readEntity(Boolean.class);
            return result;
        } catch (MalformedURLException | URISyntaxException e) {
            throw KapuaException.internalError(e);
        } finally {
            response.close();
        }
    }

    @Override
    public void checkPermission(Permission permission) throws KapuaException {

        Client client = getClient();

        String path = String.format("/authorization/_checkPermission");

        Response response = null;
        try {
            WebTarget target = client.target(new URL("http", host, port, path).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, KapuaSecurityUtils.getSession().getAccessToken().getTokenId())
                    .post(Entity.entity(permission, MediaType.APPLICATION_JSON));
            checkResponse(response);
        } catch (MalformedURLException | URISyntaxException e) {
            throw KapuaException.internalError(e);
        } finally {
            response.close();
        }
    }

    private void checkResponse(Response response) throws KapuaException {
        if (response.getStatus() != 200) {
            throw KapuaException.internalError("Error code: " + response.getStatus() + ", desc: " + response.getStatusInfo().getReasonPhrase());
        }
    }

    private Client getClient() {
        if (client == null) {
            this.client = ClientBuilder.newClient()
                    .register(JaxbContextResolver.class)
                    .register(KapuaSerializableBodyReader.class)
                    .register(KapuaSerializableBodyWriter.class);
        }
        return client;
    }
}
