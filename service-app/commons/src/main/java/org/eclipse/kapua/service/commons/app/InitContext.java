/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.commons.app;

import java.util.Set;

import org.eclipse.kapua.service.commons.ContainerBuilder;
import org.eclipse.kapua.service.commons.ContainerBuilders;
import org.eclipse.kapua.service.commons.http.HttpMonitorContainerBuilder;
import org.springframework.beans.factory.ObjectFactory;

import io.vertx.core.Vertx;

/**
 * This class defines the initialization context of a Vertx based application
 *
 * @param <C> the configuration class associated with your own Vertx based application
 */
public interface InitContext<C extends Configuration> {

    public Vertx getVertx();

    public C getConfig();

    public HttpMonitorContainerBuilder getMonitorContainerBuilder();

    public Set<ObjectFactory<ContainerBuilder<?, ?, ?>>> getContainerBuilderFactories();

    public ContainerBuilders getContainerBuilders();
}
