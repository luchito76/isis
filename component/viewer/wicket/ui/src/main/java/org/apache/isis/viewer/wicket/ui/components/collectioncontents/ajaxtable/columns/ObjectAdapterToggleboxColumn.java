/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.isis.viewer.wicket.ui.components.collectioncontents.ajaxtable.columns;

import java.util.List;

import com.google.common.collect.Lists;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.adapter.mgr.AdapterManager.ConcurrencyChecking;
import org.apache.isis.viewer.wicket.model.common.SelectionHandler;
import org.apache.isis.viewer.wicket.model.models.EntityModel;
import org.apache.isis.viewer.wicket.ui.components.widgets.checkbox.ContainedToggleboxPanel;

public final class ObjectAdapterToggleboxColumn extends ColumnAbstract<ObjectAdapter> {

    private static final long serialVersionUID = 1L;

    private final SelectionHandler handler;

    public ObjectAdapterToggleboxColumn(final SelectionHandler handler) {
        super("");
        this.handler = handler;
    }
    
    @Override
    public Component getHeader(String componentId) {
        
        final ContainedToggleboxPanel toggle = new ContainedToggleboxPanel(componentId) {
            private static final long serialVersionUID = 1L;
            @Override
            public void onSubmit(AjaxRequestTarget target) {
                for (ContainedToggleboxPanel toggle : rowToggles) {
                    toggle.toggle(target);
                    target.add(toggle);
                }
            }
        };
        return toggle; 
    }

    private final List<ContainedToggleboxPanel> rowToggles = Lists.newArrayList(); 
            
    @Override
    public void populateItem(final Item<ICellPopulator<ObjectAdapter>> cellItem, final String componentId, final IModel<ObjectAdapter> rowModel) {
        final ContainedToggleboxPanel toggle = new ContainedToggleboxPanel(componentId) {
            private static final long serialVersionUID = 1L;
            @Override
            public void onSubmit(AjaxRequestTarget target) {
                final EntityModel o = (EntityModel) rowModel;
                final ObjectAdapter selectedAdapter = o.load(ConcurrencyChecking.NO_CHECK);
                handler.onSelected(this, selectedAdapter);
            }
        };
        rowToggles.add(toggle);
        toggle.setOutputMarkupId(true);
        cellItem.add(toggle);
    }
}