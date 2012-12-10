package com.davidblondeau.cdd.na;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

public class SiRnaTest extends TestCase {
	private Rna rna1 =        new RnaImpl("aauguacguacugacugacuga");
	private Rna rna1_1diff =  new RnaImpl("aauguauguacugacugacuga");
	private Rna rna1_3diffs = new RnaImpl("aaugucuauacugacugacuga");
	private Rna rna1_4diffs = new RnaImpl("aaugucuauacucacugacuga");
	private SiRna siRna = new SiRna(rna1, 1, 21); // auguacguacugacugacuga
	
	public void testGetAntisenseMatchingSequence() {
		assertEquals("auguacguacugacugacuga", siRna.getAntisenseMatchingSequence().toString());
	}
	
	public void testCanOffTargetOtherMRnas_Can() {
		Set<Rna> mRnas = new HashSet<Rna>();
		mRnas.add(rna1);
		mRnas.add(rna1_1diff);
		assertTrue(siRna.canOffTargetOtherMRnas(mRnas));
	}
	
	public void testCanOffTargetOtherMRnas_Cannot() {
		Set<Rna> mRnas = new HashSet<Rna>();
		mRnas.add(rna1);
		mRnas.add(rna1_3diffs);
		assertFalse(siRna.canOffTargetOtherMRnas(mRnas));
	}
	
	public void testCanOffTargetOtherMRnas_Cannot2() {
		Set<Rna> mRnas = new HashSet<Rna>();
		mRnas.add(rna1);
		mRnas.add(rna1_3diffs);
		mRnas.add(rna1_4diffs);
		assertFalse(siRna.canOffTargetOtherMRnas(mRnas));
	}
	
	public void testCanOffTargetOtherMrnas_Boundaries() {
		try {
			siRna.canOffTargetOtherMRnas(null);
			fail("Should have caught null argument");
		} catch(IllegalArgumentException iae) {
			//good
		}
	}
	
	public void testCanOffTargetOtheMrnas_EmptySet() {
		Set<Rna> mRnas = new HashSet<Rna>();
		assertFalse(siRna.canOffTargetOtherMRnas(mRnas));
	}
}
