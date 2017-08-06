/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.KapuaApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestApiListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(RestApiListener.class);

    private KapuaApplication kapuaApplication;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        if (kapuaApplication != null) {
            try {
                kapuaApplication.shutdown();
            } catch (KapuaException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        if (kapuaApplication == null) {
            kapuaApplication = new KapuaApplication() {};
        }

        try {
            kapuaApplication.startup();
        } catch (KapuaException e) {
            logger.error(e.getMessage(), e);
        }
    }
}