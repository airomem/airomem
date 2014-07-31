/* Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package pl.setblack.airomem.core;

/**
 *
 * @author Kanapka
 */
public interface Query<READONLY, RESULT> {

    RESULT evaluate(READONLY object);

}
