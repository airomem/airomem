/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.builders;

import pl.setblack.airomem.core.StorableObject;
import java.io.File;
import java.util.Map;
import java.util.function.Supplier;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.disk.PersistenceDiskHelper;
import pl.setblack.badass.Politician;

/**
 *
 */
public class PersistenceFactoryTest {

    private PersistenceFactory factory;

    private static final String SYSTEM_NAME = "sample";

    public PersistenceFactoryTest() {

    }

    @Before
    public void setUp() {
        factory = new PersistenceFactory();
        deletePrevaylerFolder();

    }

    private void deletePrevaylerFolder() {
        PersistenceDiskHelper.delete("sample");
    }

    @After
    public void tearDown() {
        deletePrevaylerFolder();
    }

    @Test
    public void testInitialization() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        assertNotNull(controller);
    }

    @Test
    public void testOptionalInitialization() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        assertNotNull(controller);
        this.factory.initOptional(SYSTEM_NAME, (Supplier< StorableObject>) () -> {
            throw new RuntimeException("this should not be called in fact");
        });
        controller.close();
        tearDown();
        controller = this.factory.initOptional(SYSTEM_NAME, ()
                -> StorableObject.createTestObject()
        );
        String value = controller.query((map) -> map.get("key:3"));
        assertEquals("val:3", value);
    }

    @Test
    public void testQuery() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        String value = controller.query((map) -> map.get("key:1"));
        assertEquals("val:1", value);
    }

    @Test(expected = NullPointerException.class)
    public void testClose() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        controller.close();

        controller.query((map) -> map.get("key:2"));

    }

    @Test
    public void shouldCloseNicelyMutipleTimes() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        controller.close();
        controller.close();
        controller.close();
    }

    @Test(expected = NullPointerException.class)
    public void testShut() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        controller.shut();
        controller.query((map) -> map.get("key:2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testLoad() {
        //WHEN
        factory.load("sample");
    }

    @Test
    public void testReload() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        controller.close();
        PersistenceController<StorableObject, Map<String, String>> controller2 = factory.load("sample");
        String value = controller2.query((map) -> map.get("key:2"));
        assertEquals("val:2", value);
    }

    /**
     * This test does not do much.
     * Would be good to fix it later.
     */
    @Test
    public void testSnapshot() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        controller.snapshot();
        PersistenceController<StorableObject, Map<String, String>> controller2 = factory.load("sample");
        String value = controller2.query((map) -> map.get("key:2"));
        assertEquals("val:2", value);
    }

    @Test
    public void testExecuteStored() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        controller.execute((x) -> x.internalMap.put("key:2", "dzikb"));
        assertEquals("dzikb", controller.query((map) -> map.get("key:2")));
        controller.execute((x) -> x.internalMap.put("key:2", "dzikc"));
        controller.shut();
        PersistenceController<StorableObject, Map<String, String>> controller2 = factory.load("sample");
        String value = controller2.query((map) -> map.get("key:2"));
        assertEquals("dzikc", value);
    }

    @Test
    public void testExecuteWithDate() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        assertNull(controller.query((map) -> map.get("ctxtest")));

        controller.execute((x, ctx) -> x.internalMap.put("ctxtest", ctx.time.toString()));
        final String date = controller.query((map) -> map.get("ctxtest"));
        assertNotNull(date);

        controller.shut();
        PersistenceController<StorableObject, Map<String, String>> controller2 = factory.load("sample");
        String value = controller2.query((map) -> map.get("ctxtest"));
        assertEquals(date, value);
    }

    @Test
    public void testExistMethod() {
        assertFalse(this.factory.exists("mysystem"));
        PersistenceController<StorableObject, Map<String, String>> controller = factory
                .init("mysystem", StorableObject.createTestObject());
        assertTrue(this.factory.exists("mysystem"));
    }

    private PersistenceController<StorableObject, Map<String, String>> createController() {
        PersistenceController<StorableObject, Map<String, String>> controller = factory
                .init(SYSTEM_NAME, StorableObject.createTestObject());
        return controller;
    }

}
