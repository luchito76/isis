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


package org.apache.isis.extensions.mongo;

import org.apache.isis.alternatives.objectstore.nosql.mongo.MongoStateWriter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class MongoStateWriterTest {

    private static final String SPEC_NAME = "org.test.Object";
    private DB testDb;
    private MongoStateWriter writer;

    @Before
    public void setup() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);

        Mongo m = new Mongo();
        m.dropDatabase("mydb");
        testDb = m.getDB("mydb");
        
        writer = new MongoStateWriter(testDb, SPEC_NAME);
    }
    
    @Test
    public void flushSavesObject() throws Exception {
        writer.flush();
        
        DBCollection instances = testDb.getCollection(SPEC_NAME);
        assertEquals(1, instances.getCount());
    }
    
    @Test
    public void objectNotSavedUntilFlush() throws Exception {
        writer.writeField("number", 1023);
        writer.writeField("string", "testing");
        
        DBCollection instances = testDb.getCollection(SPEC_NAME);
        assertEquals(0, instances.getCount());
    }
    
    @Test
    public void serialNumberNotStored() throws Exception {
        writer.writeId("D01");
        writer.flush();
        
        DBCollection instances = testDb.getCollection(SPEC_NAME);
        assertEquals(1, instances.getCount());
        DBObject object = instances.findOne();
        assertNotNull(object.get("_id"));
        assertEquals("D01", object.get("_id"));
        assertEquals(1, object.keySet().size());
    }
    
    @Test
    public void writeFields() throws Exception {
        writer.writeType(SPEC_NAME);
        writer.writeField("number", 1023);
        writer.writeField("string", "testing");
        writer.flush();
        
        DBCollection instances = testDb.getCollection(SPEC_NAME);
        assertEquals(1, instances.getCount());
        DBObject object = instances.findOne();
        assertEquals(SPEC_NAME, object.get("_type"));
        assertEquals("1023", object.get("number"));
        assertEquals("testing", object.get("string"));
    }

    
    @Test
    public void writeFields2() throws Exception {
        writer.writeId("3");
        writer.writeType(SPEC_NAME);
        writer.flush();

        writer.writeField("number", 1023);
        writer.writeField("string", "testing");
        writer.flush();

        DBCollection instances = testDb.getCollection(SPEC_NAME);
        assertEquals(1, instances.getCount());
        DBObject object = instances.findOne();
        assertEquals(SPEC_NAME, object.get("_type"));
        assertEquals("1023", object.get("number"));
        assertEquals("testing", object.get("string"));
    }

}


