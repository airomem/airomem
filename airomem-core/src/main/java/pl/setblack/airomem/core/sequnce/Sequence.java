/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core.sequnce;

import java.io.Serializable;

/**
 * Helper object used to generate sequences of unique ids.
 *
 * @author jarekr
 */
public class Sequence implements Serializable {

    private static final long serialVersionUID = 1l;

    private long number;

    public synchronized long generateId() {
        return this.number++;
    }
}
