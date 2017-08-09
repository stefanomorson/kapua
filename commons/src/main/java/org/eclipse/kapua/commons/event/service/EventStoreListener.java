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
package org.eclipse.kapua.commons.event.service;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventService;
import org.eclipse.kapua.service.event.KapuaEventbusException;
import org.eclipse.kapua.service.event.KapuaServiceEventListener;
import org.eclipse.kapua.service.event.ListenKapuaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventStoreListener implements KapuaServiceEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventStoreListener.class);

    private KapuaEventService kapuaEventService;

    public EventStoreListener(KapuaEventService kapuaEventService) throws KapuaEventbusException {
        this.kapuaEventService = kapuaEventService;
    }

    @Override
    @ListenKapuaEvent
    public void onKapuaEvent(KapuaEvent kapuaEvent) throws KapuaException {

        if (kapuaEvent == null) {
            LOGGER.error("Received null event");
            return;
        }

        LOGGER.info("Received event from service {} - entity type {} - entity id {} - context id {}",
                new Object[] { kapuaEvent.getService(), kapuaEvent.getEntityType(), kapuaEvent.getEntityId(), kapuaEvent.getContextId() });

        KapuaSecurityUtils.doPrivileged(()->{            
            KapuaEvent persistedKapuaEvent = kapuaEventService.find(kapuaEvent.getScopeId(), kapuaEvent.getId());
            persistedKapuaEvent.setStatus(KapuaEvent.EVENT_STATUS.CONFIRMED.name());
            kapuaEventService.update(persistedKapuaEvent);
        });
    }
}