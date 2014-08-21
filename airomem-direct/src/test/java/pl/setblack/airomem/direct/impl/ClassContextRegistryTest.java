/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct.impl;

import javax.inject.Inject;
import org.jglue.cdiunit.CdiRunner;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.setblack.airomem.direct.SampleController;

/**
 *
 * @author jarek ratajski
 */
@RunWith(CdiRunner.class)
public class ClassContextRegistryTest {

    @Inject
    private ClassContextRegistry registry;

    public ClassContextRegistryTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testGetContextReturnsSomething() {
        final SampleController ctrl = new SampleController();
        final ClassContext ctx = registry.getContext(ctrl);
        assertNotNull(ctx);
    }

    @Test
    public void testGetContextCachesResults() {
        final SampleController ctrl = new SampleController();
        final ClassContext ctx1 = registry.getContext(ctrl);
        final ClassContext ctx2 = registry.getContext(ctrl);
        assertEquals(ctx2, ctx1);
    }

}
