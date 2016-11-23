/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.sequnce;

import junit.framework.Assert;
import org.junit.Test;
import pl.setblack.airomem.core.builders.AbstractPrevaylerTest;

/**
 * @author jarekr
 */
public class SequencePersistenceTest
        extends AbstractPrevaylerTest<SequenceSystem> {

    @Override
    protected SequenceSystem createSystem() {
        return new SequenceSystem();
    }

    @Test
    public void testPersistenceOfSequence() {
        this.persistenceController.execute((x) -> x.setNumber(x.getSequence().generateId()));
        long id1 = this.persistenceController.query(x -> x.getNumber());
        reloadController(SequenceSystem.class);
        long id2 = this.persistenceController.query(x -> x.getNumber());
        Assert.assertEquals(id1, id2);
    }

    @Test
    public void testPersistenceOfSequence2() {
        this.persistenceController.execute((x) -> x.setNumber(x.getSequence().generateId()));
        long id1 = this.persistenceController.query(x -> x.getNumber());
        reloadController(SequenceSystem.class);
        this.persistenceController.execute((x) -> x.setNumber(x.getSequence().generateId()));
        long id2 = this.persistenceController.query(x -> x.getNumber());
        Assert.assertEquals(id1 + 1, id2);

    }


}
