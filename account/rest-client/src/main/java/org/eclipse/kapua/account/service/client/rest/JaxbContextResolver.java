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
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.AccountXmlRegistry;
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

                    // Account
                    Account.class,
                    AccountCreator.class,
                    AccountListResult.class,
                    AccountQuery.class,
                    AccountXmlRegistry.class

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
