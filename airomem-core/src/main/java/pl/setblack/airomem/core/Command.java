/* Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package pl.setblack.airomem.core;

/**
 *
 * @author jarekr
 */
@FunctionalInterface
public interface Command<T extends Storable> extends ContextCommand<T> {

    default void execute(T system, PrevalanceContext context) {
        this.execute(system);
    }

    void execute(T system);
}
