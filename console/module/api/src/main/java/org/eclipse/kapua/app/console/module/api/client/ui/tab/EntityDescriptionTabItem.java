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
package org.eclipse.kapua.app.console.module.api.client.ui.tab;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.KapuaGrid;
import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class EntityDescriptionTabItem<M extends GwtEntityModel> extends KapuaTabItem<M> {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private Grid<GwtGroupedNVPair> descriptionGrid;
    private GroupingStore<GwtGroupedNVPair> descriptionValuesStore;
    private BaseListLoader<ListLoadResult<GwtGroupedNVPair>> descriptionValuesloader;

    public EntityDescriptionTabItem() {
        super(MSGS.entityTabDescriptionTitle(), new KapuaIcon(IconSet.INFO));
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        //
        // Container borders
        setBorders(true);

        RpcProxy<ListLoadResult<GwtGroupedNVPair>> proxy = getDataProxy();
        descriptionValuesloader = new BaseListLoader<ListLoadResult<GwtGroupedNVPair>>(proxy);
        descriptionValuesStore = new GroupingStore<GwtGroupedNVPair>(descriptionValuesloader);
        descriptionValuesStore.groupBy("groupLoc");

        //
        // Columns
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        ColumnConfig name = new ColumnConfig("nameLoc", MSGS.entityTabDescriptionName(), 50);
        ColumnConfig value = new ColumnConfig("value", MSGS.devicePropValue(), 50);
        // Name column
        GridCellRenderer<GwtGroupedNVPair> renderer = new GridCellRenderer<GwtGroupedNVPair>() {

            @Override
            public Object render(GwtGroupedNVPair model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtGroupedNVPair> store, Grid<GwtGroupedNVPair> grid) {
                Object value = model.getValue();
                return value;
            }
        };

        value.setRenderer(renderer);
        columns.add(name);
        columns.add(value);

        ColumnModel cm = new ColumnModel(columns);

        //
        // Grid
        GroupingView groupingView = new GroupingView();
        groupingView.setShowGroupedColumn(false);
        groupingView.setForceFit(true);
        groupingView.setAutoFill(true);
        groupingView.setSortingEnabled(false);
        groupingView.setShowGroupedColumn(false);
        groupingView.setEmptyText(MSGS.entityTabDescriptionNoSelection());
        groupingView.setEnableNoGroups(false);
        groupingView.setEnableGroupingMenu(false);
        descriptionGrid = new KapuaGrid<GwtGroupedNVPair>(descriptionValuesStore, cm);
        descriptionGrid.setView(groupingView);

        this.setStyleAttribute("border-top-width: 0px", "!important;");
        add(descriptionGrid);
    }

    @Override
    public void setEntity(M t) {
        super.setEntity(t);
    }

    protected abstract RpcProxy<ListLoadResult<GwtGroupedNVPair>> getDataProxy();

    protected Object renderNameCell(GwtGroupedNVPair model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtGroupedNVPair> store, Grid<GwtGroupedNVPair> grid) {
        return model.getName();
    }

    protected Object renderValueCell(GwtGroupedNVPair model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtGroupedNVPair> store, Grid<GwtGroupedNVPair> grid) {
        Object value = model.getValue();
        if (value != null && value instanceof Date) {
            Date dateValue = (Date) value;
            return DateUtils.formatDateTime(dateValue);
        }
        return value;
    }

    @Override
    protected void doRefresh() {
        if (selectedEntity != null) {
            descriptionValuesloader.load();
        } else {
            descriptionValuesStore.removeAll();
        }
    }
}
