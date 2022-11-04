/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.core.sequnce;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.setblack.airomem.core.builders.AbstractPrevaylerTest;

/**
 * @author jarekr
 */
class SequencePersistenceTest
		extends AbstractPrevaylerTest<SequenceSystem> {

	@Override
	protected SequenceSystem createSystem() {
		return new SequenceSystem();
	}

	@Test
    void testPersistenceOfSequence() {
		this.persistenceController.execute((x) -> x.setNumber(x.getSequence().generateId()));
		long id1 = this.persistenceController.query(x -> x.getNumber());
		reloadController(SequenceSystem.class);
		long id2 = this.persistenceController.query(x -> x.getNumber());
		assertThat(id1).isEqualTo(id2);
	}

	@Test
    void testPersistenceOfSequence2() {
		this.persistenceController.execute((x) -> x.setNumber(x.getSequence().generateId()));
		long id1 = this.persistenceController.query(x -> x.getNumber());
		reloadController(SequenceSystem.class);
		this.persistenceController.execute((x) -> x.setNumber(x.getSequence().generateId()));
		long id2 = this.persistenceController.query(x -> x.getNumber());
        assertThat(id1 + 1).isEqualTo(id2);
	}


}
