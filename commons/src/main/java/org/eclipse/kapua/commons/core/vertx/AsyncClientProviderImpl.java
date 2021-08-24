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
package org.eclipse.kapua.commons.core.vertx;

import javax.inject.Inject;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class AsyncClientProviderImpl implements AsyncClientProvider {

    @Inject
    private Vertx vertx;

    public WebClient create() {
        return WebClient.create(vertx);
    }

    public WebClient create(WebClientOptions options) {
        return WebClient.create(vertx, options);
    }

    @Override
    public AsyncClient get() {
        // TODO Auto-generated method stub
        return null;
    }
}
