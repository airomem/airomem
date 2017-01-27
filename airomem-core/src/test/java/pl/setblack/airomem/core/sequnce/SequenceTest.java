/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0 
 */
package pl.setblack.airomem.core.sequnce;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author jarek ratajski
 */
public class SequenceTest {

    private Sequence sequence;

    @Before
    public void setUp() {
        this.sequence = new Sequence();
    }

    @Test
    public void testGenerateId() {
        long id1 = sequence.generateId();
        long id2 = sequence.generateId();
        assertEquals(id1 + 1, id2);
    }

}
