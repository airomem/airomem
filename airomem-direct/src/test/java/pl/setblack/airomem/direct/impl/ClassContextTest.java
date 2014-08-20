/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct.impl;

import java.lang.reflect.Method;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import pl.setblack.airomem.direct.SampleController;
import pl.setblack.airomem.direct.SampleObject;

/**
 *
 * @author jarek ratajski
 */
public class ClassContextTest {

    public ClassContextTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void shouldInstantiateSampleObjectField() throws NoSuchMethodException {
        //GIVEN
        SampleController ctrl = new SampleController();
        final Method method = ctrl.getClass().getMethod("writeMethod");
        //WHEN
        final ClassContext ctx = new ClassContext(ctrl);

        ctx.performTransaction(ctrl, method);
        //THEN
        assertEquals("changed field1",
                PrevaylerRegister.getInstance().getController(SampleObject.class, "object").query(o -> o.getField1()));
    }

}
