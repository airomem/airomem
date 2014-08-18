/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core;

/**
 *
 */
public interface VoidCommand<T> extends Command<T, Void> {

    @Override
    default Void execute(T system) {
        this.executeVoid(system);
        return null;
    }

    void executeVoid(T system);

}
