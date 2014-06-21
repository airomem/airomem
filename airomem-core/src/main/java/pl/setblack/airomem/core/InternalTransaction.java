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
public class InternalTransaction<T extends Storable, RESULT> implements TransactionWithQuery<Optional<T>, RESULT> {

    private final Command<T, RESULT> cmd;

    public InternalTransaction(Command<T, RESULT> cmd) {
        this.cmd = cmd;
    }

    @Override
    public RESULT executeAndQuery(Optional<T> p, Date date) throws Exception {
        return cmd.execute(p.get());
    }

}
