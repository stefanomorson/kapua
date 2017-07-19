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
package org.eclipse.kapua.account.service.uservice;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.eclipse.kapua.account.service.api.rest.AccountsResource;
import org.eclipse.kapua.account.service.commons.jaxb.JaxbContextResolver;
import org.eclipse.kapua.commons.rest.api.KapuaSerializableBodyReader;
import org.eclipse.kapua.commons.rest.api.KapuaSerializableBodyWriter;
import org.jboss.resteasy.plugins.server.vertx.VertxRequestHandler;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;

public class AccountsServer extends AbstractVerticle {

    private ServiceDiscovery discovery;
    private VertxResteasyDeployment deployment;

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions());
        vertx.deployVerticle(new AccountsServer());
    }

    @Override
    public void start() {

        vertx.executeBlocking(future ->{

            Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
            SecurityManager securityManager = factory.getInstance();
            SecurityUtils.setSecurityManager(securityManager);

            // Create discovery service
            discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions().setBackendConfiguration(config()));

            // Publish RESTful endpoint
            deployment = createRestEndpoint();
            publishRestEndpoint();

            // Publish EventBus service
            //publishEventBusService();            

            future.complete();
        },
        res -> {
            if (res.succeeded()) {
                System.out.println("Startup succeeded!");
            } else {
                res.cause().printStackTrace();
            }
        });
    }

    @Override
    public void stop() {

        // Stop the deployment
        if (deployment != null) {
        }

        // Close discovery servuce
        if (discovery != null) {
            discovery.close();
        }
    }

    private VertxResteasyDeployment createRestEndpoint() {

        VertxResteasyDeployment deployment = new VertxResteasyDeployment();
        deployment.start();
        deployment.getProviderFactory().register(new JaxbContextResolver());
        deployment.getProviderFactory().register(new KapuaSerializableBodyWriter());
        deployment.getProviderFactory().register(new KapuaSerializableBodyReader());
        deployment.getRegistry().addPerInstanceResource(AccountsResource.class);

        // Start the front end server using the Jax-RS controller
        VertxRequestHandler vertxRequestHandler = new VertxRequestHandler(vertx, deployment);
        vertx.createHttpServer()
               .requestHandler(vertxRequestHandler)
               .listen(8182,
                        ar -> {
                            if (ar.succeeded()) {
                                System.out.println("Server started on port "
                                        + ar.result().actualPort());
                            } else {
                                ar.cause().printStackTrace();
                            }
                        });

        return deployment;
    }

    private void publishRestEndpoint() { 
        Record record = HttpEndpoint.createRecord("kapua-account-api", "localhost", 8182, "/");
        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                System.out.println("Service published : " + record.getName());
            } else {
                ar.cause().printStackTrace();
            }
        });
    }

    private void publishEventBusService() {
//
//        // Register the service proxy on the event bus
//        Accounts accountsService = new AccountsImpl();
//        MessageConsumer<JsonObject> consumer = ProxyHelper.registerService(Accounts.class, vertx, accountsService, Accounts.ADDRESS);
//
//        // Publish it in the discovery infrastructure
//        Record record = EventBusService.createRecord("users", Accounts.ADDRESS, Accounts.class);
//        discovery.publish(record, ar -> {
//            if (ar.succeeded()) {
//                // publication succeeded
//                System.out.println("Service published : " + ar.succeeded());
//            } else {
//                ar.cause().printStackTrace();
//            }
//        });
    }
}
