/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.account.server;

import javax.inject.Named;

import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.ObjectContextConfig;
import org.eclipse.kapua.commons.core.vertx.RestServiceConfig;
import org.eclipse.kapua.commons.core.vertx.RestServiceVerticle;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;

import com.google.inject.Provides;

public class AccountContextConfig extends ObjectContextConfig {

    @Override
    protected void configure() {
        super.configure();
        bind(MainVerticle.class);
        bind(JAXBContextProvider.class).to(JAXBContextProviderImpl.class);
        bind(EventBusAccountServiceVerticle.class);
        bind(RestServiceVerticle.class);
    }

    @Provides
    @Named("kapua.restService")
    RestServiceConfig provideHttpServiceImplConfig(Configuration config) {
        return RestServiceConfig.create("kapua.restService", config);
    }
}
