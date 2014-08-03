/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core;

import java.io.Serializable;
import pl.setblack.airomem.data.DataRoot;
import pl.setblack.badass.Politician;

/**
 * Simplified version of persistence controller.
 *
 * This version does not force to follow IMMUTABLE , MUTABLE Pattern. IT is
 * easier to implement - but of course not that safe for team development.
 */
public class SimpleController<T extends Serializable> implements AutoCloseable {

    private final PersistenceController<DataRoot<T, T>, T> controller;

    private SimpleController(PersistenceController<DataRoot<T, T>, T> controller) {
        this.controller = controller;
    }

    public static <T extends Serializable> SimpleController<T> load(String name) {
        final PersistenceFactory factory = new PersistenceFactory();
        PersistenceController<DataRoot<T, T>, T> controller
                = factory.<DataRoot<T, T>, T>load(name);
        return new SimpleController<>(controller);
    }

    public static <T extends Serializable> SimpleController<T> create(String name, T initial) {
        final PersistenceFactory factory = new PersistenceFactory();
        final DataRoot<T, T> root = new DataRoot<>(initial);
        PersistenceController<DataRoot<T, T>, T> controller
                = factory.<DataRoot<T, T>, T>init(name, root);
        return new SimpleController<>(controller);
    }

    public <RESULT> RESULT query(Query<T, RESULT> query) {
        return controller.query(query);
    }

    public void close() {
        this.controller.close();
    }

    public boolean isOpen() {
        return controller.isOpen();
    }

    public void execute(ContextCommand<T> cmd) {
        controller.execute((x, ctx) -> cmd.execute(x.getDataObject(), ctx));
    }

    public void execute(Command<T> cmd) {

    }

}
