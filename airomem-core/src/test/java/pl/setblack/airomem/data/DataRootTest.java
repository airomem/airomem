/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0 
 */
package pl.setblack.airomem.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author Kanapka
 */
public class DataRootTest {

    private DataRoot<BankAccount> bank;

    @Before
    public void setUp() {
        bank = new DataRoot<>(new BankAccount());
    }

    @Test
    public void testCreation() {
        assertNotNull(bank);
        assertNotNull(bank.getDataObject().getAmount());
    }

}
