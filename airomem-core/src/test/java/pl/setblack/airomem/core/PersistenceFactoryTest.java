package pl.setblack.airomem.core;

/*
 *  Created by Jarek Ratajski
 */
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
        try {
            FileUtils.deleteDirectory(new File(PersistenceFactory.STORAGE_FOLDER));
        } catch (IOException ex) {
            Logger.getLogger(PersistenceFactoryTest.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    private PersistenceController<StorableObject, Map<String, String>> createController() {
        PersistenceController<StorableObject, Map<String, String>> controller = factory
                .init("sample", createTestObject());
        return controller;
    }

    @Test
    public void testQuery() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        String value = controller.query((map) -> map.get("key:1"));
        assertEquals("val:1", value);
    }

    @Test
    public void testReload() {
        PersistenceController<StorableObject, Map<String, String>> controller = createController();
        controller.close();
        PersistenceController<StorableObject, Map<String, String>> controller2 = factory.load("sample", StorableObject.class);
        String value = controller2.query((map) -> map.get("key:2"));
        assertEquals("val:2", value);
    }

}
