/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0 
 */
package pl.setblack.airomem.core.sequnce;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author jarek ratajski
 */
class SequenceTest {

    private Sequence sequence;

    @BeforeEach
    public void setUp() {
        this.sequence = new Sequence();
    }

    @Test
    void testGenerateId() {
        long id1 = sequence.generateId();
        long id2 = sequence.generateId();
        assertThat(id1 + 1).isEqualTo(id2);
    }

}
