/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core;

import java.io.Serializable;
import java.util.function.Supplier;
import pl.setblack.airomem.core.builders.PersistenceFactory;
import pl.setblack.airomem.data.DataRoot;

/**
 * Simplified version of persistence controller.
 *
 * This version does not force to follow IMMUTABLE , MUTABLE Pattern. IT is
 * easier to implement - but of course not that safe for team development.
 */
public class Persistent<T extends Serializable> implements AutoCloseable {

    private final PersistenceController<DataRoot<T, T>, T> controller;

    private Persistent(PersistenceController<DataRoot<T, T>, T> controller) {
        this.controller = controller;
    }

    public static boolean exists(String name) {
        final PersistenceFactory factory = new PersistenceFactory();
        return factory.exists(name);
    }

    public static <T extends Serializable> Persistent<T> load(String name) {
        final PersistenceFactory factory = new PersistenceFactory();
        PersistenceController<DataRoot<T, T>, T> controller
                = factory.<DataRoot<T, T>, T>load(name);
        return new Persistent<>(controller);
    }

    public static <T extends Serializable> Persistent<T> loadOptional(String name, Supplier<T> constructor, boolean useRoyalFoodTester) {
        final PersistenceFactory factory = new PersistenceFactory();
        PersistenceController<DataRoot<T, T>, T> controller
                = factory.<DataRoot<T, T>, T>initOptional(name, () -> new DataRoot<>(constructor.get()), useRoyalFoodTester);
        return new Persistent<>(controller);
    }

    public static <T extends Serializable> Persistent<T> loadOptional(String name, Supplier<T> constructor) {
        return loadOptional(name, constructor, true);
    }


        public static <T extends Serializable> Persistent<T> create(String name, T initial) {
        final PersistenceFactory factory = new PersistenceFactory();
        final DataRoot<T, T> root = new DataRoot<>(initial);
        PersistenceController<DataRoot<T, T>, T> controller
                = factory.<DataRoot<T, T>, T>init(name, root);
        return new Persistent<>(controller);
    }

    public <RESULT> RESULT query(Query<T, RESULT> query) {
        return controller.query(query);
    }

    public T readOnly() {
        return controller.query( t -> t);
    }

    @Override
    public void close() {
        this.controller.close();
    }

    public void shut() {
        this.controller.shut();
    }

    public boolean isOpen() {
        return controller.isOpen();
    }

    public <R> R executeAndQuery(ContextCommand<T, R> cmd) {
        return controller.executeAndQuery((ContextCommand<DataRoot<T, T>, R>) ((x, ctx) -> cmd.execute(x.getDataObject(), ctx)));
    }

    public <R> R executeAndQuery(Command<T, R> cmd) {
        return controller.executeAndQuery((Command<DataRoot<T, T>, R>) (x -> cmd.execute(x.getDataObject())));
    }

    public void execute(VoidContextCommand<T> cmd) {
        controller.execute((VoidContextCommand<DataRoot<T, T>>) ((x, ctx) -> cmd.execute(x.getDataObject(), ctx)));
    }

    public void execute(VoidCommand<T> cmd) {
        controller.execute((VoidCommand<DataRoot<T, T>>) (x -> cmd.execute(x.getDataObject())));
    }

}
