/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core.sequnce;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jarek ratajski
 */
public class SequenceTest {

    private Sequence sequence;

    public SequenceTest() {
    }

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
