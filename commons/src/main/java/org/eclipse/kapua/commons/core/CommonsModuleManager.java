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
package org.eclipse.kapua.commons.core;

public class CommonsModuleManager {

    private static CommonsModuleManager instance = new CommonsModuleManager();

    private CommonsModule module;

    private CommonsModuleManager() {}

    public static CommonsModuleManager manager() {
        return instance;
    }

    public void setCommonsModule(CommonsModule aModule) {
        module = aModule;
    }

    public CommonsModule getCommonsModule() {
        return module;
    }
}
