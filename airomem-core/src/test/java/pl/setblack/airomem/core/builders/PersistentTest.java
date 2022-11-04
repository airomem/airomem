/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.builders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.setblack.airomem.core.Persistent;
import pl.setblack.airomem.core.StorableObject;
import pl.setblack.airomem.core.disk.PersistenceDiskHelper;

import java.io.File;
import java.util.HashMap;


/**
 * @author jarek ratajski
 */
class PersistentTest {

    private final File localFolder = new File("prevayler");

    @BeforeEach
    void setUp() {
        PersistenceDiskHelper.delete(localFolder.toPath());
    }

    @AfterEach
    void tearDown() {
        PersistenceDiskHelper.delete(localFolder.toPath());
    }

    @Test
    void testSimpleControllerCreation() {
        //GIVEN
        final Persistent<HashMap<String, String>> persistent = Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());
        //THEN
        assertThat(persistent.isOpen()).isTrue();
    }

    @Test
    void testSimpleControllerQuery() {
        //GIVEN
        final Persistent<HashMap<String, String>> persistent = Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());
        //WHEN
        String val = persistent.query(x -> x.get("key:2"));
        //THEN
        assertThat("val:2").isEqualTo(val);
    }

    @Test
    void testCreateTwice() {
        //GIVEN
        Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());
        //WHEN
        final Persistent<HashMap<String, String>> persistent2 = Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());
        String val = persistent2.query(x -> x.get("key:2"));
        //THEN
        assertThat("val:2").isEqualTo(val);
    }

    @Test
    void testSimpleControlleReadOnly() {
        //GIVEN
        final Persistent<HashMap<String, String>> persistent = Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());
        //WHEN
        String val = persistent.readOnly().get("key:2");
        //THEN
        assertThat("val:2").isEqualTo(val);
    }

    @Test
    void testSimpleControllerClose() {
        //GIVEN
        final Persistent<HashMap<String, String>> persistent = Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());
        //WHEN
        persistent.close();
        //THEN
        assertThat(persistent.isOpen()).isFalse();
    }

    @Test
    void testSimpleControllerShut() {
        //GIVEN
        final Persistent<HashMap<String, String>> persistent = Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());
        //WHEN
        persistent.shut();
        //THEN
        assertThat(persistent.isOpen()).isFalse();
    }


    @Test
    void testLoadWithNoStoredSystemShouldFail() {
        //WHEN
        assertThatThrownBy(()-> Persistent.load(localFolder.toPath())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testExecutePerformed() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());) {
            //WHEN
            persistent.executeAndQuery((x, ctx) -> x.put("key:1", "otherVal"));
            //THEN
            assertThat("otherVal").isEqualTo(persistent.query(x -> x.get("key:1")));
        }
    }

    @Test
    void testExecuteAndQueryWithoutContextPerformed() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());) {
            //WHEN
            persistent.executeAndQuery((x) -> x.put("key:1", "otherVal"));
            //THEN
            assertThat("otherVal").isEqualTo(persistent.query(x -> x.get("key:1")));
        }
    }

    @Test
    void testExecuteWithoutContextPerformed() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());) {
            //WHEN
            persistent.execute((x) -> x.put("key:1", "otherVal"));
            //THEN
            assertThat("otherVal").isEqualTo(persistent.query(x -> x.get("key:1")));
        }
    }

    @Test
    void testExecutePerformedAndStored() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());) {
            persistent.execute((x, ctx) -> x.put("key:1", "otherVal"));
        }

        //WHEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.load(localFolder.toPath())) {
            //THEN
            assertThat("otherVal").isEqualTo(persistent.query(x -> x.get("key:1")));
        }
    }

    @Test
    void schouldExistsAfterCreation() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());) {
            persistent.execute((x, ctx) -> x.put("key:1", "otherVal"));
        }
        //when
        //then
        assertThat(Persistent.exists(localFolder.toPath())).isTrue();
    }

    @Test
    void schouldNotExistsAfterCreation() {
        //GIVEN

        //when
        //then
        assertThat(Persistent.exists(localFolder.toPath())).isFalse();
    }

    @Test
    void schouldCreateNewSystem() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.loadOptional(localFolder.toPath(), () -> StorableObject.createTestHashMap());) {
            //WHEN
            final String val = persistent.query(x -> x.get("key:1"));
            //THEN
            assertThat("val:1").isEqualTo(val);
        }
    }

    @Test
    void schouldLoadOldSystemIfExists() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());) {
            persistent.execute((x, ctx) -> x.put("key:1", "otherVal"));
        }
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.loadOptional(localFolder.toPath(), () -> StorableObject.createTestHashMap());) {
            //WHEN
            final String val = persistent.query(x -> x.get("key:1"));
            //THEN
            assertThat("otherVal").isEqualTo(val);
        }
    }

    @Test
    void schouldForgetChangesDoneInQueries() {
        //GIVEN
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.create(localFolder.toPath(), StorableObject.createTestHashMap());) {
            persistent.<Void>query((x) -> {
                x.put("key:1", "otherVal");
                return null;
            });
        }
        try (
                final Persistent<HashMap<String, String>> persistent = Persistent.loadOptional(localFolder.toPath(), () -> StorableObject.createTestHashMap());) {
            //WHEN
            final String val = persistent.query(x -> x.get("key:1"));
            //THEN
            assertThat("val:1").isEqualTo(val);
        }
    }


}
