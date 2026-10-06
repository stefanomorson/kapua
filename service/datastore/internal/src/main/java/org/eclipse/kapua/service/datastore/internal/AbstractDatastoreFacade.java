/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.commons.cache.ExpiryPolicy;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDatastoreFacade {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDatastoreFacade.class);

    protected final ConfigurationProvider configProvider;
    // TODO Make the Cache size configurable.
    private static final LocalCache<String, MessageStoreConfiguration> MESSAGE_STORE_CONFIGS_CACHE = new LocalCache<>(1000, 60, ExpiryPolicy.MODIFIED, null);


    public AbstractDatastoreFacade(ConfigurationProvider configProvider) {
        this.configProvider = configProvider;
    }

    protected MessageStoreConfiguration getMessageStoreConfiguration(KapuaId scopeId) throws ConfigurationException {
        MessageStoreConfiguration messageStoreConfig;
        String compactId = scopeId.toCompactId();
        if (MESSAGE_STORE_CONFIGS_CACHE.get(compactId) == null) {
            LOG.info("Reloading message store configuration local cache for scope Id: {}", compactId);
            messageStoreConfig = configProvider.getConfiguration(scopeId);
            MESSAGE_STORE_CONFIGS_CACHE.put(compactId, messageStoreConfig);
        } else {
            messageStoreConfig = MESSAGE_STORE_CONFIGS_CACHE.get(compactId);
        }
        return messageStoreConfig;
    }

    protected boolean isDatastoreServiceEnabled(KapuaId scopeId) throws ConfigurationException {
        MessageStoreConfiguration messageStoreConfiguration = getMessageStoreConfiguration(scopeId);
        long ttl = messageStoreConfiguration.getDataTimeToLiveMilliseconds();

        return messageStoreConfiguration.getDataStorageEnabled() && ttl != MessageStoreConfiguration.DISABLED;
    }
}
