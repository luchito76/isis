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


package org.apache.isis.extensions.dnd.view.debug;

import org.apache.isis.core.commons.debug.DebugInfo;
import org.apache.isis.core.commons.debug.DebugString;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.runtime.util.Dump;


public class DebugObjectSpecification implements DebugInfo {
    private final ObjectSpecification specification;

    public DebugObjectSpecification(final ObjectAdapter object) {
        this.specification = object.getSpecification();
    }

    public DebugObjectSpecification(final ObjectSpecification object) {
        this.specification = object;
    }

    public void debugData(final DebugString debug) {
        if (specification == null) {
            debug.appendln("no specfication");
        } else {
            Dump.specification(specification, debug);
        }
    }

    public String debugTitle() {
        return "Object Specification";
    }

}
