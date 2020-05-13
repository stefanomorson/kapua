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
package org.eclipse.kapua.service.job.engine.app;

import org.eclipse.kapua.service.commons.app.BaseConfiguration;
import org.eclipse.kapua.service.commons.http.HttpContainerOptions;

import org.springframework.beans.factory.annotation.Autowired;

public class JobEngineConfiguration extends BaseConfiguration {

    private JobEngineHttpController controller;
    private HttpContainerOptions httpServiceConfig;

    public JobEngineHttpController getController() {
        return controller;
    }

    @Autowired
    public void setController(JobEngineHttpController controller) {
        this.controller = controller;
    }

    public HttpContainerOptions getHttpServiceConfig() {
        return httpServiceConfig;
    }

    @Autowired
    public void setHttpServiceConfig(HttpContainerOptions httpServiceConfig) {
        this.httpServiceConfig = httpServiceConfig;
    }

}
