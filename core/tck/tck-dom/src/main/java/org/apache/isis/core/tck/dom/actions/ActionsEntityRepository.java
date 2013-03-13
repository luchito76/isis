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

package org.apache.isis.core.tck.dom.actions;

import java.util.List;

import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MustSatisfy;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.query.Query;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.spec.Specification;
import org.apache.isis.core.tck.dom.AbstractEntityRepository;

@Named("ActionsEntities")
@ObjectType("ActionsEntities")
public class ActionsEntityRepository extends AbstractEntityRepository<ActionsEntity> {

    public ActionsEntityRepository() {
        super(ActionsEntity.class, "ActionsEntities");
    }

    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "1")
    public ActionsEntity findById(int id) {
        final Query<ActionsEntity> query = 
                new QueryDefault<ActionsEntity>(ActionsEntity.class, ActionsEntity.class.getName() + "#pk", "id", id);
        return this.firstMatch(query);
    }

    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "1")
    public List<ActionsEntity> subList(
            @MustSatisfy(IntegerCannotBeNegative.class)
            @Named("from") int from, 
            @MustSatisfy(IntegerCannotBeNegative.class)
            @Named("to") int to) {
        List<ActionsEntity> list = list();
        int toChecked = Math.min(to, list.size());
        int fromChecked = Math.min(from, toChecked);
        return list.subList(fromChecked, toChecked);
    }
    public String validateSubList(final int from, final int to) {
        if(from > to) {
            return "'from' cannot be larger than 'to'";
        }
        return null;
    }

    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "1")
    public boolean contains(@Named("searchFor") ActionsEntity entity, @Named("from") int from, @Named("to") int to) {
        List<ActionsEntity> list = subList(from, to);
        return list.contains(entity);
    }

    public static class IntegerCannotBeNegative implements Specification {
        @Override
        public String satisfies(Object obj) {
            if(!(obj instanceof Integer)) {
                return null;
            } 
            Integer integer = (Integer) obj;
            return integer.intValue() < 0? "Cannot be less than zero": null;
        }
    }
}