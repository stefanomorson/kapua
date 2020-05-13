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
package org.eclipse.kapua.service.commons;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * This class represents a container verticle. A container verticle encapsulates a
 * container and starts/stops it when the verticle is deployed/undeployed.
 *
 */
public class ContainerVerticle extends AbstractVerticle {

    private Container container;

    private ContainerVerticle() {}

    public ContainerVerticle setContainer(Container aContainer) {
        container = aContainer;
        return this;
    }

    public void start(Future<Void> startFuture) throws Exception {
        container.start(startFuture);
    }

    public void stop(Future<Void> stopFuture) throws Exception {
        container.stop(stopFuture);
    }

    public static ContainerVerticle create() {
        return new ContainerVerticle();
    }
}
