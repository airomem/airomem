/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.setblack.airomem.core;

import com.google.common.base.Optional;
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

    /**
     * Test of executeOn method, of class InternalTransaction.
     */
    @Test
    public void testExecuteOn() {
        final LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));
        final VoidContextCommand<StorableObject> myCmd = (x, ctx) -> x.internalMap.put("date", ctx.time.toString());
        final Optional<StorableObject> testSystem = Optional.of(StorableObject.createTestObject());

        final InternalTransaction instance = new InternalTransaction(myCmd);
        instance.executeAndQuery(testSystem, date);

        assertEquals(testSystem.get().internalMap.get("date"), time.toInstant(ZoneOffset.UTC).toString());
    }

    @Test
    public void testWriteCheckerContext() {
        final LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));
        final VoidContextCommand<StorableObject> myCmd = (x, ctx) -> {
            assertTrue(WriteChecker.hasPrevalanceContext());
            assertEquals(ctx, WriteChecker.getContext());
        };
        InternalTransaction instance = new InternalTransaction(myCmd);
        final Optional<StorableObject> testSystem = Optional.of(StorableObject.createTestObject());
        instance.executeAndQuery(testSystem, date);
    }

    @Test
    public void testWriteCheckerContextClearedWhenExceptionOccured() {
        final LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));
        final RuntimeException exception = new RuntimeException("fail");
        final VoidContextCommand<StorableObject> myCmd = (x, ctx) -> {
            assertTrue(WriteChecker.hasPrevalanceContext());
            assertEquals(ctx, WriteChecker.getContext());
            throw exception;
        };
        InternalTransaction instance = new InternalTransaction(myCmd);
        final Optional<StorableObject> testSystem = Optional.of(StorableObject.createTestObject());
        try {
            instance.executeAndQuery(testSystem, date);
        } catch (RuntimeException re) {
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

}
