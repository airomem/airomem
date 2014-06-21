/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core;

import java.io.Serializable;

/**
 *
 * @author Kanapka
 */
public interface Command<T extends Storable> extends Serializable {

    void execute(T system);
}
