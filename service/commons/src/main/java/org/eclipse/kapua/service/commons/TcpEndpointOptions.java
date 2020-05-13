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
package org.eclipse.kapua.service.commons;

import io.vertx.core.http.ClientAuth;

public class TcpEndpointOptions {

    private String bindAddress = "0.0.0.0";
    private int port = 80;
    private boolean ssl;
    private String keyStorePath;
    private String keyStorePassword;
    private ClientAuth clientAuth = ClientAuth.NONE;
    private String trustStorePath;
    private String trustStorePassword;

    public String getBindAddress() {
        return bindAddress;
    }

    public void setBindAddress(String aBindAddress) {
        bindAddress = aBindAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int aPort) {
        port = aPort;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public ClientAuth getClientAuth() {
        return clientAuth;
    }

    public void setClientAuth(ClientAuth clientAuth) {
        this.clientAuth = clientAuth;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    @Override
    public String toString() {
        return String.format("\"bindAddress\":\"%s\""
                + ", \"port\":\"%d\""
                + ", \"ssl\":\"%b\""
                + ", \"keyStorePath\":\"%s\""
                + ", \"keyStorePassword\":\"%s\""
                + ", \"clientAuth\":\"%s\""
                + ", \"trustStorePath\":\"%s\""
                + ", \"trustStorePassword\":\"%s\"",
                bindAddress, port, ssl, keyStorePath, "xxxxxxxxx", clientAuth, trustStorePath, "xxxxxxxxx");
    }

    public static TcpEndpointOptions from(TcpEndpointOptions aConfig) {

        if (aConfig == null) {
            return null;
        }

        TcpEndpointOptions copyConfig = new TcpEndpointOptions();
        copyConfig.setBindAddress(aConfig.getBindAddress());
        copyConfig.setClientAuth(aConfig.getClientAuth());
        copyConfig.setKeyStorePassword(aConfig.getKeyStorePassword());
        copyConfig.setKeyStorePath(aConfig.getKeyStorePath());
        copyConfig.setPort(aConfig.getPort());
        copyConfig.setSsl(aConfig.isSsl());
        copyConfig.setTrustStorePassword(aConfig.getTrustStorePassword());
        copyConfig.setTrustStorePath(aConfig.getTrustStorePath());
        return copyConfig;
    }
}
