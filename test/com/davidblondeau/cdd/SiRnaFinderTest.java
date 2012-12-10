package com.davidblondeau.cdd;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.davidblondeau.cdd.na.NaFactory;
import com.davidblondeau.cdd.na.Rna;
import com.davidblondeau.cdd.na.SiRna;

import junit.framework.TestCase;

public class SiRnaFinderTest extends TestCase {

	public void testFindCandidates() {
		Rna mRna = NaFactory.newRna("aauguacguacugacugacugacgcguacguacugaucg");
		Map<Integer,String> expectedResults = new HashMap<Integer,String>();
		expectedResults.put(1, "auguacguacugacugacuga");
		expectedResults.put(16, "gacugacgcguacguacugau");
		
		Set<SiRna> siRnas = new SiRnaFinder(21).findCandidates(mRna);
		assertNotNull(siRnas);
		assertEquals("Found a different numbers of candidates than expected", 2, siRnas.size());

		for (SiRna candidate : siRnas) {
			assertEquals("Found a candidate that is for the wrong mRNA", mRna, candidate.getTargetMRna());
		}
		
		assertFindingCompleteness(expectedResults, siRnas);
	}
	
	public void testFindCandidates2() {
		Rna mRna = NaFactory.newRna("aauguauguacugacugacuga");
		Map<Integer,String> expectedResults = new HashMap<Integer,String>();
		expectedResults.put(1, "auguauguacugacugacuga");
		
		Set<SiRna> siRnas = new SiRnaFinder(21).findCandidates(mRna);
		assertNotNull(siRnas);
		assertEquals("Found a different numbers of candidates than expected", 1, siRnas.size());

		for (SiRna candidate : siRnas) {
			assertEquals("Found a candidate that is for the wrong mRNA", mRna, candidate.getTargetMRna());
		}
		
		assertFindingCompleteness(expectedResults, siRnas);		
	}
	
	public void testFindCandidatesNone() {
		Rna mRna = NaFactory.newRna("auauauauauauauauauauauauuuuaauauauau");
		
		Set<SiRna> siRnas = new SiRnaFinder(21).findCandidates(mRna);
		assertNotNull(siRnas);
		assertEquals("Found a different numbers of candidates than expected", 0, siRnas.size());
	}
	
	public void testFindCandidatesNone2() {
		Rna mRna = NaFactory.newRna("gcgcgcgcgcauauauauauagcc");
		
		Set<SiRna> siRnas = new SiRnaFinder(21).findCandidates(mRna);
		assertNotNull(siRnas);
		assertEquals("Found a different numbers of candidates than expected", 0, siRnas.size());
	}
	

	/**
	 * Check that the list of candidates found matches exactly with what was expected
	 * @param expectedResults
	 * @param siRnas
	 */
	private void assertFindingCompleteness(Map<Integer,String> expectedResults, Set<SiRna> siRnas) {
		for (SiRna candidate : siRnas) {
			assertNotNull(candidate);
			assertTrue(candidate.getMRnaOffset() >= 0);
			String expectedSeq = expectedResults.remove(candidate.getMRnaOffset());
			assertNotNull("Found a candidate that was not expected", expectedSeq);
			assertEquals("The subsequence at offset " + candidate.getMRnaOffset() + " is not as expected", expectedSeq, candidate.getAntisenseMatchingSequence().toString());
		}
		
		assertTrue("We were expecting more candidates", expectedResults.isEmpty());
	}
}
