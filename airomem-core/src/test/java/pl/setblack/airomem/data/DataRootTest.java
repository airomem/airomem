/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0   http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.data;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Kanapka
 */
class DataRootTest {

	private DataRoot<BankAccount> bank;

	@BeforeEach
	public void setUp() {
		bank = new DataRoot<>(new BankAccount());
	}

	@Test
	void testCreation() {
		assertThat(bank).isNotNull();
		assertThat(bank.getDataObject().getAmount()).isNotNull();
	}

}
