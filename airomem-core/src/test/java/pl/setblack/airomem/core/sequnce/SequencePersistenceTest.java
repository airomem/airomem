/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.core.sequnce;

import junit.framework.Assert;
import org.junit.Test;
import pl.setblack.airomem.core.AbstractPrevaylerTest;
import pl.setblack.airomem.core.Storable;

/**
 *
 * @author jarekr
 */
public class SequencePersistenceTest
        extends AbstractPrevaylerTest<SequenceSystem, SequenceSystemView> {

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
