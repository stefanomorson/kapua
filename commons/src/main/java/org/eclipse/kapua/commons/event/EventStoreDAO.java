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
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.event.KapuaEvent;

public class EventStoreDAO {

    private EventStoreDAO() {
    }

    /**
     * Creates and return new KapuaEvent
     * 
     * @param em
     * @param kapuaEvent
     * @return
     * @throws KapuaException
     */
    public static KapuaEvent create(EntityManager em, KapuaEvent kapuaEvent)
            throws KapuaException {

        // TODO Implements persistency
        return null;
    }

    /**
     * Updates the provided KapuaEvent
     * 
     * @param em
     * @param kapuaEvent
     * @return
     * @throws KapuaException
     */
    public static KapuaEvent update(EntityManager em, KapuaEvent account)
            throws KapuaException {

        // TODO Implements persistency
        return null;
    }

    /**
     * Deletes the kapua event by event identifier
     * 
     * @param em
     * @param eventId
     * @throws KapuaEntityNotFoundException
     *             If the {@link Account} is not found
     */
    public static void delete(EntityManager em, KapuaId eventId) throws KapuaEntityNotFoundException {
        // TODO Implements persistency
    }

    /**
     * Finds the event by event identifier
     */
    public static KapuaEvent find(EntityManager em, KapuaId eventId) {
        // TODO Implements persistency
        return null;
    }
}
