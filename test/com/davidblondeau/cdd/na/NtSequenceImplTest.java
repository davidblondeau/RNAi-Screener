package com.davidblondeau.cdd.na;

import junit.framework.TestCase;

public class NtSequenceImplTest extends TestCase {
	private static final String FIRST_SEQ = "auguacguacugacugacuga";
	private static final String SECOND_SEQ = "guauguacu";
	private static final String THIRD_SEQ =  "ggcgcuacu";
	
	
	private NtSequenceImpl ntSeq1,ntSeq2, richGCSeq;
	
	public void setUp() {
		ntSeq1 = new NtSequenceImpl(FIRST_SEQ);
		ntSeq2 = new NtSequenceImpl(SECOND_SEQ);
		richGCSeq = new NtSequenceImpl(THIRD_SEQ);
		
		assertEquals(ntSeq1.length(), FIRST_SEQ.length());
		assertEquals(ntSeq2.length(), SECOND_SEQ.length());
		assertEquals(ntSeq1.toString(), FIRST_SEQ);
		assertEquals(ntSeq2.toString(), SECOND_SEQ);
	}
	
	public void testisAorU() {
		assertTrue(ntSeq1.hasAorU(1));
		assertTrue(ntSeq1.hasAorU(4));
		assertFalse(ntSeq1.hasAorU(3));
		assertFalse(ntSeq1.hasAorU(18));
	}
	
	public void testisAorUBoundaries() {
		// boundary tests
		try {
			ntSeq1.hasAorU(0);
			fail("Index of NtSequence starts at 1");
		} catch(Exception e) {
			// nothing
		}
		
		try {
			ntSeq1.hasAorU(FIRST_SEQ.length() + 1);
			fail("Can reach beyond the sequence");
		} catch(Exception e) {
			// nothing
		}
	}
	
	public void testisCorG() {
		assertTrue(ntSeq1.hasCorG(3));
		assertTrue(ntSeq1.hasCorG(20));
		assertFalse(ntSeq1.hasCorG(19));
		assertFalse(ntSeq1.hasCorG(5));
	}
	
	public void testisCorGBoundaries() {
		// boundary tests
		try {
			ntSeq1.hasCorG(0);
			fail("Index of NtSequence starts at 1");
		} catch(Exception e) {
			// nothing
		}
		
		try {
			ntSeq1.hasCorG(FIRST_SEQ.length() + 1);
			fail("Can reach beyond the sequence");
		} catch(Exception e) {
			// nothing
		}
	}
	
	public void testGetSubSequence() {
		NtSequence subSeq = ntSeq1.getSubNtSequence(1, 10);
		assertNotNull(subSeq);
		assertEquals(10, subSeq.length());
		assertEquals("auguacguac", subSeq.toString());
	}
	
	public void testGetSubSequenceBoundaries() {
	
		// boundary tests
		try {
			ntSeq2.getSubNtSequence(1, 10);
			fail("Managed to get a subsequence longer than the original sequence");
		} catch(IndexOutOfBoundsException ioobe) {
			// nothing
		}
		
		try {
			ntSeq2.getSubNtSequence(0, 4);
			fail("Managed to get a subsequence longer than the original sequence");
		} catch(IndexOutOfBoundsException ioobe) {
			// nothing
		}
		
		try {
			ntSeq2.getSubNtSequence(3, 10);
			fail("Managed to get a subsequence longer than the original sequence");
		} catch(IndexOutOfBoundsException ioobe) {
			// nothing
		}
	}
	
	public void testHasGCStretch() {
		assertFalse(ntSeq2.hasGCStretch(4));
		assertTrue(richGCSeq.hasGCStretch(3));
		assertTrue(richGCSeq.hasGCStretch(4));
		assertTrue(richGCSeq.hasGCStretch(5));
		assertFalse(richGCSeq.hasGCStretch(6));
	}
	
	public void testIsAURich() {
		assertTrue(ntSeq2.isAURich());
		assertFalse(richGCSeq.isAURich());
		assertTrue(ntSeq1.getSubNtSequence(15, 21).isAURich());
	}
	
	public void testDistanceTo() {
		assertEquals(4, ntSeq2.distanceTo(richGCSeq));
		assertEquals(0, ntSeq1.distanceTo(ntSeq1));
	}
	
	public void testDistanceToNullArg() {
		assertEquals(Integer.MAX_VALUE, ntSeq1.distanceTo(null));
	}
	
	public void testDistanceToDifferentLengths() {
		assertEquals(Integer.MAX_VALUE, ntSeq1.distanceTo(ntSeq2));
	}
}
