/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.setblack.airomem.direct.SampleController;

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
    public void shouldInstantiateSampleObjectField() {
        //GIVEN
        SampleController ctrl = new SampleController();
        //WHEN
        final ClassContext ctx = new ClassContext(ctrl);
        ctx.initMutable();
        //THEN
        assertNotNull(ctrl.getObject());
    }

}
