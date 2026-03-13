package com.leonifrazao.MoraNode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MoraNodeApplicationTests {

	@Test
	void applicationClassExists() {
		assertDoesNotThrow(() -> Class.forName("com.leonifrazao.MoraNode.MoraNodeApplication"));
	}

}
