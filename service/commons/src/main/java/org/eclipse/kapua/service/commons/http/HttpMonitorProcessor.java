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

public interface HttpMonitorProcessor extends ServiceProcessor<HttpMonitorProcessor, HttpMonitorConfiguration, RoutingContext>, Handler<RoutingContext> {

    public HttpMonitorConfiguration getConfiguration();

    public static HttpMonitorProcessor create(Vertx aVertx) {
        return new HttpMonitorProcessorImpl(aVertx);
    }

    public static HttpMonitorProcessor create(Vertx aVertx, HttpMonitorConfiguration aConfig) {
        return new HttpMonitorProcessorImpl(aVertx, aConfig);
    }
}
