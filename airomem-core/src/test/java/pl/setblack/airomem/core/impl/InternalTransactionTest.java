/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.setblack.airomem.core.impl;

import pl.setblack.airomem.core.StorableObject;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Date;
import static org.junit.Assert.*;
import org.junit.Test;
import pl.setblack.airomem.core.PrevalanceContext;
import pl.setblack.airomem.core.VoidContextCommand;
import pl.setblack.airomem.core.WriteChecker;
import pl.setblack.airomem.core.impl.InternalTransaction;
import pl.setblack.airomem.core.impl.RoyalFoodTester;

/**
 *
 * @author jratajsk
 */
public class InternalTransactionTest {

    /**
     * Test of executeOn method, of class InternalTransaction.
     */
    @Test
    public void testExecuteOn() throws Exception {
        final LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));
        final VoidContextCommand<StorableObject> myCmd = (x, ctx) -> x.internalMap.put("date", ctx.time.toString());
        final RoyalFoodTester<StorableObject> testSystem = RoyalFoodTester.of(StorableObject.createTestObject(), true);

        final InternalTransaction instance = new InternalTransaction(myCmd);
        instance.executeAndQuery(testSystem, date);

        assertEquals(testSystem.getFoodTester().internalMap.get("date"), time.toInstant(ZoneOffset.UTC).toString());
    }

    @Test
    public void testWriteCheckerContext() throws Exception {
        final LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));
        final VoidContextCommand<StorableObject> myCmd = (x, ctx) -> {
            assertTrue(WriteChecker.hasPrevalanceContext());
            assertEquals(ctx, WriteChecker.getContext());
        };
        InternalTransaction instance = new InternalTransaction(myCmd);
        final RoyalFoodTester<StorableObject> testSystem = RoyalFoodTester.of(StorableObject.createTestObject());
        instance.executeAndQuery(testSystem, date);
    }

    @Test
    public void testWriteCheckerContextClearedWhenExceptionOccured() throws Exception {
        final LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));
        final RuntimeException exception = new RuntimeException("fail");
        final VoidContextCommand<StorableObject> myCmd = (x, ctx) -> {
            assertTrue(WriteChecker.hasPrevalanceContext());
            assertEquals(ctx, WriteChecker.getContext());
            throw exception;
        };
        InternalTransaction instance = new InternalTransaction(myCmd);
        final RoyalFoodTester<StorableObject> testSystem = RoyalFoodTester.of(StorableObject.createTestObject());
        try {
            instance.executeAndQuery(testSystem, date);
        } catch (Exception re) {
            assertEquals(exception, re);
        }
        assertNull(WriteChecker.getContext());
    }

    /**
     * Test of createContext method, of class InternalTransaction.
     */
    @Test
    public void testCreateContext() {
        LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));
        VoidContextCommand<StorableObject> myCmd = (x, ctx) -> x.internalMap.put("date", ctx.time.toString());
        InternalTransaction instance = new InternalTransaction(myCmd);

        PrevalanceContext result = instance.createContext(date);
        assertEquals(date.toInstant(), result.time);
    }


    @Test
    public void testUnsafeRoyalFoodTester() {
        LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));

        VoidContextCommand<StorableObject> myCmd = (x, ctx) -> {
            x.internalMap.put("unsafe", "isunsafe");
            throw new RuntimeException("unsafe");
        };
        InternalTransaction instance = new InternalTransaction(myCmd);
        final RoyalFoodTester<StorableObject> testSystem = RoyalFoodTester.of(StorableObject.createTestObject(), false);

        try {
            instance.executeAndQuery(testSystem, date );
        } catch ( RuntimeException e) {}


        assertEquals("isunsafe", testSystem.getSafeCopy().internalMap.get("unsafe"));

    }
}
