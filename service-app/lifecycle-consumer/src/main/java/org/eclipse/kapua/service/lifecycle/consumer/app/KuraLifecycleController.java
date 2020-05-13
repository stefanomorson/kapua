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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.commons.MessageRouter;
import org.eclipse.kapua.service.commons.MessageRoutingContext;
import org.eclipse.kapua.service.commons.MessageServiceController;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifeCycleService;
import org.springframework.stereotype.Component;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;

@Component
public class KuraLifecycleController implements MessageServiceController<Message<Buffer>> {

    private final KapuaLocator kapuaLocator = KapuaLocator.getInstance();
    private final DeviceLifeCycleService lifeCycleService = kapuaLocator.getService(DeviceLifeCycleService.class);

    @Override
    public final void registerRoutes(MessageRouter<Message<Buffer>> router) {
        router.route().where("full-topic", "*/MQTT/BIRTH").handler(this::handleBirth);
        router.route().where("full-topic", "*/MQTT/DC").handler(this::handleDisconnect);
        router.route().where("full-topic", "*/MQTT/MISSING").handler(this::handleMissing);
        router.route().where("full-topic", "*/MQTT/APPS").handler(this::handleApplications);
        router.route().failureHandler(this::handleFailure);
    }

    public void handleBirth(MessageRoutingContext<Message<Buffer>> aCtx) {
        try {
            lifeCycleService.birth(null, null);
        } catch (KapuaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void handleDisconnect(MessageRoutingContext<Message<Buffer>> aCtx) {
        try {
            lifeCycleService.death(null, null);
        } catch (KapuaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void handleMissing(MessageRoutingContext<Message<Buffer>> aCtx) {
        try {
            lifeCycleService.missing(null, null);
        } catch (KapuaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void handleApplications(MessageRoutingContext<Message<Buffer>> aCtx) {
        try {
            lifeCycleService.applications(null, null);
        } catch (KapuaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void handleFailure(MessageRoutingContext<Message<Buffer>> aCtx) {

    }
}
