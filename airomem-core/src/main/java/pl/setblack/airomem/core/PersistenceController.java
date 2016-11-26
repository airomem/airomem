/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;

/**
 *
 */
public interface PersistenceController<ROOT extends Serializable> extends AutoCloseable {

    void close();

    void shut();

    /**
     * Query system (immutable view of it).
     * <p>
     * Few things to remember: 1. if operations done on system (using query) do
     * make some changes they will not be preserved (for long) 2. it is possible
     * to return any object from domain (including ROOT root) and perform
     * operations later on (but the more You do inside Query the safer).
     *
     * @param <RESULT> result of query
     * @param query    lambda (or query implementation) with operations
     * @return calculated result
     */
    <RESULT> RESULT query(Query<ROOT, RESULT> query);

    /**
     * Perform command on system.
     * <p>
     * Inside command can be any code doing any changes. Such changes are
     * guaranteed to be preserved (if only command ended without exception).
     *
     * @param cmd
     */
    <R> R executeAndQuery(ContextCommand<ROOT, R> cmd);

    /**
     * Perform command on system.
     * <p>
     * Inside command can be any code doing any changes. Such changes are
     * guaranteed to be preserved (if only command ended without exception).
     *
     * @param cmd
     */
    <R> R executeAndQuery(Command<ROOT, R> cmd);

    void execute(VoidCommand<ROOT> cmd);

    void execute(VoidContextCommand<ROOT> cmd);

    boolean isOpen();

    void erase();

    void snapshot();

    void snapshotXML(Path xmlFile) throws IOException;
}
