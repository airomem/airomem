/* Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package pl.setblack.airomem.core;

import java.io.Serializable;

/**
 *
 * @author jarekr
 */
@FunctionalInterface
public interface ContextCommand<T extends Storable> extends Serializable {

    void execute(T system, PrevalanceContext context);
}
