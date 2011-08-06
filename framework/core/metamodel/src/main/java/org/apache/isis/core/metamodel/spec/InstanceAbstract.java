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

package org.apache.isis.core.metamodel.spec;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;

public abstract class InstanceAbstract implements Instance {

    private final ObjectAdapter owner;
    private Specification specification;

    protected InstanceAbstract() {
        this(null, null);
    }

    protected InstanceAbstract(final ObjectAdapter owner) {
        this(owner, null);
    }

    protected InstanceAbstract(final ObjectAdapter owner, final Specification specification) {
        this.owner = owner;
        this.specification = specification;
    }

    @Override
    public final ObjectAdapter getOwner() {
        return owner;
    }

    @Override
    public Specification getSpecification() {
        if (specification == null) {
            specification = loadSpecification();
        }
        return specification;
    }

    /**
     * Allows the specification to be lazily loaded.
     */
    protected Specification loadSpecification() {
        return null;
    }

    /**
     * Allows subclasses to get specification without necessarily triggering {@link #loadSpecification() loading} if not
     * yet known.
     */
    protected final Specification getSpecificationNoLoad() {
        return specification;
    }

}