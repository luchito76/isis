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

package org.apache.isis.viewer.wicket.ui.components.widgets.entitysimplelink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.vaynberg.wicket.select2.ChoiceProvider;
import com.vaynberg.wicket.select2.Select2Choice;
import com.vaynberg.wicket.select2.Settings;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.adapter.mgr.AdapterManager.ConcurrencyChecking;
import org.apache.isis.core.metamodel.facets.object.autocomplete.AutoCompleteFacet;
import org.apache.isis.core.metamodel.spec.ActionType;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.feature.ObjectAction;
import org.apache.isis.core.runtime.system.DeploymentType;
import org.apache.isis.core.runtime.system.context.IsisContext;
import org.apache.isis.viewer.wicket.model.mementos.ObjectAdapterMemento;
import org.apache.isis.viewer.wicket.model.models.ActionModel;
import org.apache.isis.viewer.wicket.model.models.EntityModel;
import org.apache.isis.viewer.wicket.model.models.ScalarModelWithPending;
import org.apache.isis.viewer.wicket.model.models.EntityModel.RenderingHint;
import org.apache.isis.viewer.wicket.model.models.ScalarModelWithPending.Util;
import org.apache.isis.viewer.wicket.model.models.ModelAbstract;
import org.apache.isis.viewer.wicket.model.models.ScalarModel;
import org.apache.isis.viewer.wicket.model.util.Mementos;
import org.apache.isis.viewer.wicket.ui.ComponentFactory;
import org.apache.isis.viewer.wicket.ui.ComponentType;
import org.apache.isis.viewer.wicket.ui.components.actions.ActionInvokeHandler;
import org.apache.isis.viewer.wicket.ui.components.widgets.ObjectAdapterMementoProviderAbstract;
import org.apache.isis.viewer.wicket.ui.components.widgets.formcomponent.CancelHintRequired;
import org.apache.isis.viewer.wicket.ui.components.widgets.formcomponent.FormComponentPanelAbstract;

/**
 * {@link FormComponentPanel} representing a reference to an entity: a link and
 * (optionally) an autocomplete field.
 */
public class EntityLinkSimplePanel extends FormComponentPanelAbstract<ObjectAdapter> implements CancelHintRequired, ActionInvokeHandler  {

    private static final long serialVersionUID = 1L;

    private static final String ID_ENTITY_ICON_AND_TITLE = "entityIconAndTitle";
    private static final String ID_ENTITY_TITLE_NULL = "entityTitleNull";
    
    public EntityLinkSimplePanel(final String id, final EntityModel entityModel) {
        super(id, entityModel);
        setType(ObjectAdapter.class);
        buildGui();
    }

    public EntityModel getEntityModel() {
        return (EntityModel) getModel();
    }

    private void buildGui() {
        syncWithInput();
    }


    @Override
    protected void onBeforeRender() {
        syncWithInput();
        super.onBeforeRender();
    }

    private void syncWithInput() {
        final ObjectAdapter adapter = getPendingElseCurrentAdapter();

        if (adapter != null) {
            final EntityModel entityModelForLink = new EntityModel(adapter);
            entityModelForLink.setContextAdapterIfAny(getEntityModel().getContextAdapterIfAny());
            entityModelForLink.setRenderingHint(getEntityModel().getRenderingHint());
            
            final ComponentFactory componentFactory = getComponentFactoryRegistry().findComponentFactory(ComponentType.ENTITY_ICON_AND_TITLE, entityModelForLink);
            final Component component = componentFactory.createComponent(entityModelForLink);
            addOrReplace(component);
            
            permanentlyHide(ID_ENTITY_TITLE_NULL);
        } else {
            // represent no object by a simple label displaying '(null)'
            addOrReplace(new Label(ID_ENTITY_TITLE_NULL, ""));
            permanentlyHide(ID_ENTITY_ICON_AND_TITLE);
        }
    }

    @Override
    public void onClick(final ActionModel actionModel) {
    }

    @Override
    public void onCancel() {
    }

    private ObjectAdapter getPendingElseCurrentAdapter() {
        return getEntityModel().getPendingElseCurrentAdapter();
    }

}
