package com.davidblondeau.cdd.na;

import junit.framework.TestCase;

public class RnaImplTest extends TestCase {
	private Rna rna1 =        new RnaImpl("aauguacguacugacugacuga");
	private Rna rna1_1diff =  new RnaImpl("aauguauguacugacugacuga");
	private Rna rna1_2diffs = new RnaImpl("aauguauauacugacugacuga");
	private Rna rna1_3diffs = new RnaImpl("aaugucuauacugacugacuga");
	private SiRna siRna = new SiRna(rna1, 1, 21); // auguacguacugacugacuga
	
	public void testHybridizeWellTargetRna() {
		assertTrue(rna1.hybridizeWellWith(siRna));
	}
	
	public void testHybridizeWellTarget1Diff() {
		assertTrue(rna1_1diff.hybridizeWellWith(siRna));
	}
	
	public void testHybridizeWellTarget2Diffs() {
		assertTrue(rna1_2diffs.hybridizeWellWith(siRna));
	}
	
	public void testHybridizeWellTarget3Diffs() {
		assertFalse(rna1_3diffs.hybridizeWellWith(siRna));
	}
	
	public void testHybridizeWellWithNullArg() {
		try {
			rna1.hybridizeWellWith(null);
			fail("Should throw a IllegalArgumentException");
		} catch(IllegalArgumentException iae) {
			//good
		}
	}
}
