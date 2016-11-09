/* Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package pl.setblack.airomem.core.impl;

import org.prevayler.Prevayler;
import pl.setblack.airomem.core.Command;
import pl.setblack.airomem.core.ContextCommand;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.Query;
import pl.setblack.airomem.core.Storable;
import pl.setblack.airomem.core.VoidCommand;
import pl.setblack.airomem.core.VoidContextCommand;
import pl.setblack.airomem.core.disk.PersistenceDiskHelper;
import pl.setblack.badass.Politician;

/**
 * Controller of persistence system.
 *
 * Use this to perform queries and commands on system.
 *
 * @param <IMMUTABLE> immutable interface to persistent system (view)
 * @param <T> mutable interface to system
 * @author jarekr
 */
public class PersistenceControllerImpl<T extends Storable<IMMUTABLE>, IMMUTABLE>
        implements PersistenceController<T, IMMUTABLE> {

    private Prevayler<RoyalFoodTester<T>> prevayler;

    private final String uniqueName;

    public PersistenceControllerImpl(String name) {
        this.uniqueName = name;
    }

    /**
     * Close system.
     *
     * This couses snapshot of system to be done. After this operation
     * Controller should be no more usable.
     */
    public void close() {
        Politician.beatAroundTheBush(() -> {
            if (this.prevayler != null) {
                this.prevayler.takeSnapshot();
                this.prevayler.close();
                this.prevayler = null;
            }
        });
    }

    /**
     * Shut system immediatelly.
     *
     * No snaphsot is done. After load system will be restored using Commands.
     */
    @Override
    public void shut() {
        Politician.beatAroundTheBush(() -> {
            this.prevayler.close();
            this.prevayler = null;
        });
    }

    public void initSystem(final Prevayler<RoyalFoodTester<T>> prevayler) {
        this.prevayler = prevayler;
        Politician.beatAroundTheBush(() -> this.prevayler.takeSnapshot());
    }

    /**
     * Query system (immutable view of it).
     *
     * Few things to remember: 1. if operations done on system (using query) do
     * make some changes they will not be preserved (for long) 2. it is possible
     * to return any object from domain (including IMMUTABLE root) and perform
     * operations later on (but the more You do inside Query the safer).
     *
     * @param <RESULT> result of query
     * @param query lambda (or query implementation) with operations
     * @return calculated result
     */
    public <RESULT> RESULT query(Query<IMMUTABLE, RESULT> query) {
        return query.evaluate(getImmutable());
    }

    /**
     * Perform command on system.
     *
     * Inside command can be any code doing any changes. Such changes are
     * guaranteed to be preserved (if only command ended without exception).
     *
     * @param cmd
     */
    public <R> R executeAndQuery(ContextCommand<T, R> cmd) {
        return Politician.beatAroundTheBush(() -> this.prevayler.execute(new InternalTransaction<>(cmd)));
    }

    /**
     * Perform command on system.
     *
     * Inside command can be any code doing any changes. Such changes are
     * guaranteed to be preserved (if only command ended without exception).
     *
     * @param cmd
     */
    @Override
    public <R> R executeAndQuery(Command<T, R> cmd) {
        return this.executeAndQuery((ContextCommand<T, R>) cmd);
    }

    @Override
    public void execute(VoidCommand<T> cmd) {
        this.executeAndQuery((Command<T, Void>) cmd);
    }

    @Override
    public void execute(VoidContextCommand<T> cmd) {
        this.executeAndQuery((ContextCommand<T, Void>) cmd);
    }

    private T getObject() {
        return this.prevayler.prevalentSystem().getWorkObject();
    }

    private IMMUTABLE getImmutable() {
        return this.getObject().getImmutable();
    }

    @Override
    public boolean isOpen() {
        return this.prevayler != null;
    }

    public void deleteFolder() {
        PersistenceDiskHelper.delete(this.uniqueName);
    }

    @Override
    public void erase() {
        this.close();
        this.deleteFolder();
    }

    @Override
    public void snapshot() {
        Politician.beatAroundTheBush(()->this.prevayler.takeSnapshot());
    }
}
