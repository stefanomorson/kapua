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
package org.eclipse.kapua.account.service.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.rest.api.AuthenticatedSessionExecutor;
import org.eclipse.kapua.commons.rest.api.v1.resources.AbstractKapuaResource;
import org.eclipse.kapua.commons.rest.api.v1.resources.model.CountResult;
import org.eclipse.kapua.commons.rest.api.v1.resources.model.EntityId;
import org.eclipse.kapua.commons.rest.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountPredicates;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.AccountService;

import com.google.common.base.Strings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.vertx.core.Vertx;

@Api("Accounts")
@Path("/{scopeId}/accounts")
public class AccountsResource extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccountService accountService = locator.getService(AccountService.class);
    private final AccountFactory accountFactory = locator.getFactory(AccountFactory.class);

    /**
     * Gets the {@link Account} list in the scope.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param name
     *            The {@link Account} name in which to search results.
     * @param offset
     *            The result set offset.
     * @param limit
     *            The result set limit.
     * @return The {@link AccountListResult} of all the accounts associated to the current selected scope.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the Account list in the scope", //
            notes = "Returns the list of all the accounts associated to the current selected scope.", //
            response = Account.class, //
            responseContainer = "AccountListResult") //
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void simpleQuery( //
            @Context Vertx vertx,
            @Context HttpHeaders httpHeaders,
            @Suspended final AsyncResponse asyncResponse,
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The account name to filter results.") @QueryParam("name") String name, //
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset, //
            @ApiParam(value = "The result set limit.", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {

        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        new AuthenticatedSessionExecutor(vertx, headers).exec(
                future -> {
                    try {
                        System.out.println("Thread id: " + Thread.currentThread().getId());
                        AccountQuery query = accountFactory.newQuery(scopeId);

                        AndPredicate andPredicate = new AndPredicate();
                        if (!Strings.isNullOrEmpty(name)) {
                            andPredicate.and(new AttributePredicate<>(AccountPredicates.NAME, name));
                        }
                        query.setPredicate(andPredicate);

                        query.setOffset(offset);
                        query.setLimit(limit);
                        AccountListResult result = accountService.query(query);
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
     * Queries the results with the given {@link AccountQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link AccountQuery} to use to filter results.
     * @return The {@link AccountListResult} of all the result matching the given {@link AccountQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Queries the Accounts", //
            notes = "Queries the Accounts with the given AccountQuery parameter returning all matching Accounts", //
            response = Account.class, //
            responseContainer = "AccountListResult") //
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void query(
            @Context Vertx vertx,
            @Context HttpHeaders httpHeaders,
            @Suspended final AsyncResponse asyncResponse,
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The AccountQuery to use to filter results.", required = true) AccountQuery query) throws Exception {

        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        new AuthenticatedSessionExecutor(vertx, headers).exec(
                future -> {
                    try {
                        System.out.println("Thread id: " + Thread.currentThread().getId());
                        query.setScopeId(scopeId);
                        AccountListResult result = accountService.query(query);
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
     * Counts the results with the given {@link AccountQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to count results.
     * @param query
     *            The {@link AccountQuery} to use to filter results.
     * @return The count of all the result matching the given {@link AccountQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Counts the Accounts", //
            notes = "Counts the Accounts with the given AccountQuery parameter returning the number of matching Accounts", //
            response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void count(
            @Context Vertx vertx,
            @Context HttpHeaders httpHeaders,
            @Suspended final AsyncResponse asyncResponse,
            @ApiParam(value = "The ScopeId in which to count results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The AccountQuery to use to filter count results", required = true) AccountQuery query) throws Exception {

        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        new AuthenticatedSessionExecutor(vertx, headers).exec(
                future -> {
                    try {
                        query.setScopeId(scopeId);
                        CountResult result = new CountResult(accountService.count(query));
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
     * Creates a new Account based on the information provided in AccountCreator
     * parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to create the {@link Account}
     * @param accountCreator
     *            Provides the information for the new {@link Account} to be created.
     * @return The newly created {@link Account} object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Create an Account",  //
            notes = "Creates a new Account based on the information provided in AccountCreator parameter.",  //
            response = Account.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void create(
            @Context Vertx vertx,
            @Context HttpHeaders httpHeaders,
            @Suspended final AsyncResponse asyncResponse,
            @ApiParam(value = "The ScopeId in which to create the Account", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "Provides the information for the new Account to be created", required = true) AccountCreator accountCreator) throws Exception {

        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        new AuthenticatedSessionExecutor(vertx, headers).exec(
            future -> {
                try {
                    accountCreator.setScopeId(scopeId);
                    Account account = accountService.create(accountCreator);
                    future.complete(account);
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
     * Returns the Account specified by the "accountId" path parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the requested {@link Account}.
     * @param accountId
     *            The id of the requested Account.
     * @return The requested Account object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Get an Account",  //
            notes = "Returns the Account specified by the \"accountId\" path parameter.",  //
            response = Account.class)
    @GET
    @Path("{accountId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void find(
            @Context Vertx vertx,
            @Context HttpHeaders httpHeaders,
            @Suspended final AsyncResponse asyncResponse,
            @ApiParam(value = "The ScopeId of the requested Account.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The id of the requested Account", required = true) @PathParam("accountId") EntityId accountId) throws Exception {

        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        new AuthenticatedSessionExecutor(vertx, headers).exec(
            future -> {
                try {
                    Account account = accountService.find(scopeId, accountId);
                    if (account == null) {
                        throw new KapuaEntityNotFoundException(Account.TYPE, accountId);
                    }
                    future.complete(account);
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
     * Updates the Account based on the information provided in the Account parameter.
     *
     * @param scopeId
     *            The ScopeId of the requested {@link Account}.
     * @param accountId
     *            The id of the requested {@link Account}
     * @param account
     *            The modified Account whose attributed need to be updated.
     * @return The updated account.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Update an Account",  //
            notes = "Updates a new Account based on the information provided in the Account parameter.",  //
            response = Account.class)
    @PUT
    @Path("{accountId}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public void update(
            @Context Vertx vertx,
            @Context HttpHeaders httpHeaders,
            @Suspended final AsyncResponse asyncResponse,
            @ApiParam(value = "The ScopeId of the requested Account.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The id of the requested Account", required = true) @PathParam("accountId") EntityId accountId, //
            @ApiParam(value = "The modified Account whose attributes needs to be updated", required = true) Account account) throws Exception {
        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        new AuthenticatedSessionExecutor(vertx, headers).exec(
            future -> {
                try {
                    account.setScopeId(scopeId);
                    account.setId(accountId);

                    Account result = accountService.update(account);
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
     * Deletes the Account specified by the "accountId" path parameter.
     *
     * @param scopeId
     *            The ScopeId of the requested {@link Account}.
     * @param accountId
     *            The id of the Account to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Delete an Account",  //
            notes = "Deletes the Account specified by the \"accountId\" path parameter.")
    @DELETE
    @Path("{accountId}")
    public void deleteAccount(
            @Context Vertx vertx,
            @Context HttpHeaders httpHeaders,
            @Suspended final AsyncResponse asyncResponse,
            @ApiParam(value = "The ScopeId of the Account to delete.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The id of the Account to be deleted", required = true) @PathParam("accountId") EntityId accountId) throws Exception {

        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        new AuthenticatedSessionExecutor(vertx, headers).exec(
            future -> {
                try {
                    accountService.delete(scopeId, accountId);
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

}
