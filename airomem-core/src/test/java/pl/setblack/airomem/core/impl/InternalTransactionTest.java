/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.setblack.airomem.core.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.setblack.airomem.core.PrevalanceContext;
import pl.setblack.airomem.core.StorableObject;
import pl.setblack.airomem.core.VoidContextCommand;
import pl.setblack.airomem.core.WriteChecker;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author jratajsk
 */
class InternalTransactionTest {

    /**
     * Test of executeOn method, of class InternalTransaction.
     */
    @Test
    void testExecuteOn() {
        final LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));
        final VoidContextCommand<StorableObject> myCmd = (x, ctx) -> x.internalMap.put("date", ctx.time.toString());
        final RoyalFoodTester<StorableObject> testSystem = RoyalFoodTester.of(StorableObject.createTestObject(), true);

        final InternalTransaction instance = new InternalTransaction(myCmd);
        instance.executeAndQuery(testSystem, date);

        assertThat(testSystem.getFoodTester().internalMap)
                .containsKeys("date")
                .containsEntry("date", time.toInstant(ZoneOffset.UTC).toString());
    }

    @Test
    void testWriteCheckerContext() {
        final LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));
        final VoidContextCommand<StorableObject> myCmd = (x, ctx) -> {
            assertThat(WriteChecker.hasPrevalanceContext()).isTrue();
            assertThat(ctx.time).isEqualTo(WriteChecker.getContext().time);
        };
        InternalTransaction instance = new InternalTransaction(myCmd);
        final RoyalFoodTester<StorableObject> testSystem = RoyalFoodTester.of(StorableObject.createTestObject());
        instance.executeAndQuery(testSystem, date);
    }

    @Test
    void testWriteCheckerContextClearedWhenExceptionOccured() {
        final LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));
        final RuntimeException exception = new RuntimeException("fail");
        final VoidContextCommand<StorableObject> myCmd = (x, ctx) -> {
            assertThat(WriteChecker.hasPrevalanceContext()).isTrue();
            assertThat(ctx).isEqualTo(WriteChecker.getContext());
            throw exception;
        };
        InternalTransaction instance = new InternalTransaction(myCmd);
        final RoyalFoodTester<StorableObject> testSystem = RoyalFoodTester.of(StorableObject.createTestObject());
        assertThatThrownBy(() -> instance.executeAndQuery(testSystem, date))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("fail");
        assertThat(WriteChecker.getContext()).isNull();
    }

    /**
     * Test of createContext method, of class InternalTransaction.
     */
    @Test
    void testCreateContext() {
        LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));
        VoidContextCommand<StorableObject> myCmd = (x, ctx) -> x.internalMap.put("date", ctx.time.toString());
        InternalTransaction instance = new InternalTransaction(myCmd);

        PrevalanceContext result = instance.createContext(date);
        assertThat(date.toInstant()).isEqualTo(result.time);
    }


    @Test
    void testUnsafeRoyalFoodTester() {
        LocalDateTime time = LocalDateTime.of(1977, Month.MAY, 20, 1, 1);
        final Date date = Date.from(time.toInstant(ZoneOffset.UTC));

        VoidContextCommand<StorableObject> myCmd = (x, ctx) -> {
            x.internalMap.put("unsafe", "isunsafe");
            throw new IllegalStateException("unsafe");
        };
        InternalTransaction instance = new InternalTransaction(myCmd);
        final RoyalFoodTester<StorableObject> testSystem = RoyalFoodTester.of(StorableObject.createTestObject(), false);


        assertThatThrownBy(() -> instance.executeAndQuery(testSystem, date))
                .isInstanceOf(IllegalStateException.class);

        assertThat(testSystem.getSafeCopy().internalMap)
                .containsKeys("unsafe")
                .containsEntry("unsafe", "isunsafe");
    }
}
