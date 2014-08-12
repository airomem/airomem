/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.domain;

import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jarek ratajski
 */
public class AccountTest {

    public AccountTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testConstructionWithId() {
        final Account acc = new Account("myid", BigDecimal.ONE);
        assertEquals("myid", acc.id);
    }

    @Test
    public void testConstructionWithValue() {
        final Account acc = new Account("myid", BigDecimal.valueOf(1024));
        assertEquals(1024, acc.value.longValue());
    }

    @Test
    public void testAddValue() {
        final Account acc = new Account("myid", BigDecimal.valueOf(1024));
        final Account changed = acc.change(BigDecimal.valueOf(256));
        assertEquals(1024 + 256, changed.value.longValue());
    }

}
