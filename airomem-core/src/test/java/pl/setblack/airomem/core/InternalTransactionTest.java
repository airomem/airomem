/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.setblack.airomem.core;

import com.google.common.base.Optional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jratajsk
 */
public class InternalTransactionTest {

    public InternalTransactionTest() {
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
     * Test of executeOn method, of class InternalTransaction.
     */
    @Test
    public void testExecuteOn() {
        LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));
        ContextCommand<StorableObject> myCmd = (x, ctx) -> x.internalMap.put("date", ctx.time.toString());
        Optional<StorableObject> testSystem = Optional.of(StorableObject.createTestObject());

        InternalTransaction instance = new InternalTransaction(myCmd);
        instance.executeOn(testSystem, date);
        // TODO review the generated test code and remove the default call to fail.
        assertEquals(testSystem.get().internalMap.get("date"), time.toInstant(ZoneOffset.UTC).toString());
    }

    /**
     * Test of createContext method, of class InternalTransaction.
     */
    @Test
    public void testCreateContext() {
        LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));
       ContextCommand<StorableObject> myCmd = (x, ctx) -> x.internalMap.put("date", ctx.time.toString());
        InternalTransaction instance = new InternalTransaction(myCmd);
        
        PrevalanceContext result = instance.createContext(date);
        assertEquals(date.toInstant(), result.time);
    }

}
