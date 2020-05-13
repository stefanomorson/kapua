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
package org.eclipse.kapua.service.lifecycle.consumer.app;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.commons.app.KapuaIdMixin;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class LifecycleConsumerModule extends SimpleModule {

    private static final long serialVersionUID = -4889595576117483977L;

    public LifecycleConsumerModule() {
        setMixInAnnotation(KapuaId.class, KapuaIdMixin.class);
    }
}
