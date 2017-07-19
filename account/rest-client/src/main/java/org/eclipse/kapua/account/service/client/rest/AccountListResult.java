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
package org.eclipse.kapua.account.service.client.rest;

import java.util.Collection;
import java.util.List;

import org.eclipse.kapua.service.account.Account;

public class AccountListResult implements org.eclipse.kapua.service.account.AccountListResult {

    /**
     * 
     */
    private static final long serialVersionUID = 6520121106549200218L;

    @Override
    public boolean isLimitExceeded() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setLimitExceeded(boolean limitExceeded) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Account> getItems() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Account getItem(int i) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Account getFirstItem() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void addItems(Collection<? extends Account> items) {
        // TODO Auto-generated method stub

    }

    @Override
    public void clearItems() {
        // TODO Auto-generated method stub

    }

}
