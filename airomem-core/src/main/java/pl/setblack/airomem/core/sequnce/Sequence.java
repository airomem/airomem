/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.sequnce;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Helper object used to generate sequences of unique ids.
 *
 * @author jarekr
 */
public class Sequence implements Serializable {

    private static final long serialVersionUID = 1l;

    private final AtomicLong number = new AtomicLong(0);

    public synchronized long generateId() {
        return this.number.incrementAndGet();
    }
}
