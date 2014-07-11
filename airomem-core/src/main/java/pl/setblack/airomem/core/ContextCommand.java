/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core;

import java.io.Serializable;

/**
 *
 * @author Kanapka
 */
@FunctionalInterface
public interface ContextCommand<T extends Storable> extends Serializable {

    void execute(T system, PrevalanceContext context);
}
