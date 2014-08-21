/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct.impl;

import java.lang.reflect.Method;
import javax.inject.Inject;
import org.jglue.cdiunit.CdiRunner;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.setblack.airomem.direct.OperationType;
import pl.setblack.airomem.direct.SampleController;
import pl.setblack.badass.Politician;

/**
 *
 * @author jarek ratajski
 */
@RunWith(CdiRunner.class)
public class TransactionAnalyzerTest {

    private final SampleController controler = new SampleController();

    @Inject
    private TransactionAnalyzer analyzer;

    public TransactionAnalyzerTest() {
    }

    private Method getMethod(final Object object, final String name) {
        return Politician.beatAroundTheBush(() -> object.getClass().getMethod(name));

    }

    @Before
    public void setUp() {
    }

    @Test
    public void shouldSayWriteForWriteMethod() {
        final Method method = getMethod(controler, "writeMethod");
        //THEN
        assertEquals(OperationType.WRITE, analyzer.sayTypeOf(method));
    }

    @Test
    public void shouldSayReadFastForHTTPGetMethod() {
        final Method method = getMethod(controler, "readMethod");
        //THEN
        assertEquals(OperationType.READ_FAST, analyzer.sayTypeOf(method));
    }

}
