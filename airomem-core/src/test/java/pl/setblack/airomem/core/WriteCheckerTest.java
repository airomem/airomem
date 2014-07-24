/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core;

import pl.setblack.airomem.core.WriteChecker;
import java.util.Date;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.setblack.airomem.core.PrevalanceContext;

/**
 *
 * @author jarek ratajski
 */
public class WriteCheckerTest {

    private PrevalanceContext ctx1;
    private PrevalanceContext ctx2;

    public WriteCheckerTest() {
    }

    @Before
    public void setUp() {
        ctx1 = Mockito.mock(PrevalanceContext.class);
        ctx2 = Mockito.mock(PrevalanceContext.class);
    }

    @Test
    public void testSetContextSimple() {
        assertFalse(WriteChecker.hasPrevalanceContext());
        assertNull(WriteChecker.getContext());
        WriteChecker.setContext(ctx1);
        assertEquals(ctx1, WriteChecker.getContext());
        assertTrue(WriteChecker.hasPrevalanceContext());
        WriteChecker.clearContext();
        assertNull(WriteChecker.getContext());
        assertFalse(WriteChecker.hasPrevalanceContext());
    }

    @Test
    public void testSetContextThread() throws InterruptedException {
        assertNull(WriteChecker.getContext());
        WriteChecker.setContext(ctx1);
        TestThread th = new TestThread();
        th.start();
        assertEquals(ctx1, WriteChecker.getContext());
        th.join();
        assertTrue(WriteChecker.hasPrevalanceContext());
        assertEquals(ctx1, WriteChecker.getContext());
        WriteChecker.clearContext();

    }

    private final class TestThread extends Thread {

        @Override
        public void run() {
            assertNull(WriteChecker.getContext());
            assertFalse(WriteChecker.hasPrevalanceContext());
            WriteChecker.setContext(ctx2);
            assertEquals(ctx2, WriteChecker.getContext());
            WriteChecker.clearContext();
        }

    }
}
