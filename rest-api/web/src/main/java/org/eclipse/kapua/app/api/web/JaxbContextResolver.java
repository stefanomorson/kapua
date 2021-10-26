/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.web;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.eclipse.kapua.app.api.core.AbstractJaxbContextResolver;
import org.eclipse.kapua.commons.core.ClassProvider;

/**
 * Provides a customized JAXBContext that makes the XML bindings known and available
 * to the jax-rs implementation for marshalling/unmarshalling to/from XML format. 
 * When the JAXB context is provided to jax-rs with this resolver, all classes that
 * need to be serialized by jax-rs, in any format (not only XML) must be passed to the
 * factory in the constructor here.
 *
 * @since 1.0.0
 */
@Provider
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class JaxbContextResolver extends AbstractJaxbContextResolver {

    public JaxbContextResolver(ClassProvider ... providers) {
        super(providers);
    }
}
