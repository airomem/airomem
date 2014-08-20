/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct;

import java.lang.reflect.Method;
import javax.interceptor.InvocationContext;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author jratajsk
 */
public class PersistenceInterceptorTest {

    private PersistenceInterceptor instance = new PersistenceInterceptor();
    private InvocationContext context;
    private SampleController controller;

    public PersistenceInterceptorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private SampleController getController() {
        return this.controller;
    }

    @Before
    public void setUp() throws Exception {
        context = Mockito.mock(InvocationContext.class);
        controller = Mockito.mock(SampleController.class);

        Method method = SampleController.class.getMethod("writeMethod");

        Mockito.when(context.getTarget()).thenReturn(controller);
        Mockito.when(context.getMethod()).thenReturn(method);
        Mockito.when(context.proceed()).thenAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                getController().writeMethod();
                return null;
            }

        });
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of preparePersistence method, of class PersistenceInterceptor.
     */
    @Test
    public void shouldCallBusinnesMethod() throws Exception {

        //when
        instance.preparePersistence(context);
        //then
        Mockito.verify(controller).writeMethod();
    }

    @Test

    public void shoudInstantiatePersistentObjectBeforeCall() {
        controller = new SampleController() {

            @Override
            public void writeMethod() {
                assertNotNull(this.object);
            }

        };
        //when
        instance.preparePersistence(context);
        //then

    }

}
