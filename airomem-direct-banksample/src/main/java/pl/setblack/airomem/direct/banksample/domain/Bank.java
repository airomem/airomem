/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 */
public class Bank implements Serializable {

    public BigDecimal getTotalAmount() {
        return BigDecimal.ZERO;
    }

    public Account registerNewAccount(BigDecimal value) {
        throw new UnsupportedOperationException();
    }

    public Account getAccount(String id) {
        throw new UnsupportedOperationException();
    }
}
