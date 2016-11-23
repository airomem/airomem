/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0 
 */
package pl.setblack.airomem.core.sequnce;

import pl.setblack.airomem.core.WriteChecker;

import java.io.Serializable;

/**
 * @author jarekr
 */
public class SequenceSystem implements Serializable {

    private final Sequence seq = new Sequence();

    private long number;


    public Sequence getSequence() {
        return seq;
    }


    public long getNumber() {
        return this.number;
    }

    public void setNumber(long id) {
        assert WriteChecker.hasPrevalanceContext();
        this.number = id;
    }

}
