/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core;

import pl.setblack.airomem.core.builders.PrevaylerBuilder;
import pl.setblack.airomem.data.DataRoot;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * Simplified version of persistence controller.
 * <p>
 * This version does not force to follow IMMUTABLE , MUTABLE Pattern. IT is
 * easier to implement - but of course not that safe for team development.
 */
public class Persistent<T extends Serializable> implements AutoCloseable {

    private final PersistenceController<DataRoot<T>> controller;

    private Persistent(PersistenceController<DataRoot<T>> controller) {
        this.controller = controller;
    }

    public static boolean exists(Path path) {
        return Files.exists(path);
    }

    public static <T extends Serializable> Persistent<T> load(Path path) {
        return new Persistent<T>(PrevaylerBuilder.<DataRoot<T>>newBuilder()
                .withFolder(path)
                .build());
    }

    public static <T extends Serializable> Persistent<T> loadOptional(Path path, Supplier<T> constructor, boolean useRoyalFoodTester) {
        return new Persistent<T>(PrevaylerBuilder.<DataRoot<T>>newBuilder()
                .useSupplier(() -> new DataRoot<>(constructor.get()))
                .withRoyalFoodTester(useRoyalFoodTester)
                .withFolder(path)
                .build());

    }

    public static <T extends Serializable> Persistent<T> loadOptional(Path path, Supplier<T> constructor) {
        return loadOptional(path, constructor, true);
    }


    public static <T extends Serializable> Persistent<T> create(Path path, T initial) {
        return new Persistent<T>(PrevaylerBuilder.<DataRoot<T>>newBuilder()
                .useSupplier(() -> new DataRoot<>(initial))
                .forceOverwrite(true)
                .withFolder(path)
                .build());
    }

    public <RESULT> RESULT query(Query<T, RESULT> query) {
        return controller.query((dataRoot) -> query.evaluate(dataRoot.getDataObject()));
    }

    public T readOnly() {
        return query(t -> t);
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
        return controller.executeAndQuery((ContextCommand<DataRoot<T>, R>) ((x, ctx) -> cmd.execute(x.getDataObject(), ctx)));
    }

    public <R> R executeAndQuery(Command<T, R> cmd) {
        return controller.executeAndQuery((Command<DataRoot<T>, R>) (x -> cmd.execute(x.getDataObject())));
    }

    public void execute(VoidContextCommand<T> cmd) {
        controller.execute((VoidContextCommand<DataRoot<T>>) ((x, ctx) -> cmd.execute(x.getDataObject(), ctx)));
    }

    public void execute(VoidCommand<T> cmd) {
        controller.execute((VoidCommand<DataRoot<T>>) (x -> cmd.execute(x.getDataObject())));
    }

}
