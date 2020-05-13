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
package org.eclipse.kapua.service.job.engine.app;

import javax.validation.constraints.NotNull;

import org.eclipse.kapua.service.commons.app.BaseContext;
import org.eclipse.kapua.service.commons.app.Configuration;
import org.eclipse.kapua.service.commons.app.InitContext;
import org.springframework.beans.factory.annotation.Autowired;

import io.vertx.core.Vertx;


public class JobEngineContext extends BaseContext {

    @Autowired
    public JobEngineContext(@NotNull Vertx aVertx, @NotNull InitContext<? extends Configuration> aInitContext) {
        super(aVertx, aInitContext);
        // TODO Auto-generated constructor stub
    }
}
