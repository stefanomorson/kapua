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

import java.util.Date;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.eclipse.kapua.commons.event.bus.EventbusProvider;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventbus;
import org.eclipse.kapua.service.event.KapuaEventbusException;
import org.eclipse.kapua.service.event.RaiseKapuaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Event interceptor
 * 
 * @since 1.0
 */
@KapuaProvider
@InterceptorBind(matchSubclassOf = KapuaService.class, matchAnnotatedWith = RaiseKapuaEvent.class)
public class RaiseKapuaEventInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaiseKapuaEventInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object returnObject = null;

        try {
            // if(!create) then the entity id can be set here
            KapuaEvent event = EventScope.begin();

            KapuaSession session = KapuaSecurityUtils.getSession();
            // Context ID is initialized/managed by the EventScope object
            event.setTimestamp(new Date());
            event.setUserId(session.getUserId());
            event.setScopeId(session.getScopeId());
            Object[] arguments = invocation.getArguments();
            if (arguments!=null) {
                for (Object obj : arguments) {
                    if (obj instanceof KapuaEntity) {
                        event.setEntityType(obj.getClass().getName());
                        event.setEntityId(((KapuaEntity) obj).getId());
                        break;
                    }
                }
            }
            event.setOperation(invocation.getMethod().getName());
            //get the service name
            //the service is wrapped by guice so getThis --> getSuperclass() should provide the intercepted class
            //then keep the interface from this object
            Class<?> wrappedClass = invocation.getThis().getClass().getSuperclass(); //this object should be not null
            Class<?>[] impementedClass = wrappedClass.getInterfaces();
            String interfaceName = impementedClass[0].getName();
            String tmp[] = interfaceName.split("\\.");
            String serviceName = tmp[tmp.length-1];
            event.setService(serviceName.substring(0, serviceName.length()-"Service".length()).toLowerCase());

            // TODO Extract from MethodInvocation and RaiseKapuaEvent annotation attributes
            // event.setInputs(inputs);
            // event.setProperties(properties);

               //execute the business logic
            returnObject = invocation.proceed();

            // Raise service event if the execution is successful
            sendEvent(event, returnObject, invocation);

            return returnObject;

        } finally {
            EventScope.end();
        }
    }

    private void sendEvent(KapuaEvent kapuaEvent, Object returnedValue, MethodInvocation invocation) throws KapuaEventbusException {

        KapuaEventbus eventbus = EventbusProvider.getInstance();

        String address = String.format("%s", kapuaEvent.getService());
        eventbus.publish(address, kapuaEvent);
        LOGGER.info("SENT event from service {} - entity type {} - entity id {} - context id {}", new Object[]{kapuaEvent.getService(), kapuaEvent.getEntityType(), kapuaEvent.getEntityId(), kapuaEvent.getContextId()});
    }

}