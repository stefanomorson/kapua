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
package org.eclipse.kapua.auth.service.proxy.rest.authentication;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.rest.api.KapuaSerializableBodyReader;
import org.eclipse.kapua.commons.rest.api.KapuaSerializableBodyWriter;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.RefreshTokenCredentials;
import org.eclipse.kapua.service.authentication.SessionCredentials;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfo;

/**
 * {@link AccessInfo} REST API resource.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class AuthenticationServiceRestProxy implements AuthenticationService {

    // TODO search the registry and get the endpoint to connect to
    // as well as the mount point
    private final int port = Integer.parseInt(System.getProperty("port", "8183"));
    private final String host = System.getProperty("host", "localhost");

    private final CredentialsFactory credentialsFactory = KapuaLocator.getInstance().getFactory(CredentialsFactory.class);
    private final AccessTokenFactory accessTokenFactory = KapuaLocator.getInstance().getFactory(AccessTokenFactory.class);
    private final KapuaIdFactory kapuaIdFactory = KapuaLocator.getInstance().getFactory(KapuaIdFactory.class);

    private Client client;

    public AuthenticationServiceRestProxy() {
    }

    @Override
    public AccessToken login(LoginCredentials loginCredentials) throws KapuaException {

        if (loginCredentials == null) {
            throw KapuaException.internalError("Invalid login credentials.");
        }

        AccessToken accessToken = null;
        String path = null;
        if (loginCredentials instanceof UsernamePasswordCredentials) {
            path = String.format("/auhentication/user");
        } else if (loginCredentials instanceof ApiKeyCredentials) {
            path = String.format("/auhentication/apikey");
        } else if (loginCredentials instanceof JwtCredentials) {
            path = String.format("/auhentication/jwt");
        } else {
            throw KapuaException.internalError("Credential type not supported : " + loginCredentials.getClass().getName());
        }

        accessToken = this.doLogin(loginCredentials, path);
        return accessToken;
    }

    @Override
    public void authenticate(SessionCredentials sessionCredentials) throws KapuaException {

        if (KapuaSecurityUtils.getSession() != null) {
            return;
        }

        AccessToken accessToken = null;
        KapuaId scopeId = kapuaIdFactory.newKapuaId("AQ");
        KapuaId userId = kapuaIdFactory.newKapuaId("AQ");

        if (sessionCredentials instanceof AccessTokenCredentialsImpl) {
            accessToken = accessTokenFactory.newEntity(null);
            accessToken.setTokenId(((AccessTokenCredentialsImpl) sessionCredentials).getTokenId());
        } else {
            throw KapuaException.internalError("Invalid credentials provided");
        }
        //TODO pass scopeId and userId throug session credentials
        KapuaSession session = new KapuaSession(accessToken, scopeId, userId);
        KapuaSecurityUtils.setSession(session);
        return;
    }

    @Override
    public void logout() throws KapuaException {

        Client client = getClient();

        String path = String.format("/auhentication/logout");

        Response response = null;
        try {
            WebTarget target = client.target(new URL("http", host, port, path).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.entity("", MediaType.APPLICATION_JSON));
            checkResponse(response);
        } catch (MalformedURLException | URISyntaxException e) {
            throw KapuaException.internalError(e);
        } finally {
            response.close();
        }
    }

    @Override
    public AccessToken findAccessToken(String tokenId) throws KapuaException {
        // TODO Auto-generated method stub
        throw KapuaException.internalError("Implementation in progress.");
    }

    @Override
    public AccessToken refreshAccessToken(String tokenId, String refreshToken) throws KapuaException {

        Client client = getClient();

        String path = String.format("/auhentication/refresh");

        RefreshTokenCredentials refreshCreds = credentialsFactory.newRefreshTokenCredentials(tokenId, refreshToken);

        Response response = null;
        try {
            WebTarget target = client.target(new URL("http", host, port, path).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(refreshCreds, MediaType.APPLICATION_JSON));
            checkResponse(response);
            AccessToken result = response.readEntity(AccessToken.class);
            return result;
        } catch (MalformedURLException | URISyntaxException e) {
            throw KapuaException.internalError(e);
        } finally {
            response.close();
        }
    }

    @Override
    public boolean verifyCredentials(LoginCredentials loginCredentials) throws KapuaException {
        // TODO Auto-generated method stub
        throw KapuaException.internalError("Implementation in progress.");
    }

    private AccessToken doLogin(LoginCredentials loginCredentials, String path) throws KapuaException {

        Response response = null;
        try {
            WebTarget target = client.target(new URL("http", host, port, path).toURI());
            response = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(loginCredentials, MediaType.APPLICATION_JSON));
            checkResponse(response);
            AccessToken result = response.readEntity(AccessToken.class);
            return result;
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
