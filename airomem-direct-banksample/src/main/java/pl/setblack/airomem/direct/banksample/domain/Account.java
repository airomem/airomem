/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 */
public final class Account implements Serializable {

    public final String id;

    public final BigDecimal value;

    Account(final String id, final BigDecimal value) {
        this.id = id;
        this.value = value;
    }

    Account change(final BigDecimal change) {
        return new Account(this.id, value.add(change));
    }

}
