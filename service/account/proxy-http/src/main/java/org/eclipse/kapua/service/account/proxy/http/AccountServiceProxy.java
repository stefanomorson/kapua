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
package org.eclipse.kapua.service.account.proxy.http;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.vertx.AsyncClient;
import org.eclipse.kapua.commons.core.vertx.AsyncClientProvider;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.proxy.http.setting.KapuaAccountProxySetting;
import org.eclipse.kapua.service.account.proxy.http.setting.KapuaAccountProxySettingKeys;
import org.xml.sax.SAXException;

import io.vertx.core.Future;

@KapuaProvider
public class AccountServiceProxy implements AccountService {

    @Inject
    private AsyncClientProvider webClientProvider;

    private AsyncClient asyncClient;

    public AccountServiceProxy() {
    }

    @Override
    public Account create(AccountCreator creator) throws KapuaException {
        CountDownLatch latch = new CountDownLatch(1);
        Future<Account> createEvt = Future.future();
        try {
            KapuaSession session = KapuaSecurityUtils.getSession();
            KapuaId scopeId = session.getScopeId();
            String creatorJsonString = XmlUtil.marshalJson(creator);
            getWebClient()
            .post(KapuaAccountProxySetting.getInstance().getInt(KapuaAccountProxySettingKeys.ACCOUNT_PROXY_PORT),
                  KapuaAccountProxySetting.getInstance().getString(KapuaAccountProxySettingKeys.ACCOUNT_PROXY_HOST),
                  String.format("/accounts/%s/child-accounts", scopeId.toStringId()))
            .putHeader("Authorization", "Bearer " + session.getAccessToken())
            .putHeader("ContentType", "application/json")
            .sendJson(creatorJsonString, sendEvt -> {
                if (sendEvt.succeeded()) {
                    try {
                        sendEvt.result();
                        Account account = XmlUtil.unmarshal(sendEvt.result().toString(), Account.class);
                        createEvt.complete(account);
                        latch.countDown();
                    } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException e) {
                        createEvt.fail(e);
                        latch.countDown();
                    }
                } else {

                }
            });
        } catch (JAXBException e) {
            throw KapuaException.internalError(e);
        }

        try {
            if (!latch.await(30, TimeUnit.SECONDS)) {
                throw KapuaException.internalError("Request timeout expired");
            }
        } catch (InterruptedException e) {
            KapuaException.internalError(e);
        }

        if (!createEvt.succeeded()) {
            throw KapuaException.internalError(createEvt.cause());
        }

        return createEvt.result();
    }

    @Override
    public Account find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count(KapuaQuery<Account> query) throws KapuaException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        // TODO Auto-generated method stub
    }

    @Override
    public Account update(Account entity) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Account findByName(String name) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KapuaTocd getConfigMetadata(KapuaId scopeId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) throws KapuaException {
        // TODO Auto-generated method stub
    }

    @Override
    public Account find(KapuaId id) throws KapuaException {
        CountDownLatch latch = new CountDownLatch(1);
        Future<Account> findEvt = Future.future();
        KapuaSession session = KapuaSecurityUtils.getSession();
        getWebClient()
        .get(KapuaAccountProxySetting.getInstance().getInt(KapuaAccountProxySettingKeys.ACCOUNT_PROXY_PORT),
              KapuaAccountProxySetting.getInstance().getString(KapuaAccountProxySettingKeys.ACCOUNT_PROXY_HOST),
              String.format("/accounts/%s", id.toStringId()))
        .putHeader("Authorization", "Bearer " + session.getAccessToken())
        .putHeader("ContentType", "application/json")
        .send(sendEvt -> {
            if (sendEvt.succeeded()) {
                try {
                    sendEvt.result();
                    Account account = XmlUtil.unmarshal(sendEvt.result().body().toString(), Account.class);
                    findEvt.complete(account);
                    latch.countDown();
                } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException e) {
                    findEvt.fail(e);
                    latch.countDown();
                }
            } else {

            }
        });

        try {
            if (!latch.await(30, TimeUnit.SECONDS)) {
                throw KapuaException.internalError("Request timeout expired");
            }
        } catch (InterruptedException e) {
            KapuaException.internalError(e);
        }

        if (!findEvt.succeeded()) {
            throw KapuaException.internalError(findEvt.cause());
        }

        return findEvt.result();
    }

    @Override
    public AccountListResult query(KapuaQuery<Account> query) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccountListResult findChildsRecursively(KapuaId accountId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    private AsyncClient getWebClient() {
        if (asyncClient == null) {
            asyncClient = webClientProvider.get();
        }
        return asyncClient;
    }
}
