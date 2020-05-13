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

public class TcpClientOptions {

    private String host = "127.0.0.1";
    private int port;
    private boolean ssl;
    private String keyStorePath;
    private String keyStorePassword;
    private String trustStorePath;
    private String trustStorePassword;

    public String getHost() {
        return host;
    }

    public void setHost(String aHost) {
        host = aHost;
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
        return String.format("\"host\":\"%s\""
                + ", \"port\":\"%d\""
                + ", \"ssl\":\"%b\""
                + ", \"keyStorePath\":\"%s\""
                + ", \"keyStorePassword\":\"%s\""
                + ", \"trustStorePath\":\"%s\""
                + ", \"trustStorePassword\":\"%s\"",
                host, port, ssl, keyStorePath, "xxxxxxxxx", trustStorePath, "xxxxxxxxx");
    }

    public static TcpClientOptions from(TcpClientOptions aConfig) {

        if (aConfig == null) {
            return null;
        }

        TcpClientOptions copyConfig = new TcpClientOptions();
        copyConfig.setHost(aConfig.getHost());
        copyConfig.setKeyStorePassword(aConfig.getKeyStorePassword());
        copyConfig.setKeyStorePath(aConfig.getKeyStorePath());
        copyConfig.setPort(aConfig.getPort());
        copyConfig.setSsl(aConfig.isSsl());
        copyConfig.setTrustStorePassword(aConfig.getTrustStorePassword());
        copyConfig.setTrustStorePath(aConfig.getTrustStorePath());
        return copyConfig;
    }
}
