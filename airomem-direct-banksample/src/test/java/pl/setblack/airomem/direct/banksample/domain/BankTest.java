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
public class BankTest {

    private Bank bank;

    public BankTest() {
    }

    @Before
    public void setUp() {
        bank = new Bank();
    }

    @Test
    public void totalAmountAfterCreateIs0() {
        assertEquals(BigDecimal.ZERO, this.bank.getTotalAmount());
    }

    @Test
    public void testRegisterNewAccount() {
    }

    @Test
    public void testGetAccount() {
    }

}
