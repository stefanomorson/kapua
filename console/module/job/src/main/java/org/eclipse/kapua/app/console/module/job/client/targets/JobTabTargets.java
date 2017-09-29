/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.job.client.targets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJob;

public class JobTabTargets extends KapuaTabItem<GwtJob> {

    private static final ConsoleJobMessages MSGS = GWT.create(ConsoleJobMessages.class);

    private JobTabTargetsGrid targetsGrid;

    public JobTabTargets(GwtSession currentSession) {
        super(MSGS.gridJobTabTargetsLabel(), new KapuaIcon(IconSet.BULLSEYE));
        targetsGrid = new JobTabTargetsGrid(null, currentSession);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        add(targetsGrid);
    }

    @Override
    public void setEntity(GwtJob gwtJob) {
        super.setEntity(gwtJob);
        if (gwtJob != null) {
            targetsGrid.setJobId(gwtJob.getId());
            targetsGrid.getToolbar().setJobId(gwtJob.getId());
        } else {
            targetsGrid.setJobId(null);
            targetsGrid.getToolbar().setJobId(null);
        }
    }

    @Override
    protected void doRefresh() {
        targetsGrid.refresh();
        targetsGrid.getToolbar().getAddEntityButton().setEnabled(selectedEntity != null && selectedEntity.getJobXmlDefinition() == null);
        targetsGrid.getToolbar().getRefreshEntityButton().setEnabled(selectedEntity != null);
    }

}
