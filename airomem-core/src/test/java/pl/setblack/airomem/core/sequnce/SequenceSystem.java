/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0 
 */
package pl.setblack.airomem.core.sequnce;

import pl.setblack.airomem.core.Storable;
import pl.setblack.airomem.core.WriteChecker;
import pl.setblack.airomem.core.WriteCheckerTest;

/**
 *
 * @author jarekr
 */
public class SequenceSystem implements Storable<SequenceSystemView>, SequenceSystemView {

    private final Sequence seq = new Sequence();

    private long number;

    @Override
    public SequenceSystemView getImmutable() {
        return this;
    }

    public Sequence getSequence() {
        return seq;
    }

    @Override
    public long getNumber() {
        return this.number;
    }

    public void setNumber(long id) {
        assert WriteChecker.hasPrevalanceContext();
        this.number = id;
    }

}
