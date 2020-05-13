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

import org.eclipse.kapua.service.commons.ContainerBuilder;

import io.vertx.core.Vertx;

public interface HttpMonitorContainerBuilder extends ContainerBuilder<HttpMonitorContainerBuilder, HttpMonitorContainer, HttpMonitorContainerOptions> {

    public HttpMonitorContainerBuilder processor(HttpMonitorProcessor processor);

    public static HttpMonitorContainerBuilder builder(Vertx vertx) {
        return new HttpMonitorContainerImpl.Builder(vertx);
    }

    public static HttpMonitorContainerBuilder builder(Vertx vertx, HttpMonitorContainerOptions aConfig) {
        return new HttpMonitorContainerImpl.Builder(vertx, aConfig);
    }
}