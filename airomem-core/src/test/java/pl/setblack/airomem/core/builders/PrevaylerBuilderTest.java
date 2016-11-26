/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.builders;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.RestoreException;
import pl.setblack.airomem.core.StorableObject;
import pl.setblack.airomem.core.VoidCommand;
import pl.setblack.airomem.core.disk.PersistenceDiskHelper;
import pl.setblack.badass.Politician;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * @author jarek ratajski
 */
public class PrevaylerBuilderTest {

    private static final AtomicReference<Boolean> failureMarker = new AtomicReference<>(Boolean.FALSE);


    final Path testFolder = new File("prevayler/test").toPath();

    private static boolean isFailureNeeded() {
        return failureMarker.get();
    }

    public PrevaylerBuilderTest() {
    }

    @Before
    public void setUp() {
        deletePrevaylerFolders();
        failureMarker.set(Boolean.FALSE);

    }

    private void deletePrevaylerFolders() {
        PersistenceDiskHelper.delete(this.testFolder);
        PersistenceDiskHelper.delete(PrevaylerBuilder.PREVAYLER_DEFAULT_FOLDER);
    }

    @After
    public void tearDown() {
        deletePrevaylerFolders();
    }

    @Test(expected = IllegalArgumentException.class)
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
                final PersistenceController<StorableObject> ctrl = PrevaylerBuilder.<StorableObject>newBuilder().useSupplier(() -> StorableObject.createTestObject()).build();) {
            ctrl.execute((x) -> x.internalMap.put("myKey", "myVal"));
            //THEN
            assertEquals("myVal", ctrl.query((x) -> x.getImmutable().get("myKey")));
        }
    }

    @Test
    public void shouldPreventValuesUponException() {
        //WHEN
        try (
                final PersistenceController<StorableObject> ctrl = PrevaylerBuilder.<StorableObject>newBuilder().useSupplier(() -> StorableObject.createTestObject()).build();) {
            ctrl.execute((x) -> x.internalMap.put("myKey", "myVal"));
            try {
                ctrl.execute((x) -> {
                    x.internalMap.put("myKey", "myBadVal");
                    throw new RuntimeException();
                });
            } catch (RuntimeException re) {
                //THEN
                assertEquals("myVal", ctrl.query((x) -> x.getImmutable().get("myKey")));
            }

        }
    }

    @Test
    public void shouldUseGivenFolder() {
        //GIVEN
        final PrevaylerBuilder<StorableObject> builder = PrevaylerBuilder.<StorableObject>newBuilder()
                .useSupplier(StorableObject::createTestObject)
                .withFolder(testFolder);

        //WHEN
        try (
                final PersistenceController<StorableObject> ctrl = builder.build();) {
            ctrl.execute((x) -> x.internalMap.put("myKey", "myVal"));
            //THEN
            assertTrue(PersistenceDiskHelper.exists(testFolder));
        }
    }

    @Test
    public void shouldStoreXML() throws IOException {
        //GIVEN
        final PrevaylerBuilder<StorableObject> builder = PrevaylerBuilder.<StorableObject>newBuilder()
                .useSupplier(StorableObject::createTestObject)
                .withFolder(testFolder);

        //WHEN
        try (
                final PersistenceController<StorableObject> ctrl = builder.build();) {
            ctrl.execute((x) -> x.internalMap.put("myKey", "myVal"));

            ctrl.snapshotXML(Paths.get("target/my.xml"));
        }
    }

    @Test
    public void shouldOverweriteSystem() {
        //GIVEN
        final PrevaylerBuilder<StorableObject> builder = PrevaylerBuilder.<StorableObject>newBuilder()
                .useSupplier(StorableObject::createTestObject)
                .forceOverwrite(true);
        //WHEN
        try (
                final PersistenceController<StorableObject> ctrl = builder.build();) {
            ctrl.execute((x) -> {
                x.internalMap.put("key:1", "myVal");
            });

        }

        Politician.beatAroundTheBush(() -> Thread.sleep(1000));
        //THEN
        try (
                final PersistenceController<StorableObject> ctrl = builder.build();) {
            final String val = ctrl.query(s -> s.getImmutable().get("key:1"));
            assertEquals("val:1", val);
        }
    }


    @Test
    public void shouldEraseSystem() {
        //GIVEN
        final PrevaylerBuilder<StorableObject> builder = PrevaylerBuilder.<StorableObject>newBuilder()
                .useSupplier(StorableObject::createTestObject)
                .forceOverwrite(false);
        //WHEN
        try (
                final PersistenceController<StorableObject> ctrl = builder.build();) {
            ctrl.execute((x) -> {
                x.internalMap.put("key:1", "myVal");
            });
            ctrl.erase();
        }

        Politician.beatAroundTheBush(() -> Thread.sleep(100));
        //THEN
        try (
                final PersistenceController<StorableObject> ctrl = builder.build();) {
            final String val = ctrl.query(s -> s.getImmutable().get("key:1"));
            assertEquals("val:1", val);
        }
    }


    @Test
    public void shouldWorkOnDisabledRFT() {
        //GIVEN
        final PrevaylerBuilder<StorableObject> builder = PrevaylerBuilder.<StorableObject>newBuilder()
                .useSupplier(StorableObject::createTestObject)
                .disableRoyalFoodTester();
        //WHEN

        final PersistenceController<StorableObject> ctrl = builder.build();
        try {
            ctrl.execute((x) -> {
                x.internalMap.put("key:1", "myVal");
                throw new RuntimeException();
            });

        } catch (RuntimeException e) {
        }

        assertEquals("myVal", ctrl.query(c -> c.getImmutable().get("key:1")));
    }

    @Test
    public void shouldUseJavaSerializerForJournaling() {
        //GIVEN
        final PrevaylerBuilder<StorableObject> builder = PrevaylerBuilder.<StorableObject>newBuilder()
                .useSupplier(StorableObject::createTestObject)
                .withJournalFastSerialization(false);
        StrangeTransaction.counter = 0;
        //WHEN
        try (
                final PersistenceController<StorableObject> ctrl = builder.build();) {
            ctrl.execute(new StrangeTransaction());
        }
        assertTrue(StrangeTransaction.counter > 0);
    }

    @Test
    public void shouldUseFastSerializerForJournaling() {
        //GIVEN
        final PrevaylerBuilder<StorableObject> builder = PrevaylerBuilder.<StorableObject>newBuilder()
                .useSupplier(StorableObject::createTestObject)
                .withJournalFastSerialization(true);
        StrangeTransaction.counter = 0;
        //WHEN
        try (
                final PersistenceController<StorableObject> ctrl = builder.build();) {
            ctrl.execute(new StrangeTransaction());
        }
        assertTrue(StrangeTransaction.counter == 0);
    }

    @Test(expected = RestoreException.class)
    public void shouldThrowRestoreExceptionWhenLoadFails() {
        //GIVEN
        final PrevaylerBuilder<StorableObject> builder = PrevaylerBuilder.<StorableObject>newBuilder()
                .useSupplier(StorableObject::createTestObject)
                .withJournalFastSerialization(false);
        try (
                final PersistenceController<StorableObject> ctrl = builder.build();) {
            ctrl.execute(new UnstableTransaction());
            ctrl.execute((x) -> x.internalMap.put("key:2", "dzikc"));
            ctrl.shut();
        }

        failureMarker.set(Boolean.TRUE);
        try (
                PersistenceController<StorableObject> controller2 = builder.build();) {
        }
    }

    @Test
    public void shouldUseSnapshotUponRestart() {
        //GIVEN
        final boolean[] calledInsideTransactionCheck = new boolean[]{false};


        final PrevaylerBuilder<StorableObject> builder = PrevaylerBuilder.<StorableObject>newBuilder()
                .useSupplier(StorableObject::createTestObject)
                .withJournalFastSerialization(false);
        try (
                final PersistenceController<StorableObject> ctrl = builder.build();) {
            ctrl.execute((x) -> {
                calledInsideTransactionCheck[0] = true;
                x.internalMap.put("key:2", "dzikc");
            });
            ctrl.snapshot();
        }

        calledInsideTransactionCheck[0] = false;
        try (
                PersistenceController<StorableObject> controller2 = builder.build();) {
            controller2.query(obj -> obj.internalMap.get("key:2"));
        }

        assertFalse(calledInsideTransactionCheck[0]);
    }

    @Test
    public void shouldCreateFolderWithinUserHome() {
        //WHEN
        final String prevHome = System.getProperty("user.home");
        File localFolder = new File("prevaylerHome");
        System.setProperty("user.home", localFolder.getAbsolutePath());
        localFolder.mkdirs();
        try (
                final PersistenceController<StorableObject> ctrl =
                        PrevaylerBuilder.<StorableObject>newBuilder()
                                .useSupplier(StorableObject::createTestObject)
                                .withinUserFolder("myfolder").build();) {

            ctrl.execute((x) -> x.internalMap.put("myKey", "myVal"));
            ctrl.close();
            //THEN
            File testFolder = new File(localFolder, "prevayler/myfolder");
            File[] insideFiles = testFolder.listFiles();
            Assert.assertEquals(3, insideFiles.length);
        } finally {
            Politician.beatAroundTheBush(() -> FileUtils.deleteDirectory(localFolder));
            System.setProperty("user.home", prevHome);
        }
    }


    @Test
    public void shouldStartTransientPrevayler() {
        //WHEN
        try (
                final PersistenceController<StorableObject> ctrl =
                        PrevaylerBuilder.<StorableObject>newBuilder()
                                .useSupplier(StorableObject::createTestObject)
                                .beTransient()
                                .withFolder(this.testFolder).build();) {

            ctrl.execute((x) -> x.internalMap.put("myKey", "myVal"));
            ctrl.close();
        }
        try (
                final PersistenceController<StorableObject> ctrl =
                        PrevaylerBuilder.<StorableObject>newBuilder()
                                .useSupplier(StorableObject::createTestObject)
                                .beTransient()
                                .withFolder(this.testFolder).build();) {

            assertNull( ctrl.query( obj -> obj.getImmutable().get("myKey")));
            ctrl.close();
        }
    }
    private static final class StrangeTransaction implements VoidCommand<StorableObject>, Serializable {

        private static int counter = 0;

        @Override
        public void executeVoid(StorableObject system) {
            system.internalMap.put("key:2", "dzikb");
        }

        private void writeObject(ObjectOutputStream ois) throws IOException {
            ois.defaultWriteObject();
            counter++;
        }

    }

    private static final class UnstableTransaction implements VoidCommand<StorableObject>, Serializable {

        @Override
        public void executeVoid(StorableObject system) {
            system.internalMap.put("key:2", "dzikb");
        }

        private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            ois.defaultReadObject();
            if (isFailureNeeded()) {
                throw new IOException("failure on demand");
            }
        }

    }
}
