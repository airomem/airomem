package pl.setblack.airomem.core;

/*
 *  Created by Jarek Ratajski
 */
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import pl.setblack.badass.Politician;

/**
 *
 * @author Kanapka
 */
public class PersistenceFactoryTest {

    private PersistenceFactory factory;

    public PersistenceFactoryTest() {
    }

    @Before
    public void setUp() {
        factory = new PersistenceFactory();
        deletePrevaylerFolder();

    }

    private void deletePrevaylerFolder() {
        Politician.beatAroundTheBush(() -> {
            FileUtils.deleteDirectory(new File(PersistenceFactory.STORAGE_FOLDER));
        });
    }

    @After
    public void tearDown() {
        deletePrevaylerFolder();
    }

    private HashMap<String, String> createTestHashMap() {
        final HashMap<String, String> result = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            result.put("key:" + i, "val:" + i);
        }
        return result;
    }

    private StorableObject createTestObject() {
        return new StorableObject(createTestHashMap());
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
                -> createTestObject()
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

    @Test(expected = NullPointerException.class)
    public void testShut() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        controller.shut();
        controller.query((map) -> map.get("key:2"));
    }

    @Test
    public void testReload() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        controller.close();
        PersistenceController<StorableObject, Map<String, String>> controller2 = factory.load("sample", StorableObject.class);
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
        PersistenceController<StorableObject, Map<String, String>> controller2 = factory.load("sample", StorableObject.class);
        String value = controller2.query((map) -> map.get("key:2"));
        assertEquals("dzikc", value);

    }

    @Test
    public void testExistMethod() {
        assertFalse(this.factory.exists("mysystem"));
        PersistenceController<StorableObject, Map<String, String>> controller = factory
                .init("mysystem", createTestObject());
        assertTrue(this.factory.exists("mysystem"));
    }

    private PersistenceController<StorableObject, Map<String, String>> createController() {
        PersistenceController<StorableObject, Map<String, String>> controller = factory
                .init(SYSTEM_NAME, createTestObject());
        return controller;
    }
    private static final String SYSTEM_NAME = "sample";
}
