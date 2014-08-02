/* Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package pl.setblack.airomem.core;

import com.google.common.base.Optional;
import java.util.Date;
import org.prevayler.Transaction;

/**
 * Class used internally to wrap user Command.
 *
 * @author jarekr
 */
class InternalTransaction<T extends Storable> implements Transaction<Optional<T>> {

    private static final long serialVersionUID = 1l;

    private final ContextCommand<T> cmd;

    InternalTransaction(ContextCommand<T> cmd) {
        this.cmd = cmd;
    }

    @Override
    public void executeOn(Optional<T> p, Date date) {
        final PrevalanceContext ctx = createContext(date);
        WriteChecker.setContext(ctx);
        try {
            cmd.execute(p.get(), ctx);
        } finally {
            WriteChecker.clearContext();
        }
    }

    PrevalanceContext createContext(Date date) {
        return new PrevalanceContext(date);
    }

}
