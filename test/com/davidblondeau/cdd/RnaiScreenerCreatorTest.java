package com.davidblondeau.cdd;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.davidblondeau.cdd.na.Rna;
import com.davidblondeau.cdd.na.RnaImpl;
import com.davidblondeau.cdd.na.SiRna;

import junit.framework.TestCase;

public class RnaiScreenerCreatorTest extends TestCase {
	private static final String MRNA1 = "aauguacguauugacugacugaauacacuauacugacugacucuauuguacguacugacugacuga";
	private static final String MRNA2 = "aaugucuaugcucacugacugaauucacuauacucacugagucaa";
	private static final String MRNA3 = "aauguauguacugacugacuga";
	private static final String MRNA4 = "aaucgcuaugcucacugacugauaucacuauacucacugacacaa";
	
	private static final String NEWLINE = System.getProperty("line.separator");
	private static final String SCREENER_INPUT = MRNA1 + NEWLINE + MRNA2;
	private static final String SCREENER_OUTPUT = MRNA1 + "|1:auguacguauugacugacuga|4:uacguauugacugacugaaua|10:uugacugacugaauacacuau|23:uacacuauacugacugacucu"
		+ NEWLINE + MRNA2 + "|1:augucuaugcucacugacuga|3:gucuaugcucacugacugaau|8:ugcucacugacugaauucacu|10:cucacugacugaauucacuau";
	
	private Rna mRna1 = new RnaImpl(MRNA1);
	private Rna mRna2 = new RnaImpl(MRNA2);
	private Rna mRna3 = new RnaImpl(MRNA3);
	private Rna mRna4 = new RnaImpl(MRNA4);
	
	private Set<SiRna> siRna1,siRna2;
	RnaiScreenerCreator creator = new RnaiScreenerCreator();
	
	
	@Override
	public void setUp() {
		siRna1 = new LinkedHashSet<SiRna>();
		siRna1.add(new SiRna(mRna1, 1, 21));
		siRna1.add(new SiRna(mRna1, 4, 21));
		siRna1.add(new SiRna(mRna1, 10, 21));
		siRna1.add(new SiRna(mRna1, 23, 21));
		siRna1.add(new SiRna(mRna1, 26, 21));
		siRna1.add(new SiRna(mRna1, 32, 21));
		siRna1.add(new SiRna(mRna1, 45, 21));
		
		siRna2 = new LinkedHashSet<SiRna>();
		siRna2.add(new SiRna(mRna2, 1, 21));
		siRna2.add(new SiRna(mRna2, 3, 21));
		siRna2.add(new SiRna(mRna2, 8, 21));
		siRna2.add(new SiRna(mRna2, 10, 21));
		siRna2.add(new SiRna(mRna2, 23, 21));
	}
	
	public void testFindSiRnaCandidates() {
		Set<Rna> mRnas = new LinkedHashSet<Rna>();
		mRnas.add(mRna1);
		mRnas.add(mRna2);
		
		Map<Rna,Set<SiRna>> candidates = creator.findCandidateSiRnas(mRnas);
		assertNotNull(candidates);
		assertFalse(candidates.isEmpty());
		assertEquals(2, candidates.keySet().size());
		assertNotNull(candidates.get(mRna1));
		assertEquals(7, candidates.get(mRna1).size());
		assertNotNull(candidates.get(mRna2));
		assertEquals(5, candidates.get(mRna2).size());
	}

	public void testFindSiRnaCandidates_Not_Enough_Candidates() {
		Set<Rna> mRnas = new LinkedHashSet<Rna>();
		mRnas.add(mRna3);
		mRnas.add(mRna2);
		
		Map<Rna,Set<SiRna>> candidates = creator.findCandidateSiRnas(mRnas);
		assertNull(candidates);
	}
	
	public void testSelectOffTargetingFreeSiRnas() {
		Set<Rna> mRnas = new LinkedHashSet<Rna>();
		mRnas.add(mRna1);
		mRnas.add(mRna2);
		
		Set<SiRna> rna1Screen = creator.selectOffTargetingFreeSiRnas(siRna1, mRnas);
		assertNotNull(rna1Screen);
		assertEquals(4, rna1Screen.size());
		
	}
	
	public void testSelectOffTargetingFreeSiRnas2() {
		Set<Rna> mRnas = new LinkedHashSet<Rna>();
		mRnas.add(mRna1);
		mRnas.add(mRna2);
		
		Set<SiRna> rna2Screen = creator.selectOffTargetingFreeSiRnas(siRna2, mRnas);
		assertNotNull(rna2Screen);
		assertEquals(4, rna2Screen.size());
	}
	
	public void testSelectOffTargetingFreeSiRnas_NotEnoughCandidates() {
		Set<Rna> mRnas = new LinkedHashSet<Rna>();
		mRnas.add(mRna2);
		mRnas.add(mRna4);
		
		Set<SiRna> rna2Screen = creator.selectOffTargetingFreeSiRnas(siRna2, mRnas);
		assertNull(rna2Screen);		
	}
	
	public void testCreateScreener() {
		Set<Rna> mRnas = new LinkedHashSet<Rna>();
		mRnas.add(mRna1);
		mRnas.add(mRna2);
		
		Map<Rna,Set<SiRna>> screener = creator.createScreener(mRnas);
		assertNotNull(screener);
		assertEquals(2, screener.keySet().size());
		
		Set<SiRna> rna1Screen = screener.get(mRna1);
		assertNotNull(rna1Screen);
		assertEquals(4, rna1Screen.size());
		
		Set<SiRna> rna2Screen = screener.get(mRna1);
		assertNotNull(rna2Screen);
		assertEquals(4, rna2Screen.size());
		
	}
	
	public void testCreateScreener_Boundaries() {
		try {
			creator.createScreener(null);
			fail();
		} catch(IllegalArgumentException iae) {
			// good
		}
	}
	
	public void testCreateScreenerRW() {
		StringReader reader = new StringReader(SCREENER_INPUT);
		StringWriter writer = new StringWriter();
		try {
			creator.createScreener(reader, writer);
		} catch(IOException ioe) {
			fail(ioe.getMessage());
		}
		assertEquals(SCREENER_OUTPUT, writer.toString());
		
	}
	
	public void testCreateScreenerRW_NoScreener() {
		StringReader reader = new StringReader(MRNA3);
		StringWriter writer = new StringWriter();
		try {
			creator.createScreener(reader, writer);
		} catch(IOException ioe) {
			fail(ioe.getMessage());
		}
		assertEquals("", writer.toString());
		
	}
	
	public void testCreateScreenerRW_Invalid_MRNA() {
		StringReader reader = new StringReader(SCREENER_INPUT + "t");
		StringWriter writer = new StringWriter();
		try {
			creator.createScreener(reader, writer);
			fail("Did not catch invalid mRNA sequence");
		} catch(IOException ioe) {
			// good
		}
	}
	
	public void testCreateScreenerRW_NullArguments() {
		StringReader reader = new StringReader(SCREENER_INPUT);
		StringWriter writer = new StringWriter();
		
		try {
			creator.createScreener(null, writer);
			fail("Did not catch null reader");
		} catch(IllegalArgumentException ie) {
			// good
		} catch(IOException ioe) {
			fail(ioe.getMessage());
		}
		
		try {
			creator.createScreener(reader, null);
			fail("Did not catch null reader");
		} catch(IllegalArgumentException ie) {
			// good
		} catch(IOException ioe) {
			fail(ioe.getMessage());
		}
	}
}
