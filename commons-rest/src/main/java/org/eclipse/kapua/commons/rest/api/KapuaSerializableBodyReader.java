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
package org.eclipse.kapua.commons.rest.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

@Provider
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class KapuaSerializableBodyReader implements MessageBodyReader<Object> {

    private static final String PROP_ECLIPSE_LINK_MEDIA_TYPE = "eclipselink.media-type";
    private static final String PROP_ECLIPSE_LINK_JSON_INCLUDE_ROOT = "eclipselink.json.include-root";
    private static final String PROP_JAXB_CHARSET = "charset";

    @Context
    Providers providers;

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        // TODO any ode here to do more significant check on the types ?
        return true;
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, 
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        try {
            if (providers == null) {
                throw new WebApplicationException("Unable to find any provider.");
            }

            ContextResolver<JAXBContext> cr = providers.getContextResolver(JAXBContext.class, mediaType);
            JAXBContext jaxbContext = cr.getContext(JAXBContext.class);
            if (jaxbContext == null) {
                throw new WebApplicationException("Unable to get a JAXBContext.");
            }

            Unmarshaller um = jaxbContext.createUnmarshaller();
            if (mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
                um.setProperty(PROP_ECLIPSE_LINK_MEDIA_TYPE, MediaType.APPLICATION_JSON);
                um.setProperty(PROP_ECLIPSE_LINK_JSON_INCLUDE_ROOT, false);
            }
            StreamSource jsonStream = null;
            Map<String, String> parameters = mediaType.getParameters();
            if (parameters.containsKey(PROP_JAXB_CHARSET)) {
                String charset = parameters.get(PROP_JAXB_CHARSET);
                InputStreamReader reader = new InputStreamReader(entityStream, charset);
                jsonStream = new StreamSource(reader);
            } else {
                jsonStream = new StreamSource(entityStream);
            }
            return um.unmarshal(jsonStream, type).getValue();
        } catch (JAXBException e) {
            throw new WebApplicationException(e);
        }
    }
}
