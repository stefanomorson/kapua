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

import io.vertx.core.Vertx;

public class RestService extends AbstractHttpService {

    protected RestService(Vertx aVertx, HttpServiceConfig aConfig) {
        super(aVertx, aConfig);
    }

    public static RestService create(Vertx aVertx, HttpServiceConfig aConfig) {
        return new RestService(aVertx, aConfig);
    }
}
