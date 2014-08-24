/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core;

import java.io.File;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.prevayler.PrevaylerFactory;
import pl.setblack.badass.Politician;

/**
 *
 * @author jarek ratajski
 */
public class PrevaylerBuilderTest {

    public PrevaylerBuilderTest() {
    }

    @Before
    public void setUp() {
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

    @Test(expected = IllegalStateException.class)
    public void shouldFailIfInitialSystemNotGiven() {
        //WHEN
        try (final PersistenceController ctrl
                = PrevaylerBuilder.newBuilder().build()) {
        }
    }

    @Test
    public void shouldCreateInitialSimpleSystemWhenSupplierGiven() {
        //WHEN
        try (
                final PersistenceController ctrl = PrevaylerBuilder.newBuilder().useSupplier(() -> StorableObject.createTestObject()).build();) {
            //THEN
            Assert.assertNotNull(ctrl);
        }
    }

    @Test
    public void shouldReallyExecuteValues() {
        //WHEN
        try (
                final PersistenceController<StorableObject, Map<String, String>> ctrl = PrevaylerBuilder.newBuilder().useSupplier(() -> StorableObject.createTestObject()).build();) {
            ctrl.execute((x) -> x.internalMap.put("myKey", "myVal"));
            //THEN
            assertEquals("myVal", ctrl.query((x) -> x.get("myKey")));
        }
    }

    @Test
    public void shouldUseGivenFolder() {
        //GIVEN
        final PrevaylerBuilder<StorableObject, Map<String, String>> builder = PrevaylerBuilder.newBuilder()
                .useSupplier(StorableObject::createTestObject)
                .withFolder("test1");

        //WHEN
        try (
                final PersistenceController<StorableObject, Map<String, String>> ctrl = builder.build();) {
            ctrl.execute((x) -> x.internalMap.put("myKey", "myVal"));
            //THEN
            assertTrue(new PersistenceFactory().exists("test1"));
        }
    }

}
