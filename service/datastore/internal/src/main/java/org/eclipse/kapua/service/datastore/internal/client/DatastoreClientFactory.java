/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore.internal.client;

import org.eclipse.kapua.service.datastore.exception.DatastoreInternalError;
import org.eclipse.kapua.service.datastore.internal.converter.ModelContextImpl;
import org.eclipse.kapua.service.datastore.internal.converter.QueryConverterImpl;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClient;
import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;
import org.eclipse.kapua.service.elasticsearch.client.configuration.ElasticsearchClientConfiguration;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the {@link ElasticsearchClientProvider} as a singleton for the message store.
 *
 * @since 1.0.0
 */
public class DatastoreClientFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DatastoreClientFactory.class);

    private static List<ElasticsearchClientProvider<?>> elasticsearchClientProviderInstances;

    private static volatile boolean initialized;
    private static volatile boolean closed = true;

    private static AtomicInteger nextProviderIndex = new AtomicInteger(0);

    private DatastoreClientFactory() {
    }

    /**
     * Gets the {@link ElasticsearchClientProvider} instance.
     * <p>
     * The implementation is specified by {@link DatastoreElasticsearchClientConfiguration#getProviderClassName()}.
     *
     * @return An Elasticsearch client.
     */
    public static ElasticsearchClientProvider<?> getInstance() {
        if (!closed) {
            LOG.warn("Datastore client factory: closing the pool failed at a previous stage, trying to close before init.");
            close();
        }
        if (!initialized || !closed) {
            synchronized (DatastoreClientFactory.class) {
                if (!closed) {
                    throw new DatastoreInternalError(null, "EDatastore client factory: closing the pool failed at a previous stage, can't init");
                }
                if (!initialized) {
                    ElasticsearchClientProvider<?> elasticsearchClientProvider;
                    try {
                        initialized = false;
                        ElasticsearchClientConfiguration esClientConfiguration = DatastoreElasticsearchClientConfiguration.getInstance();
                        int poolSize = esClientConfiguration.getPoolSize();
                        if (poolSize >= 1) {
                            LOG.info("Datastore client factory: configured pool of size {}", poolSize);
                        } else {
                            LOG.warn("Datastore client factory: configured pool of size {} is invalid, set to default 1", poolSize);
                            poolSize = 1;
                        }
                        Class<ElasticsearchClientProvider<?>> providerClass = (Class<ElasticsearchClientProvider<?>>) Class.forName(esClientConfiguration.getProviderClassName());
                        Constructor<?> constructor = providerClass.getConstructor();
                        elasticsearchClientProviderInstances = new ArrayList<>(poolSize);
                        for (int i=0; i < poolSize; i++) {
                            elasticsearchClientProvider = (ElasticsearchClientProvider<?>) constructor.newInstance();
                            elasticsearchClientProvider
                                    .withClientConfiguration(esClientConfiguration)
                                    .withModelContext(new ModelContextImpl())
                                    .withModelConverter(new QueryConverterImpl())
                                    .init();

                            elasticsearchClientProviderInstances.add(elasticsearchClientProvider);
                        }
                        initialized = true;
                    } catch (Exception e) {
                        // Try close immediately. Use closePool to avoid synchronize again
                        closePool();
                        throw new DatastoreInternalError(e, "Datastore client factory: cannot instantiate Elasticsearch Client Pool");
                    }
                }
            }
        }
        // Calculate the index of the client to return from the pool in a round robin fashion to evenly distribute requests. 
        int providerIndex = Math.abs(nextProviderIndex.getAndAdd(1) % elasticsearchClientProviderInstances.size());
        LOG.debug("Datastore client factory: assign request to client of index {} in a pool of {} (initialized {}, closed {})", providerIndex, elasticsearchClientProviderInstances.size(), initialized, closed);
        ElasticsearchClientProvider<?> elasticsearchClientProviderInstance = elasticsearchClientProviderInstances.get(providerIndex);
        return elasticsearchClientProviderInstance;
    }

    /**
     * Gets the {@link ElasticsearchClient} instance.
     *
     * @return The {@link ElasticsearchClient} instance.
     * @throws ClientUnavailableException see {@link ElasticsearchClientProvider#getElasticsearchClient()}
     * @since 1.3.0
     */
    public static ElasticsearchClient<?> getElasticsearchClient() throws ClientUnavailableException {
        return getInstance().getElasticsearchClient();
    }

    /**
     * Closes the {@link ElasticsearchClientProvider} instance.
     *
     * @since 1.0.0
     */
    public static void close() {
        if (elasticsearchClientProviderInstances != null) {
            synchronized (DatastoreClientFactory.class) {
                closePool();
            }
        }
    }

    private static void closePool() {
        if (elasticsearchClientProviderInstances != null) {
            try {
                initialized = false;
                closed = false;
                int size = elasticsearchClientProviderInstances.size();
                for (int i=0; i < size; i++) {
                    if (elasticsearchClientProviderInstances.get(i) != null) {
                        elasticsearchClientProviderInstances.get(i).close();
                        elasticsearchClientProviderInstances.set(i, null);
                    }
                }
                elasticsearchClientProviderInstances.clear();
                elasticsearchClientProviderInstances = null;
                closed = true;
            } catch (Exception e) {
                LOG.error("Datastore client factory: unable to close all clients.", e);
            }
        } else {
            closed = true;
        }
    }
}
