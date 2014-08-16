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

    
    public PersistenceInterceptorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of preparePersistence method, of class PersistenceInterceptor.
     */
    @Test
    public void testBusinessMethodWasCalled() throws Exception {
        InvocationContext context = Mockito.mock(InvocationContext.class);
        
        final SampleController ctrl  = Mockito.mock(SampleController.class);
      
        Mockito.when(context.proceed()).thenAnswer( new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
               ctrl.writeMethod();
               return null;
            }
            
        });
        //when
        instance.preparePersistence(context);
       Mockito.verify(ctrl).writeMethod();
    }
    
}
