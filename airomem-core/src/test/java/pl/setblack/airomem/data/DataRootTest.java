/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.data;

import pl.setblack.airomem.data.DataRoot;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kanapka
 */
public class DataRootTest {

    private DataRoot<BankAccountView, BankAccount> bank;

    public DataRootTest() {
    }

    @Before
    public void setUp() {
        bank = new DataRoot<>(new BankAccount());
    }

    @Test
    public void testCreation() {
        assertNotNull(bank);
        assertNotNull(bank.getImmutable().getAmount());
    }

}
