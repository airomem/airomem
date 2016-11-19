/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.builders;

import pl.setblack.airomem.core.StorableObject;
import pl.setblack.airomem.core.Persistent;

import java.io.File;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jarek ratajski
 */
public class PersistentTest {

    private PersistenceFactory factory;

    public PersistentTest() {
    }

    @Before
    public void setUp() {
        File localFolder = new File("prevayler/");
        localFolder.mkdir();
        System.setProperty("user.home",  localFolder.getAbsolutePath());
        AbstractPrevaylerTest.deletePrevaylerFolder();
        factory = new PersistenceFactory();
    }

    @Test
    public void testSimpleControllerCreation() {
        //GIVEN
        final Persistent<HashMap<String, String>> persistent = Persistent.create("test", StorableObject.createTestHashMap());
        //THEN
        assertTrue(persistent.isOpen());
    }

    @Test
     public void testSimpleControllerQuery() {
        //GIVEN
        final Persistent<HashMap<String, String>> persistent = Persistent.create("test", StorableObject.createTestHashMap());
        //WHEN
        String val = persistent.query(x -> x.get("key:2"));
        //THEN
        assertEquals("val:2", val);
    }

    @Test
    public void testCreateTwice() {
        //GIVEN
        final Persistent<HashMap<String, String>> persistent1 = Persistent.create("test", StorableObject.createTestHashMap());
        //WHEN
        final Persistent<HashMap<String, String>> persistent2 = Persistent.create("test", StorableObject.createTestHashMap());
        String val = persistent2.query(x -> x.get("key:2"));
        //THEN
        assertEquals("val:2", val);
    }

    @Test
    public void testSimpleControlleReadOnly() {
        //GIVEN
        final Persistent<HashMap<String, String>> persistent = Persistent.create("test", StorableObject.createTestHashMap());
        //WHEN
        String val = persistent.readOnly().get("key:2");
        //THEN
        assertEquals("val:2", val);
    }

    @Test
    public void testSimpleControllerClose() {
        //GIVEN
        final Persistent<HashMap<String, String>> persistent = Persistent.create("test", StorableObject.createTestHashMap());
        //WHEN
        persistent.close();
        //THEN
        assertFalse(persistent.isOpen());
    }

    @Test
    public void testSimpleControllerShut() {
        //GIVEN
        final Persistent<HashMap<String, String>> persistent = Persistent.create("test", StorableObject.createTestHashMap());
        //WHEN
        persistent.shut();
        //THEN
        assertFalse(persistent.isOpen());
    }


    @Test(expected = IllegalStateException.class)
    public void testLoadWithNoStoredSystemShouldFail() {
        //WHEN
        Persistent.load("test");
    }

    @Test
    public void testExecutePerformed() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create("test", StorableObject.createTestHashMap());) {
            //WHEN
            persistent.executeAndQuery((x, ctx) -> x.put("key:1", "otherVal"));
            //THEN
            assertEquals("otherVal", persistent.query(x -> x.get("key:1")));
        }
    }

    @Test
    public void testExecuteAndQueryWithoutContextPerformed() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create("test", StorableObject.createTestHashMap());) {
            //WHEN
            persistent.executeAndQuery((x) -> x.put("key:1", "otherVal"));
            //THEN
            assertEquals("otherVal", persistent.query(x -> x.get("key:1")));
        }
    }

    @Test
    public void testExecuteWithoutContextPerformed() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create("test", StorableObject.createTestHashMap());) {
            //WHEN
            persistent.execute((x) -> x.put("key:1", "otherVal"));
            //THEN
            assertEquals("otherVal", persistent.query(x -> x.get("key:1")));
        }
    }

    @Test
    public void testExecutePerformedAndStored() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create("test", StorableObject.createTestHashMap());) {
            persistent.execute((x, ctx) -> x.put("key:1", "otherVal"));
        }

        //WHEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.load("test")) {
            //THEN
            assertEquals("otherVal", persistent.query(x -> x.get("key:1")));
        }
    }

    @Test
    public void schouldExistsAfterCreation() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create("test", StorableObject.createTestHashMap());) {
            persistent.execute((x, ctx) -> x.put("key:1", "otherVal"));
        }
        //when
        //then
        assertTrue(Persistent.exists("test"));
    }

    @Test
    public void schouldNotExistsAfterCreation() {
        //GIVEN

        //when
        //then
        assertFalse(Persistent.exists("test"));
    }

    @Test
    public void schouldCreateNewSystem() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.loadOptional("test", () -> StorableObject.createTestHashMap());) {
            //WHEN
            final String val = persistent.query(x -> x.get("key:1"));
            //THEN
            assertEquals("val:1", val);
        }
    }

    @Test
    public void schouldLoadOldSystemIfExists() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create("test", StorableObject.createTestHashMap());) {
            persistent.execute((x, ctx) -> x.put("key:1", "otherVal"));
        }
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.loadOptional("test", () -> StorableObject.createTestHashMap());) {
            //WHEN
            final String val = persistent.query(x -> x.get("key:1"));
            //THEN
            assertEquals("otherVal", val);
        }
    }

    @Test
    public void schouldForgetChangesDoneInQueries() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create("test", StorableObject.createTestHashMap());) {
            persistent.<Void>query((x) -> { x.put("key:1", "otherVal"); return null;} );
        }
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.loadOptional("test", () -> StorableObject.createTestHashMap());) {
            //WHEN
            final String val = persistent.query(x -> x.get("key:1"));
            //THEN
            assertEquals("val:1", val);
        }
    }


}
