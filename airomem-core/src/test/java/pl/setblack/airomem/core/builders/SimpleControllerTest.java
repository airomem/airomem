/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.builders;

import pl.setblack.airomem.core.StorableObject;
import pl.setblack.airomem.core.SimpleController;
import pl.setblack.airomem.core.builders.PersistenceFactory;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jarek ratajski
 */
public class SimpleControllerTest {

    private PersistenceFactory factory;

    public SimpleControllerTest() {
    }

    @Before
    public void setUp() {
        AbstractPrevaylerTest.deletePrevaylerFolder();
        factory = new PersistenceFactory();
    }

    @Test
    public void testSimpleControllerCreation() {
        //GIVEN
        final SimpleController<HashMap<String, String>> simpleController = SimpleController.create("test", StorableObject.createTestHashMap());
        //THEN
        assertTrue(simpleController.isOpen());
    }

    @Test
    public void testSimpleControllerQuery() {
        //GIVEN
        final SimpleController<HashMap<String, String>> simpleController = SimpleController.create("test", StorableObject.createTestHashMap());
        //WHEN
        String val = simpleController.query(x -> x.get("key:2"));
        //THEN
        assertEquals("val:2", val);
    }

    @Test
    public void testSimpleControllerClose() {
        //GIVEN
        final SimpleController<HashMap<String, String>> simpleController = SimpleController.create("test", StorableObject.createTestHashMap());
        //WHEN
        simpleController.close();
        //THEN
        assertFalse(simpleController.isOpen());
    }

    @Test
    public void testSimpleControllerShut() {
        //GIVEN
        final SimpleController<HashMap<String, String>> simpleController = SimpleController.create("test", StorableObject.createTestHashMap());
        //WHEN
        simpleController.shut();
        //THEN
        assertFalse(simpleController.isOpen());
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadWithNoStoredSystemShouldFail() {
        //WHEN
        SimpleController.load("test");
    }

    @Test
    public void testExecutePerformed() {
        //GIVEN
        try (
                final SimpleController<HashMap<String, String>> simpleController = SimpleController.create("test", StorableObject.createTestHashMap());) {
            //WHEN
            simpleController.executeAndQuery((x, ctx) -> x.put("key:1", "otherVal"));
            //THEN
            assertEquals("otherVal", simpleController.query(x -> x.get("key:1")));
        }
    }

    @Test
    public void testExecuteAndQueryWithoutContextPerformed() {
        //GIVEN
        try (
                final SimpleController<HashMap<String, String>> simpleController = SimpleController.create("test", StorableObject.createTestHashMap());) {
            //WHEN
            simpleController.executeAndQuery((x) -> x.put("key:1", "otherVal"));
            //THEN
            assertEquals("otherVal", simpleController.query(x -> x.get("key:1")));
        }
    }

    @Test
    public void testExecuteWithoutContextPerformed() {
        //GIVEN
        try (
                final SimpleController<HashMap<String, String>> simpleController = SimpleController.create("test", StorableObject.createTestHashMap());) {
            //WHEN
            simpleController.execute((x) -> x.put("key:1", "otherVal"));
            //THEN
            assertEquals("otherVal", simpleController.query(x -> x.get("key:1")));
        }
    }

    @Test
    public void testExecutePerformedAndStored() {
        //GIVEN
        try (
                final SimpleController<HashMap<String, String>> simpleController = SimpleController.create("test", StorableObject.createTestHashMap());) {
            simpleController.execute((x, ctx) -> x.put("key:1", "otherVal"));
        }

        //WHEN
        try (
                final SimpleController<HashMap<String, String>> simpleController = SimpleController.load("test")) {
            //THEN
            assertEquals("otherVal", simpleController.query(x -> x.get("key:1")));
        }
    }

    @Test
    public void schouldExistsAfterCreation() {
        //GIVEN
        try (
                final SimpleController<HashMap<String, String>> simpleController = SimpleController.create("test", StorableObject.createTestHashMap());) {
            simpleController.execute((x, ctx) -> x.put("key:1", "otherVal"));
        }
        //when
        //then
        assertTrue(SimpleController.exists("test"));
    }

    @Test
    public void schouldNotExistsAfterCreation() {
        //GIVEN

        //when
        //then
        assertFalse(SimpleController.exists("test"));
    }

    @Test
    public void schouldCreateNewSystem() {
        //GIVEN
        try (
                final SimpleController<HashMap<String, String>> simpleController = SimpleController.loadOptional("test", () -> StorableObject.createTestHashMap());) {
            //WHEN
            final String val = simpleController.query(x -> x.get("key:1"));
            //THEN
            assertEquals("val:1", val);
        }
    }

    @Test
    public void schouldLoadOldSystemIfExists() {
        //GIVEN
        try (
                final SimpleController<HashMap<String, String>> simpleController = SimpleController.create("test", StorableObject.createTestHashMap());) {
            simpleController.execute((x, ctx) -> x.put("key:1", "otherVal"));
        }
        try (
                final SimpleController<HashMap<String, String>> simpleController = SimpleController.loadOptional("test", () -> StorableObject.createTestHashMap());) {
            //WHEN
            final String val = simpleController.query(x -> x.get("key:1"));
            //THEN
            assertEquals("otherVal", val);
        }

    }

}
