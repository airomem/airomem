/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core;

import java.io.Serializable;

/**
 *
 * @author Kanapka
 */
public interface Command<T extends Storable, RESULT> extends Serializable {

    RESULT execute(T system);
}
