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


package org.apache.isis.core.progmodel.facets.object.ident.plural;

import java.lang.reflect.Method;

import org.apache.isis.core.metamodel.facets.Facet;
import org.apache.isis.core.metamodel.spec.feature.ObjectFeatureType;
import org.apache.isis.core.progmodel.facets.object.ident.plural.PluralFacet;
import org.apache.isis.core.progmodel.facets.object.ident.plural.PluralFacetViaMethod;
import org.apache.isis.core.progmodel.facets.object.ident.plural.PluralMethodFacetFactory;
import org.apache.isis.metamodel.facets.AbstractFacetFactoryTest;


public class PluralMethodFacetFactoryTest extends AbstractFacetFactoryTest {

    private PluralMethodFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new PluralMethodFacetFactory();
    }

    @Override
    protected void tearDown() throws Exception {
        facetFactory = null;
        super.tearDown();
    }

    @Override
    public void testFeatureTypes() {
        final ObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertTrue(contains(featureTypes, ObjectFeatureType.OBJECT));
        assertFalse(contains(featureTypes, ObjectFeatureType.PROPERTY));
        assertFalse(contains(featureTypes, ObjectFeatureType.COLLECTION));
        assertFalse(contains(featureTypes, ObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, ObjectFeatureType.ACTION_PARAMETER));
    }

    public static class Customer {
        public static String pluralName() {
            return "Some plural name";
        }
    }

    public void testPluralNameMethodPickedUpOnClassAndMethodRemoved() {
        final Method pluralNameMethod = findMethod(Customer.class, "pluralName");

        facetFactory.process(Customer.class, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PluralFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PluralFacetViaMethod);
        final PluralFacetViaMethod pluralFacet = (PluralFacetViaMethod) facet;
        assertEquals("Some plural name", pluralFacet.value());

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(pluralNameMethod));
    }

}
