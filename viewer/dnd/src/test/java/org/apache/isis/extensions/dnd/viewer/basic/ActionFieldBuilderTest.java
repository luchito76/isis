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


package org.apache.isis.extensions.dnd.viewer.basic;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.easymock.MockControl;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.isis.core.metamodel.authentication.AuthenticationSession;
import org.apache.isis.core.metamodel.config.IsisConfiguration;
import org.apache.isis.core.metamodel.config.internal.PropertiesConfiguration;
import org.apache.isis.core.metamodel.specloader.SpecificationLoader;
import org.apache.isis.core.runtime.authentication.AuthenticationManager;
import org.apache.isis.core.runtime.authentication.standard.exploration.ExplorationSession;
import org.apache.isis.core.runtime.authorization.AuthorizationManager;
import org.apache.isis.core.runtime.context.IsisContext;
import org.apache.isis.core.runtime.context.IsisContextStatic;
import org.apache.isis.core.runtime.imageloader.TemplateImageLoader;
import org.apache.isis.core.runtime.userprofile.UserProfile;
import org.apache.isis.core.runtime.userprofile.UserProfileLoader;
import org.apache.isis.extensions.dnd.DummyView;
import org.apache.isis.extensions.dnd.dialog.ActionFieldBuilder;
import org.apache.isis.extensions.dnd.view.Axes;
import org.apache.isis.extensions.dnd.view.Content;
import org.apache.isis.extensions.dnd.view.View;
import org.apache.isis.extensions.dnd.view.ViewFactory;
import org.apache.isis.runtime.persistence.PersistenceSessionFactory;
import org.apache.isis.runtime.session.IsisSessionFactoryDefault;
import org.apache.isis.runtime.system.DeploymentType;


@RunWith(JMock.class)
public class ActionFieldBuilderTest {
    private ActionFieldBuilder builder;

    private Mockery mockery = new JUnit4Mockery();
    
    private IsisConfiguration configuration;
    private List<Object> servicesList;
    protected TemplateImageLoader mockTemplateImageLoader;
    protected SpecificationLoader mockSpecificationLoader;
    protected PersistenceSessionFactory mockPersistenceSessionFactory;
    private UserProfileLoader mockUserProfileLoader;
    protected AuthenticationManager mockAuthenticationManager;
    protected AuthorizationManager mockAuthorizationManager;
    

    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);

        configuration = new PropertiesConfiguration();
        servicesList = Collections.emptyList();
        
        mockTemplateImageLoader = mockery.mock(TemplateImageLoader.class);
        mockSpecificationLoader = mockery.mock(SpecificationLoader.class);
        mockPersistenceSessionFactory = mockery.mock(PersistenceSessionFactory.class);
        mockUserProfileLoader = mockery.mock(UserProfileLoader.class);
        mockAuthenticationManager = mockery.mock(AuthenticationManager.class);
        mockAuthorizationManager = mockery.mock(AuthorizationManager.class);
        
        mockery.checking(new Expectations(){{
            ignoring(mockSpecificationLoader);
            ignoring(mockPersistenceSessionFactory);
            
            one(mockUserProfileLoader).getProfile(with(any(AuthenticationSession.class)));
            will(returnValue(new UserProfile()));

            ignoring(mockTemplateImageLoader);
            ignoring(mockAuthenticationManager);
            ignoring(mockAuthorizationManager);
        }});
        
        final ViewFactory subviewSpec = new ViewFactory() {
            public View createView(final Content content, Axes axes, int fieldNumber) {
                return new DummyView();
            }
        };


		final IsisSessionFactoryDefault sessionFactory = new IsisSessionFactoryDefault(
		        DeploymentType.EXPLORATION, 
		        configuration, 
		        mockTemplateImageLoader, 
		        mockSpecificationLoader, 
		        mockAuthenticationManager, 
		        mockAuthorizationManager,
		        mockUserProfileLoader, 
		        mockPersistenceSessionFactory, servicesList);
        
        IsisContext.setConfiguration(sessionFactory.getConfiguration());
		IsisContextStatic.createRelaxedInstance(sessionFactory);
        IsisContextStatic.openSession(new ExplorationSession());
        
        builder = new ActionFieldBuilder(subviewSpec);

    }

    @After
    public void tearDown() {
        IsisContext.closeSession();
    }

    @Test
    public void testUpdateBuild() {
        final MockControl control = MockControl.createControl(View.class);
        final View view = (View) control.getMock();

        control.expectAndDefaultReturn(view.getView(), view);
        control.expectAndDefaultReturn(view.getContent(), null);

        /*
         * DummyView[] views = new DummyView[2]; views[1] = new DummyView(); views[1].setupContent(new
         * ObjectParameter("name", null, null, false, 1, actionContent)); view.setupSubviews(views);
         */

        control.replay();

        // builder.build(view);

        control.verify();
    }

    /*
     * // TODO fails on server as cant load X11 for Text class public void xxxtestNewBuild() {
     * view.setupSubviews(new View[0]);
     * 
     * view.addAction("add TextView0 null"); view.addAction("add MockView1/LabelBorder"); view.addAction("add
     * MockView2/LabelBorder");
     * 
     * builder.build(view);
     * 
     * view.verify(); } public void xxxtestUpdateBuildWhereParameterHasChangedFromNullToAnObject() {
     * DummyView[] views = new DummyView[2]; views[1] = new DummyView(); ObjectParameter objectParameter = new
     * ObjectParameter("name", null, null, false, 1, actionContent); views[1].setupContent(objectParameter);
     * view.setupSubviews(views);
     * 
     * actionContent.setParameter(0, new DummyObjectAdapter());
     * 
     * view.addAction("replace MockView1 with MockView2/LabelBorder");
     * 
     * builder.build(view);
     * 
     * view.verify(); }
     * 
     * public void xxxtestUpdateBuildWhereParameterHasChangedFromAnObjectToNull() { DummyView[] views = new
     * DummyView[2]; views[1] = new DummyView(); ObjectParameter objectParameter = new ObjectParameter("name",
     * new DummyObjectAdapter(), null, false, 1, actionContent); views[1].setupContent(objectParameter);
     * view.setupSubviews(views);
     * 
     * objectParameter.setObject(null);
     * 
     * view.addAction("replace MockView1 with MockView2/LabelBorder");
     * 
     * builder.build(view);
     * 
     * view.verify(); }
     * 
     * public void xxxtestUpdateBuildWhereParameterHasChangedFromOneObjectToAnother() { DummyView[] views =
     * new DummyView[2]; views[1] = new DummyView(); ObjectParameter objectParameter = new
     * ObjectParameter("name", new DummyObjectAdapter(), null, false, 1, actionContent);
     * views[1].setupContent(objectParameter); view.setupSubviews(views);
     * 
     * objectParameter.setObject(new DummyObjectAdapter());
     * 
     * view.addAction("replace MockView1 with MockView2/LabelBorder");
     * 
     * builder.build(view);
     * 
     * view.verify(); }
     * 
     * public void xxtestUpdateBuildWhereParameterObjectSetButToSameObject() { DummyView[] views = new
     * DummyView[2]; views[1] = new DummyView(); DummyObjectAdapter dummyObjectAdapter = new DummyObjectAdapter();
     * ObjectParameter objectParameter = new ObjectParameter("name", dummyObjectAdapter, null, false, 1,
     * actionContent); views[1].setupContent(objectParameter); view.setupSubviews(views);
     * 
     * actionContent.setParameter(0, dummyObjectAdapter); // objectParameter.setObject(dummyObjectAdapter);
     * 
     * builder.build(view);
     * 
     * view.verify(); } }
     * 
     * class MockActionHelper extends ActionHelper {
     * 
     * protected MockActionHelper( ObjectAdapter target, Action action, String[] labels, ObjectAdapter[] parameters,
     * ObjectSpecification[] parameterTypes, boolean[] required) { super(target, action, labels,
     * parameters, parameterTypes, required); }
     */
}
