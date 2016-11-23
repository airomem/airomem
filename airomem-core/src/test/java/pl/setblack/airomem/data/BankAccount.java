/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0 
 */
package pl.setblack.airomem.data;

import java.math.BigDecimal;

/**
 * @author jarekr
 */
public class BankAccount implements BankAccountView {

    private BigDecimal money;

    public BankAccount() {
        money = new BigDecimal(0);
    }

    public void add(BigDecimal amount) {
        money = money.add(amount);
    }

    @Override
    public BigDecimal getAmount() {
        return money;
    }

}
