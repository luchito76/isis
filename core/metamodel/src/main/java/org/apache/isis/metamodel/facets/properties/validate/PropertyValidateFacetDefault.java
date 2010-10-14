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


package org.apache.isis.metamodel.facets.properties.validate;

import org.apache.isis.applib.events.ValidityEvent;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.facets.FacetAbstract;
import org.apache.isis.metamodel.facets.FacetHolder;
import org.apache.isis.metamodel.interactions.ValidityContext;


/**
 * Non checking property validation facet that provides default behaviour for all properties.
 */
public class PropertyValidateFacetDefault extends FacetAbstract implements PropertyValidateFacet {

    public String invalidates(final ValidityContext<? extends ValidityEvent> ic) {
        return null;
    }

    public PropertyValidateFacetDefault(final FacetHolder holder) {
        super(PropertyValidateFacet.class, holder, false);
    }

    public String invalidReason(final ObjectAdapter target, final ObjectAdapter proposedValue) {
        return null;
    }

}
