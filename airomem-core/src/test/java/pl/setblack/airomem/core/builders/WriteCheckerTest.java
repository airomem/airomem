/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.builders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.setblack.airomem.core.PrevalanceContext;
import pl.setblack.airomem.core.WriteChecker;


/**
 * @author jarek ratajski
 */
class WriteCheckerTest {

    private PrevalanceContext ctx1;
    private PrevalanceContext ctx2;

     @BeforeEach
    public void setUp() {
        ctx1 = Mockito.mock(PrevalanceContext.class);
        ctx2 = Mockito.mock(PrevalanceContext.class);
        if (WriteChecker.hasPrevalanceContext()) {
            WriteChecker.clearContext();
        }
    }

    @AfterEach
    public void tearDown() {
        if (WriteChecker.hasPrevalanceContext()) {
            WriteChecker.clearContext();
        }
    }

    @Test
    void testSetContextSimple() {
        assertThat(WriteChecker.hasPrevalanceContext()).isFalse();
        assertThat(WriteChecker.getContext()).isNull();
        WriteChecker.setContext(ctx1);
        assertThat(ctx1).isEqualTo(WriteChecker.getContext());
        assertThat(WriteChecker.hasPrevalanceContext()).isTrue();
        WriteChecker.clearContext();
        assertThat(WriteChecker.getContext()).isNull();
        assertThat(WriteChecker.hasPrevalanceContext()).isFalse();
    }

    @Test
    void testSetContextThread() throws InterruptedException {
        assertThat(WriteChecker.getContext()).isNull();
        WriteChecker.setContext(ctx1);
        TestThread th = new TestThread();
        th.start();
        assertThat(ctx1).isEqualTo(WriteChecker.getContext());
        th.join();
        assertThat(WriteChecker.hasPrevalanceContext()).isTrue();
        assertThat(ctx1).isEqualTo(WriteChecker.getContext());
        WriteChecker.clearContext();

    }

    @Test
    void testCannotSetContextTwice() {
        assertThat(WriteChecker.getContext()).isNull();
        assertThatNoException().isThrownBy(() -> WriteChecker.setContext(ctx1));
        assertThatThrownBy(()->WriteChecker.setContext(ctx1)).isInstanceOf(AssertionError.class);


    }

    private final class TestThread extends Thread {

        @Override
        public void run() {
            assertThat(WriteChecker.getContext()).isNull();
            assertThat(WriteChecker.hasPrevalanceContext()).isFalse();
            WriteChecker.setContext(ctx2);
            assertThat(ctx2).isEqualTo(WriteChecker.getContext());
            WriteChecker.clearContext();
        }

    }
}
