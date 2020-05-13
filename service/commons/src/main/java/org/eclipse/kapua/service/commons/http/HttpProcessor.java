/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.commons.http;

import org.eclipse.kapua.service.commons.ServiceProcessor;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

public interface HttpProcessor extends ServiceProcessor<HttpProcessor, HttpConfiguration, RoutingContext>, Handler<RoutingContext> {

    public HttpConfiguration getConfiguration();

    static HttpProcessor create(Vertx vertx) {
        return new HttpProcessorImpl(vertx);
    }
}
