/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core;

/**
 *
 */
public interface VoidContextCommand<T> extends ContextCommand<T, Void> {

    @Override
    default Void execute(T system, PrevalanceContext context) {
        this.executeVoid(system, context);
        return null;
    }

    void executeVoid(T system, PrevalanceContext context);

}
