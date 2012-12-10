package com.davidblondeau.cdd.na;

import junit.framework.TestCase;

public class NaFactoryTest extends TestCase {

	public void testValidRNASequence() {
		Rna rna = NaFactory.newRna("auAGaucG");
		assertNotNull(rna);
		assertEquals(8, rna.length());
		assertEquals("auagaucg", rna.toString());
	}
	
	public void testInvalidRNASequence() {
		try {
			NaFactory.newRna("acgucguct");
			fail("Test should have failed");
		} catch(IllegalArgumentException e) {
			// correct
		}
	}
}
