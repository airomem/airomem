/* Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package pl.setblack.airomem.core;

/**
 * Query interface for query lambdas.
 */
public interface Query<READONLY, RESULT> {

    RESULT evaluate(READONLY object);

}
