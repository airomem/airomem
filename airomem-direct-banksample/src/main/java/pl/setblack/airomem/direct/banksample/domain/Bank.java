/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.domain;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */

public final class Bank implements Serializable {

    private static final long serialVersionUID = 1l;
    private final Random random = new Random(42);

    private transient RandomBasedGenerator uuidGenerator;

    private Map<String, Account> accounts = new ConcurrentHashMap<>();

    protected Bank() {

    }

    private synchronized String generateId() {
        if (uuidGenerator == null) {
            uuidGenerator = Generators.randomBasedGenerator(random);
        }
        return uuidGenerator.generate().toString();
    }

    public BigDecimal getTotalAmount() {
        return BigDecimal.ZERO;
    }

    public Account registerNewAccount(BigDecimal value) {
        final Account acc = new Account(generateId(), value);
        this.accounts.put(acc.id, acc);
        return acc;
    }

    public Account getAccount(String id) {
        return this.accounts.get(id);
    }

   

    public Account change(String id, BigDecimal value) {
        Account changed = this.accounts.get(id).change(value);
        this.accounts.put(id, changed);
        return changed;
    }

    public Iterable<Account> getAllAccounts() {
        return Collections.unmodifiableCollection(this.accounts.values());
    }
}
