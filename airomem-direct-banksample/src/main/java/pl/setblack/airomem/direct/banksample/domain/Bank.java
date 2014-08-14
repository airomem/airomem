/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.domain;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Random;

/**
 *
 */
public final class Bank implements Serializable {

    private final Random random = new Random(42);

    private final RandomBasedGenerator uuidGenerator;

    public Bank() {
        uuidGenerator = Generators.randomBasedGenerator(random);
    }

    public BigDecimal getTotalAmount() {
        return BigDecimal.ZERO;
    }

    public Account registerNewAccount(BigDecimal value) {
        return new Account(uuidGenerator.generate().toString(), value);
    }

    public Account getAccount(String id) {
        throw new UnsupportedOperationException();
    }
}
