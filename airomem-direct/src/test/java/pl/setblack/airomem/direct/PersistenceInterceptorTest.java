/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct;

import java.io.File;
import java.lang.reflect.Method;
import javax.interceptor.InvocationContext;
import org.apache.commons.io.FileUtils;
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
import pl.setblack.airomem.core.PersistenceFactory;
import pl.setblack.airomem.direct.impl.PrevaylerRegister;
import pl.setblack.badass.Politician;

/**
 *
 * @author jratajsk
 */
@Ignore
public class PersistenceInterceptorTest {

    private PersistenceInterceptor instance = new PersistenceInterceptor();
    private InvocationContext context;
    private SampleController controller;

    public PersistenceInterceptorTest() {
    }

    private SampleController getController() {
        return this.controller;
    }

    @Before
    public void setUp() throws Exception {
        context = Mockito.mock(InvocationContext.class);

        Method method = SampleController.class.getMethod("writeMethod");

        Mockito.when(context.getTarget()).thenAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return getController();
            }
        });
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
        PrevaylerRegister.getInstance().clear();
        Politician.beatAroundTheBush(() -> {
            FileUtils.deleteDirectory(new File(PersistenceFactory.STORAGE_FOLDER));
        });
    }

    /**
     * Test of preparePersistence method, of class PersistenceInterceptor.
     */
    @Test
    public void shouldCallBusinnesMethod() throws Exception {
        //given
        SampleControllerVerifyCalled.called = 0;
        controller = new SampleControllerVerifyCalled();
        //when
        instance.preparePersistence(context);
        //then
        assertEquals(1, SampleControllerVerifyCalled.called);
    }

    @Test

    public void shoudInstantiatePersistentObjectBeforeCall() {
        //given
        controller = new SampleControllerAssertInitialized();
        //when
        instance.preparePersistence(context);
        //then

    }

    public static class SampleControllerVerifyCalled extends SampleController {

        private static int called = 0;

        @Override
        public void writeMethod() {
            super.writeMethod();
            called++;
        }

    }

    public static class SampleControllerAssertInitialized extends SampleController {

        @Override
        public void writeMethod() {
            assertNotNull(this.object);

        }

    }

}
