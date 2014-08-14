/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.domain;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public final class Bank implements Serializable {

    private final Random random = new Random(42);

    private final RandomBasedGenerator uuidGenerator;

    private Map<String, Account> accounts = new ConcurrentHashMap<>();

    public Bank() {
        uuidGenerator = Generators.randomBasedGenerator(random);
    }

    public BigDecimal getTotalAmount() {
        return BigDecimal.ZERO;
    }

    public Account registerNewAccount(BigDecimal value) {
        final Account acc = new Account(uuidGenerator.generate().toString(), value);
        this.accounts.put(acc.id, acc);
        return acc;
    }

    public Account getAccount(String id) {
        return this.accounts.get(id);
    }

    public void withdraw(String id, BigDecimal value) {

    }

    public void deposit(String id, BigDecimal value) {
        Account changed = this.accounts.get(id).change(value);
        this.accounts.put(id, changed);
    }
}
