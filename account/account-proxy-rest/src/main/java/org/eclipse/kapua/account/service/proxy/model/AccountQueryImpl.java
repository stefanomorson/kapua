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
package org.eclipse.kapua.account.service.proxy.model;

import java.util.List;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaSortCriteria;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;


public class AccountQueryImpl implements org.eclipse.kapua.service.account.AccountQuery {

    public AccountQueryImpl(KapuaId scopeId) {

    }

     @Override
    public List<String> getFetchAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addFetchAttributes(String fetchAttribute) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFetchAttributes(List<String> fetchAttributeNames) {
        // TODO Auto-generated method stub

    }

    @Override
    public KapuaId getScopeId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        // TODO Auto-generated method stub

    }

    @Override
    public KapuaPredicate getPredicate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPredicate(KapuaPredicate queryPredicate) {
        // TODO Auto-generated method stub

    }

    @Override
    public KapuaSortCriteria getSortCriteria() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSortCriteria(KapuaSortCriteria sortCriteria) {
        // TODO Auto-generated method stub

    }

    @Override
    public Integer getOffset() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOffset(Integer offset) {
        // TODO Auto-generated method stub

    }

    @Override
    public Integer getLimit() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLimit(Integer limit) {
        // TODO Auto-generated method stub

    }

}
