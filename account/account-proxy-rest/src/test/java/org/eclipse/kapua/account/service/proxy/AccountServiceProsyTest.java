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
package org.eclipse.kapua.account.service.proxy;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.account.service.proxy.rest.AccountServiceRestProxy;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.service.account.Account;
import org.junit.Test;

public class AccountServiceProsyTest {

    @Test
    public void testFind() {

        try {
            AccountServiceRestProxy proxy = new AccountServiceRestProxy();
            Account account;
            account = proxy.find(KapuaEid.parseCompactId("AQ"), KapuaEid.parseCompactId("AQ"));
            System.out.println(account);
        } catch (KapuaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
