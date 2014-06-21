/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core;

import com.google.common.base.Optional;
import java.util.Date;
import org.prevayler.Transaction;
import org.prevayler.TransactionWithQuery;

/**
 *
 * @author jarekr
 */
public class InternalTransaction<T extends Storable> implements Transaction<Optional<T>> {

    private final Command<T> cmd;

    public InternalTransaction(Command<T> cmd) {
        this.cmd = cmd;
    }

    @Override
    public void executeOn(Optional<T> p, Date date) {
        cmd.execute(p.get());
    }

}
