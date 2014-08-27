/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core;

import com.google.common.base.Optional;
import org.prevayler.Prevayler;
import pl.setblack.badass.Politician;

/**
 *
 */
public interface PersistenceController<T extends Storable<IMMUTABLE>, IMMUTABLE> extends AutoCloseable {

    void close();

    void shut();

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
    <RESULT> RESULT query(Query<IMMUTABLE, RESULT> query);

    /**
     * Perform command on system.
     *
     * Inside command can be any code doing any changes. Such changes are
     * guaranteed to be preserved (if only command ended without exception).
     *
     * @param cmd
     */
    <R> R executeAndQuery(ContextCommand<T, R> cmd);

    /**
     * Perform command on system.
     *
     * Inside command can be any code doing any changes. Such changes are
     * guaranteed to be preserved (if only command ended without exception).
     *
     * @param cmd
     */
    <R> R executeAndQuery(Command<T, R> cmd);

    void execute(VoidCommand<T> cmd);

    void execute(VoidContextCommand<T> cmd);

    boolean isOpen();
}
