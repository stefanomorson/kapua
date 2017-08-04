/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.service.event.internal;

import java.util.Date;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.kapua.commons.event.EventScope;
import org.eclipse.kapua.commons.event.bus.EventBus;
import org.eclipse.kapua.commons.event.bus.EventBusException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.inject.Interceptor;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.RaiseKapuaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Event interceptor
 * 
 * @since 1.0
 */
@KapuaProvider
@Interceptor(matchSubclassOf = KapuaService.class, matchAnnotatedWith = RaiseKapuaEvent.class)
public class RaiseKapuaEventInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaiseKapuaEventInterceptor.class);

    @Inject
    EventBus eventBus;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object returnObject = null;

        try {
            KapuaEvent event = EventScope.begin();

            KapuaSession session = KapuaSecurityUtils.getSession();
            // Context ID is initialized/managed by the EventScope object
            event.setTimestamp(new Date());
            event.setUserId(session.getUserId());
            event.setScopeId(session.getScopeId());

            // TODO Extract from MethodInvocation and RaiseKapuaEvent annotation attributes
            // event.setService(service);
            // event.setEntityType(entityType);
            // if(!create) then the entity id can be set here
            // event.setEntityId(entityId);
            // event.setOperation(operation);
            // event.setInputs(inputs);
            // event.setProperties(properties);

            returnObject = invocation.proceed();

            // Raise service event if the execution is successful
            sendEvent(event, returnObject, invocation);

            return returnObject;

        } finally {
            EventScope.end();
        }
    }

    private void sendEvent(KapuaEvent event, Object returnedValue, MethodInvocation invocation) throws EventBusException {
        String address = String.format("%s", event.getService());
        eventBus.publish(address, event);
        eventBus.publish(address + ".user", event);
    }

}
