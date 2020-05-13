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
package org.eclipse.kapua.service.commons.amqp;

import org.eclipse.kapua.service.commons.TcpClientOptions;

public class AmqpBridgeClientOptions {

    private TcpClientOptions endpoint;
    private String username;
    private String password;

    protected AmqpBridgeClientOptions() {
        this(new AmqpBridgeClientOptions());
    }

    protected AmqpBridgeClientOptions(AmqpBridgeClientOptions options) {
        setEndpoint(TcpClientOptions.from(options.getEndpoint()));
        setUsername(options.getUsername());
        setPassword(options.getPassword());
    }

    public TcpClientOptions getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(TcpClientOptions endpoint) {
        this.endpoint = endpoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static AmqpBridgeClientOptions from() {
        return new AmqpBridgeClientOptions();
    }

    public static AmqpBridgeClientOptions from(AmqpBridgeClientOptions options) {
        return new AmqpBridgeClientOptions(options);
    }
}
