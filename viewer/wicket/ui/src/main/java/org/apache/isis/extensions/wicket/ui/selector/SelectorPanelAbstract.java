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


package org.apache.isis.extensions.wicket.ui.selector;

import java.util.List;

import org.apache.isis.extensions.wicket.ui.ComponentFactory;
import org.apache.isis.extensions.wicket.ui.ComponentType;
import org.apache.isis.extensions.wicket.ui.panels.PanelAbstract;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public abstract class SelectorPanelAbstract<T extends IModel<?>> extends
		PanelAbstract<T> {

	private static final long serialVersionUID = 1L;

	private static final String ID_VIEWS = "views";
	private static final String ID_VIEWS_DROP_DOWN = "viewsDropDown";

    private final ComponentType componentType;

    public SelectorPanelAbstract(final String id,
            final String underlyingId, final T model, final ComponentFactory factory) {
        super(id, model);
        
        componentType = factory.getComponentType();

        addUnderlyingViews(underlyingId, model, factory);
    }

    private void addUnderlyingViews(final String underlyingId, final T model,
            final ComponentFactory factory) {
        List<ComponentFactory> componentFactories = findOtherComponentFactories(
				model, factory);
		ComponentFactory firstComponentFactory = componentFactories.get(0);
		final Model<ComponentFactory> componentFactoryModel = new Model<ComponentFactory>();
		componentFactoryModel.setObject(firstComponentFactory);

		if (componentFactories.size()>1) {
			WebMarkupContainer views = new WebMarkupContainer(ID_VIEWS);
			DropDownChoiceComponentFactory viewsDropDown = new DropDownChoiceComponentFactory(ID_VIEWS_DROP_DOWN,
					componentFactoryModel,
					componentFactories,
					this, underlyingId, model);
			views.addOrReplace(viewsDropDown);
			addOrReplace(views);
		} else {
			permanentlyHide(ID_VIEWS);
		}
		addOrReplace(firstComponentFactory.createComponent(underlyingId, model));
    }

	private List<ComponentFactory> findOtherComponentFactories(
			T model, final ComponentFactory ignoreFactory) {
		List<ComponentFactory> componentFactories = getComponentFactoryRegistry()
				.findComponentFactories(componentType, model);
		return Lists.newArrayList(Collections2.filter(componentFactories,
				new Predicate<ComponentFactory>() {
					@Override
					public boolean apply(ComponentFactory input) {
						return input != ignoreFactory;
					}
				}));
	}

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        renderHead(response, SelectorPanelAbstract.class);
    }


}