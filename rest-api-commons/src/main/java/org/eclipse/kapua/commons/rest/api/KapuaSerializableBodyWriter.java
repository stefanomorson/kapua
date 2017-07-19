/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.rest.api;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.kapua.KapuaSerializable;

@Provider
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class KapuaSerializableBodyWriter implements MessageBodyWriter<KapuaSerializable> {

    private static final String PROP_ECLIPSE_LINK_MEDIA_TYPE = "eclipselink.media-type";
    private static final String PROP_ECLIPSE_LINK_JSON_INCLUDE_ROOT = "eclipselink.json.include-root";
    private static final String PROP_JAXB_CHARSET = "charset";

    @Context
    Providers providers;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        // TODO any ode here to do more significant check on the types ?
        return true;
    }

    @Override
    public long getSize(KapuaSerializable t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(KapuaSerializable t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        try {
            if (providers == null) {
                throw new WebApplicationException("Unable to find any provider.");
            }

            ContextResolver<JAXBContext> cr = providers.getContextResolver(JAXBContext.class, mediaType);
            JAXBContext jaxbContext = cr.getContext(JAXBContext.class);
            if (jaxbContext == null) {
                throw new WebApplicationException("Unable to get a JAXBContext.");
            }

            // serialize the entity myBean to the entity output stream
            Marshaller m = jaxbContext.createMarshaller();
            if (mediaType.equals(MediaType.APPLICATION_JSON_TYPE)) {
                m.setProperty(PROP_ECLIPSE_LINK_MEDIA_TYPE, MediaType.APPLICATION_JSON);
                m.setProperty(PROP_ECLIPSE_LINK_JSON_INCLUDE_ROOT, false);
            }

            Map<String, String> parameters = mediaType.getParameters();
            if (parameters.containsKey(PROP_JAXB_CHARSET)) {
                String charSet = parameters.get(PROP_JAXB_CHARSET);
                m.setProperty(Marshaller.JAXB_ENCODING, charSet);
            }
            m.marshal(t, entityStream);
        } catch (JAXBException e) {
            throw new WebApplicationException(e);
        }
    }

}
