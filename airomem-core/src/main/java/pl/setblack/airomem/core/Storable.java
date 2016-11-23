/* Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package pl.setblack.airomem.core;

import java.io.Serializable;

/**
 * Base interface for building Persisten system.
 * <p>
 * Every persistent system in RAM should have one ROOT object.
 */
public interface Storable<READONLY> extends Serializable {

    READONLY getImmutable();
}
