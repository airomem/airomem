/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.setblack.airomem.core;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import org.junit.jupiter.api.Test;

/**
 * @author jratajsk
 */
class CommandTest {

	private static final String COMMAND_PERFORMED_MARKER = "command performed";

	@Test
	void testExecuteWithContext() {
		StorableObject s = StorableObject.createTestObject();
		assertThat(COMMAND_PERFORMED_MARKER).isNotSameAs(s.getImmutable().get("test"));
		VoidCommand<StorableObject> cmd = x -> x.internalMap.put("test", COMMAND_PERFORMED_MARKER);
		cmd.execute(s, new PrevalanceContext(new Date()));
		assertThat(COMMAND_PERFORMED_MARKER).isEqualTo(s.getImmutable().get("test"));
	}

}
