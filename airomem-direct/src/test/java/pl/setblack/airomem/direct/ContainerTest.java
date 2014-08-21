/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct;

import java.io.File;
import java.lang.reflect.Method;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.setblack.airomem.core.PersistenceFactory;
import pl.setblack.airomem.direct.impl.PrevaylerRegister;
import pl.setblack.badass.Politician;

/**
 *
 */
@RunWith(CdiRunner.class)
@AdditionalClasses(PersistenceInterceptor.class)
public class ContainerTest {

    @Inject
    SampleController controller;

    @Inject
    SampleController controller2;

    @After
    public void tearDown() {
        PrevaylerRegister.getInstance().clear();
        Politician.beatAroundTheBush(() -> {
            FileUtils.deleteDirectory(new File(PersistenceFactory.STORAGE_FOLDER));
        });
    }

    @Test
    public void shouldRunMethodWithoutErrors() {
        controller.writeMethod();
    }

    @Test
    public void shouldRestoreResults() {
        controller.writeMethod();
        PrevaylerRegister.getInstance().shut();
        final String val = controller2.readMethod();
        Assert.assertEquals("changed field1", val);
    }

    @Test
    public void shouldNotStoreHTTPGetMethodChanges() {
        //GIVEN

        //WHEN
        controller.readMethod();
        PrevaylerRegister.getInstance().shut();
        int value = PrevaylerRegister.getInstance().getController(SampleObject.class, "object").query(v -> v.value); //THEN
        //THEN
        Assert.assertEquals(0, value);
    }

    @Test
    public void shouldNotStoreWriteMethodChanges() {
        //GIVEN

        //WHEN
        controller.writeMethod();
        PrevaylerRegister.getInstance().shut();
        int value = PrevaylerRegister.getInstance().getController(SampleObject.class, "object").query(v -> v.value); //THEN
        //THEN
        Assert.assertEquals(14, value);
    }

}
