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

package org.apache.isis.viewer.wicket.ui.components.collectioncontents.ajaxtable;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Ordering;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.feature.ObjectAssociation;
import org.apache.isis.viewer.wicket.model.models.EntityCollectionModel;
import org.apache.isis.viewer.wicket.model.models.EntityModel;

/**
 * Part of the {@link AjaxFallbackDefaultDataTable} API.
 */
public class CollectionContentsSortableDataProvider extends SortableDataProvider<ObjectAdapter,String> {

    private static final long serialVersionUID = 1L;

    private final EntityCollectionModel model;

    public CollectionContentsSortableDataProvider(final EntityCollectionModel model) {
        this.model = model;
    }

    @Override
    public Iterator<ObjectAdapter> iterator(final long first, final long count) {
        List<ObjectAdapter> adapters = sortedIfRequired(model.getObject(), this.getSort());
        
        return adapters.subList((int)first, (int)(first + count)).iterator();
    }

    @Override
    public IModel<ObjectAdapter> model(final ObjectAdapter adapter) {
        return new EntityModel(adapter);
    }

    @Override
    public long size() {
        return model.getObject().size();
    }

    @Override
    public void detach() {
        super.detach();
        model.detach();
    }

    
    private List<ObjectAdapter> sortedIfRequired(List<ObjectAdapter> adapters, final SortParam<String> sort) {
        if(sort == null) {
            return adapters;
        }
        
        final ObjectSpecification elementSpec = model.getTypeOfSpecification();
        
        final String sortPropertyId = sort.getProperty();
        final ObjectAssociation sortProperty = elementSpec.getAssociation(sortPropertyId);
        if(sortProperty == null) {
            return adapters;
        }

        Ordering<ObjectAdapter> ordering = orderingBy(sortProperty, sort.isAscending());
        return ordering.sortedCopy(adapters);
    }

    public static Ordering<ObjectAdapter> orderingBy(final ObjectAssociation sortProperty, final boolean ascending) {
        final Ordering<ObjectAdapter> ordering = new Ordering<ObjectAdapter>(){
    
            @Override
            public int compare(final ObjectAdapter p, final ObjectAdapter q) {
                final ObjectAdapter pSort = sortProperty.get(p);
                final ObjectAdapter qSort = sortProperty.get(q);
                Ordering<ObjectAdapter> naturalOrdering = 
                        ascending 
                            ? ORDERING_BY_NATURAL.nullsFirst() 
                            : ORDERING_BY_NATURAL.nullsLast();
                return naturalOrdering.compare(pSort, qSort);
            }
        };
        return ascending?ordering:ordering.reverse();
    }

    public static Ordering<ObjectAdapter> ORDERING_BY_NATURAL = new Ordering<ObjectAdapter>(){
        @Override
        public int compare(final ObjectAdapter p, final ObjectAdapter q) {
            final Object pPojo = p.getObject();
            final Object qPojo = q.getObject();
            if(!(pPojo instanceof Comparable) || !(qPojo instanceof Comparable)) {
                return 0;
            } 
            return naturalOrdering(pPojo, qPojo);
        }
        @SuppressWarnings("rawtypes")
        private int naturalOrdering(final Object pPojo, final Object qPojo) {
            Comparable pComparable = (Comparable) pPojo;
            Comparable qComparable = (Comparable) qPojo;
            return Ordering.natural().compare(pComparable, qComparable);
        }
    }; 

}