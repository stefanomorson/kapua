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
package org.eclipse.kapua.auth.service.rest;

import java.security.acl.Permission;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import org.eclipse.kapua.commons.rest.api.exception.model.EntityNotFoundExceptionInfo;
import org.eclipse.kapua.commons.rest.api.exception.model.IllegalArgumentExceptionInfo;
import org.eclipse.kapua.commons.rest.api.exception.model.IllegalNullArgumentExceptionInfo;
import org.eclipse.kapua.commons.rest.api.exception.model.ThrowableInfo;
import org.eclipse.kapua.commons.rest.api.v1.resources.model.CountResult;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.model.config.metatype.MetatypeXmlRegistry;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationCredentials;
import org.eclipse.kapua.service.authentication.AuthenticationXmlRegistry;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.RefreshTokenCredentials;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.CredentialXmlRegistry;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionQuery;
import org.eclipse.kapua.service.authorization.role.RolePermissionXmlRegistry;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleXmlRegistry;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

/**
 * Provide a customized JAXBContext that makes the concrete implementations
 * known and available for marshalling
 *
 * @since 1.0.0
 */
@Provider
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class JaxbContextResolver implements ContextResolver<JAXBContext> {

    private JAXBContext jaxbContext;

    public JaxbContextResolver() {
        try {
            jaxbContext = JAXBContextFactory.createContext(new Class[] {
                    // REST API utility models
                    CountResult.class,

                    // REST API exception models
                    ThrowableInfo.class,
                    EntityNotFoundExceptionInfo.class,
                    IllegalArgumentExceptionInfo.class,
                    IllegalNullArgumentExceptionInfo.class,

                    // Tocds
                    KapuaTocd.class,
                    KapuaTad.class,
                    KapuaTicon.class,
                    KapuaTmetadata.class,
                    KapuaToption.class,
                    MetatypeXmlRegistry.class,

                    AuthenticationCredentials.class,
                    AuthenticationXmlRegistry.class,
                    AccessToken.class,
                    ApiKeyCredentials.class,
                    JwtCredentials.class,
                    UsernamePasswordCredentials.class,
                    RefreshTokenCredentials.class,

                    // Credential
                    Credential.class,
                    CredentialListResult.class,
                    CredentialCreator.class,
                    CredentialType.class,
                    CredentialQuery.class,
                    CredentialXmlRegistry.class,

                    // Permission
                    Permission.class,

                    // Roles
                    Role.class,
                    RoleListResult.class,
                    RoleCreator.class,
                    RoleQuery.class,
                    RoleXmlRegistry.class,

                    // Role Permissions
                    RolePermission.class,
                    RolePermissionListResult.class,
                    RolePermissionCreator.class,
                    RolePermissionQuery.class,
                    RolePermissionXmlRegistry.class

            }, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JAXBContext getContext(Class<?> type) {
        return jaxbContext;
    }

}
