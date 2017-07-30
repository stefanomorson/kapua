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
package org.eclipse.kapua.auth.service.uservice.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.rest.api.AuthenticatedSessionExecutor;
import org.eclipse.kapua.commons.rest.api.v1.resources.AbstractKapuaResource;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.RefreshTokenCredentials;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.token.AccessToken;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.vertx.core.Vertx;

@Api("Authentication")
@Path("/authentication")
public class AuthenticationResource extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AuthenticationService authenticationService = locator.getService(AuthenticationService.class);

    /**
     * Authenticates an user with username and password and returns
     * the authentication token to be used in subsequent REST API calls.
     *
     * @param authenticationCredentials
     *            The username and password authentication credential of a user.
     * @return The authentication token
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Authenticate an user", notes = "Authenticates an user with username and password and returns " +
            "the authentication token to be used in subsequent REST API calls.", response = AccessToken.class)
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("user")
    public void loginUsernamePassword(
            @Context Vertx vertx,
            @Context HttpHeaders httpHeaders,
            @Suspended final AsyncResponse asyncResponse,
            @ApiParam(value = "The username and password authentication credential of a user.", required = true) UsernamePasswordCredentials authenticationCredentials) throws Exception {
//        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        vertx.executeBlocking(
                future -> {
                    try {
                        AccessToken result = authenticationService.login(authenticationCredentials);

                        future.complete(result);
                    } catch (KapuaException exc) {
                        future.fail(exc);
                    }
                },
                true,
                res -> {
                    if (res.succeeded()) {
                        if (res.result() != null) {
                            asyncResponse.resume(res.result());
                        } else {
                            asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
                        }
                    } else {
                        asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
                    }
                });
    }

    /**
     * Authenticates an user with a api key and returns
     * the authentication token to be used in subsequent REST API calls.
     *
     * @param authenticationCredentials
     *            The API KEY authentication credential of a user.
     * @return The authentication token
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Authenticate an user", notes = "Authenticates an user with API KEY and returns " +
            "the authentication token to be used in subsequent REST API calls.", response = AccessToken.class)
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("apikey")
    public void loginApiKey(
            @Context Vertx vertx,
            @Context HttpHeaders httpHeaders,
            @Suspended final AsyncResponse asyncResponse,
            @ApiParam(value = "The API KEY authentication credential of a user.", required = true) ApiKeyCredentials authenticationCredentials) throws Exception {

        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        new AuthenticatedSessionExecutor(vertx, headers).exec(
                future -> {
                    try {
                        AccessToken result = authenticationService.login(authenticationCredentials);

                        future.complete(result);
                    } catch (KapuaException exc) {
                        future.fail(exc);
                    }
                },
                true,
                res -> {
                    if (res.succeeded()) {
                        if (res.result() != null) {
                            asyncResponse.resume(res.result());
                        } else {
                            asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
                        }
                    } else {
                        asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
                    }
                });
    }

    /**
     * Authenticates an user with JWT and returns
     * the authentication token to be used in subsequent REST API calls.
     *
     * @param authenticationCredentials
     *            The JWT authentication credential of a user.
     * @return The authentication token
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Authenticate an user", notes = "Authenticates an user with a JWT and returns " +
            "the authentication token to be used in subsequent REST API calls.", response = AccessToken.class)
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("jwt")
    public void loginJwt(
            @Context Vertx vertx,
            @Context HttpHeaders httpHeaders,
            @Suspended final AsyncResponse asyncResponse,
            @ApiParam(value = "The JWT authentication credential of a user.", required = true) JwtCredentials authenticationCredentials) throws Exception {

        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        new AuthenticatedSessionExecutor(vertx, headers).exec(
                future -> {
                    try {
                        AccessToken result = authenticationService.login(authenticationCredentials);

                        future.complete(result);
                    } catch (KapuaException exc) {
                        future.fail(exc);
                    }
                },
                true,
                res -> {
                    if (res.succeeded()) {
                        if (res.result() != null) {
                            asyncResponse.resume(res.result());
                        } else {
                            asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
                        }
                    } else {
                        asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
                    }
                });
    }

    /**
     * Invalidates the AccessToken related to this session.
     * All subsequent calls will end up with a HTTP 401.
     * A new login is required after this call to make other requests.
     * 
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Logs out an user", notes = "Terminates the current session and invalidates the access token")
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("logout")
    public void logout(
            @Context Vertx vertx,
            @Context HttpHeaders httpHeaders,
            @Suspended final AsyncResponse asyncResponse) throws Exception {

        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        new AuthenticatedSessionExecutor(vertx, headers).exec(
                future -> {
                    try {
                        authenticationService.logout();

                        future.complete(returnOk());
                    } catch (KapuaException exc) {
                        future.fail(exc);
                    }
                },
                true,
                res -> {
                    if (res.succeeded()) {
                        if (res.result() != null) {
                            asyncResponse.resume(res.result());
                        } else {
                            asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
                        }
                    } else {
                        asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
                    }
                });
    }

    /**
     * Refreshes an expired {@link AccessToken}. Both the current AccessToken and the Refresh token will be invalidated.
     * If also the Refresh token is expired, the user will have to restart with a new login.
     * 
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Refreshes an AccessToken", notes = "Both the current AccessToken and the Refresh token will be invalidated. "
            + "If also the Refresh token is expired, the user will have to restart with a new login.")
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("refresh")
    public void refresh(
            @Context Vertx vertx,
            @Context HttpHeaders httpHeaders,
            @Suspended final AsyncResponse asyncResponse,
            @ApiParam(value = "The current AccessToken's tokenId and refreshToken", required = true) RefreshTokenCredentials refreshTokenCredentials) throws Exception {

        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        new AuthenticatedSessionExecutor(vertx, headers).exec(
                future -> {
                    try {
                        AccessToken result = authenticationService.refreshAccessToken(refreshTokenCredentials.getTokenId(), refreshTokenCredentials.getRefreshToken());

                        future.complete(result);
                    } catch (KapuaException exc) {
                        future.fail(exc);
                    }
                },
                true,
                res -> {
                    if (res.succeeded()) {
                        if (res.result() != null) {
                            asyncResponse.resume(res.result());
                        } else {
                            asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
                        }
                    } else {
                        asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
                    }
                });
    }
}
