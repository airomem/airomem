/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct.impl;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.setblack.airomem.core.PersistenceFactory;
import pl.setblack.airomem.core.SimpleController;
import pl.setblack.airomem.direct.SampleObject;
import pl.setblack.badass.Politician;

/**
 *
 * @author jarek ratajski
 */
public class PrevaylerRegisterTest {

    public PrevaylerRegisterTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        PrevaylerRegister.getInstance().clear();
        Politician.beatAroundTheBush(() -> {
            FileUtils.deleteDirectory(new File(PersistenceFactory.STORAGE_FOLDER));
        });
    }

    @Test
    public void shouldReturnSameInstance() {
        final PrevaylerRegister inst1 = PrevaylerRegister.getInstance();
        final PrevaylerRegister inst2 = PrevaylerRegister.getInstance();
        assertTrue(inst1 == inst2);
    }

    @Test
    public void shouldReturnPrevaylerOfGivenClass() {
        final SimpleController<SampleObject> obj = PrevaylerRegister.getInstance().getController(SampleObject.class, "test");
        assertNotNull(obj);
    }

    @Test
    public void shouldReturnSamePrevaylerAfterClose() {
        //GIVEN
        try (
                final SimpleController<SampleObject> obj = PrevaylerRegister.getInstance().getController(SampleObject.class, "test");) {
            obj.execute((x) -> x.setField1("changed"));
        }

        //WHEN
        PrevaylerRegister.getInstance().clear();
        try (
                final SimpleController<SampleObject> obj = PrevaylerRegister.getInstance().getController(SampleObject.class, "test");) {
            final String val = obj.query((x) -> x.getField1());
            assertEquals("changed", val);
        }
    }

    @Test
    public void shouldReturnSamePrevaylerObjectInSubsequentCalls() {
        //GIVEN
        try (
                final SimpleController<SampleObject> obj1 = PrevaylerRegister.getInstance().getController(SampleObject.class, "test");) {
            obj1.execute((x) -> x.setField1("changed"));
            //WHEN
            final SimpleController<SampleObject> obj2 = PrevaylerRegister.getInstance().getController(SampleObject.class, "test");
            //THEN
            assertTrue(obj1 == obj2);

        }
    }

    @Test
    public void shouldReturnNewPrevaylerAfterClear() {
        //GIVEN
        try (
                final SimpleController<SampleObject> obj1 = PrevaylerRegister.getInstance().getController(SampleObject.class, "test");) {
            obj1.execute((x) -> x.setField1("changed"));
            //WHEN
            PrevaylerRegister.getInstance().clear();
            final SimpleController<SampleObject> obj2 = PrevaylerRegister.getInstance().getController(SampleObject.class, "test");
            //THEN
            assertTrue(obj1 != obj2);

        }
    }
}
