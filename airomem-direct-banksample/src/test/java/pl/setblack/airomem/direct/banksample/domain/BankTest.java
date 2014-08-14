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
        final Account acc = bank.registerNewAccount(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, acc.value);
    }

    @Test
    public void testRegisterNewAccountId() {
        final Account acc = bank.registerNewAccount(BigDecimal.ZERO);
        assertNotNull(acc.id);
    }

    @Test
    public void testRegisterNewAccountValue() {
        final Account acc = bank.registerNewAccount(BigDecimal.valueOf(1111));
        assertEquals(BigDecimal.valueOf(1111), acc.value);
    }

    @Test
    public void testRegisterTwoVariousAccounts() {
        final Account acc1 = bank.registerNewAccount(BigDecimal.ZERO);
        final Account acc2 = bank.registerNewAccount(BigDecimal.ZERO);
        assertNotSame(acc1.id, acc2.id);
    }

    @Test
    public void testPredictableAccounts() {
        final Bank alternate = new Bank();
        for (int i = 0; i < 10;
                i++) {
            final Account acc1 = bank.registerNewAccount(BigDecimal.ZERO);
            final Account acc2 = alternate.registerNewAccount(BigDecimal.ZERO);
            assertEquals(acc1.id, acc2.id);

        }
    }

    @Test
    public void shouldRetrieveSameAccountAsRegistered() {
        final Account acc1 = bank.registerNewAccount(BigDecimal.valueOf(115));
        final Account acc2 = bank.getAccount(acc1.id);
        assertEquals(acc1.value, acc2.value);
    }

    @Test
    public void shouldChangeBalanceAfterWithdraw() {
    }

    @Test
    public void shouldChangeBalanceAfterDeposit() {
        final Account acc1 = bank.registerNewAccount(BigDecimal.valueOf(115));
        bank.deposit(acc1.id, BigDecimal.valueOf(20));

        BigDecimal value = bank.getAccount(acc1.id).value;
        assertEquals(135, value.longValue());
    }
}
