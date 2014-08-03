/* Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package pl.setblack.airomem.core;

/**
 * Command interface without context.
 *
 * Enclose all operations that mutate system in commands. This can be easily
 * achieved with lambdas :
 *
 * Command cmd = system -> system.doSomething();
 *
 * @author jarekr
 */
@FunctionalInterface
public interface Command<T> extends ContextCommand<T> {

    @Override
    default void execute(T system, PrevalanceContext context) {
        this.execute(system);
    }

    /**
     * This operation will be exectued on system.
     *
     * @param system - reference to mutable system
     */
    void execute(T system);
}
