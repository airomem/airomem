/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct.impl;

import java.io.File;
import java.lang.reflect.Method;
import org.apache.commons.io.FileUtils;
import org.jglue.cdiunit.CdiRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.setblack.airomem.core.builders.PersistenceFactory;
import pl.setblack.airomem.direct.SampleController;
import pl.setblack.badass.Politician;

/**
 *
 * @author jarek ratajski
 */
@RunWith(CdiRunner.class)
public class ClassContextTest {

    public ClassContextTest() {
    }

    @Before
    public void setUp() {
        PrevaylerRegister.getInstance().clear();
        Politician.beatAroundTheBush(() -> {
            FileUtils.deleteDirectory(new File(PersistenceFactory.STORAGE_FOLDER));
        });
    }

    @After
    public void tearDown() {
        PrevaylerRegister.getInstance().clear();
        Politician.beatAroundTheBush(() -> {
            FileUtils.deleteDirectory(new File(PersistenceFactory.STORAGE_FOLDER));
        });
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotInstantiateNoCDIBean() throws NoSuchMethodException {

        //WHEN
        final ClassContext ctx = new ClassContext(Object.class);

    }

    @Test(expected = RuntimeException.class)
    public void shouldFailOnNoBeanClass() throws NoSuchMethodException {
        //GIVEN
        SampleController ctrl = new SampleController() {
        };
        final Method method = ctrl.getClass().getMethod("writeMethod");
        //WHEN
        final ClassContext ctx = new ClassContext(ctrl.getClass());

        ctx.performTransaction(ctrl, method, new Object[]{});
    }

}
