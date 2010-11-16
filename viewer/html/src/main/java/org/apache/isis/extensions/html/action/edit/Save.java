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


package org.apache.isis.extensions.html.action.edit;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.runtime.context.IsisContext;
import org.apache.isis.core.runtime.transaction.IsisTransactionManager;
import org.apache.isis.extensions.html.action.Action;
import org.apache.isis.extensions.html.component.Page;
import org.apache.isis.extensions.html.context.Context;
import org.apache.isis.extensions.html.request.ForwardRequest;
import org.apache.isis.extensions.html.request.Request;
import org.apache.isis.runtime.persistence.PersistenceSession;



public class Save implements Action {

    public void execute(final Request request, final Context context, final Page page) {
    	
        final ObjectAdapter adapter = context.getMappedObject(request.getObjectId());
        
        // xactn mgmt now done by PersistenceSession#makePersistent()
        // getTransactionManager().startTransaction();
        getPersistenceSession().makePersistent(adapter);
        // getTransactionManager().endTransaction();
        
        // return to view
        request.forward(ForwardRequest.viewObject(request.getObjectId()));
    }

    
    
    ///////////////////////////////////////////////////////
    // Dependencies (from context)
    ///////////////////////////////////////////////////////

    private IsisTransactionManager getTransactionManager() {
        return getPersistenceSession().getTransactionManager();
    }

    private PersistenceSession getPersistenceSession() {
        return IsisContext.getPersistenceSession();
    }

    public String name() {
        return "save";
    }

}

