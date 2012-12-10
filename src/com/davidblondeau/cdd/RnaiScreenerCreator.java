package com.davidblondeau.cdd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.davidblondeau.cdd.na.NaFactory;
import com.davidblondeau.cdd.na.Rna;
import com.davidblondeau.cdd.na.SiRna;

import java.util.logging.*;

/**
 * Creates RNAi screeners.
 * 
 * Finds sets of siRNAs for each of a set of target mRNAs in order to create an RNAi screening library. 
 * For each mRNA it picks four good candidate siRNAs (if possible) that do not hybridize well with any other mRNA 
 * in the target set.
 * 
 * @author david
 *
 */
public class RnaiScreenerCreator {
	/**
	 * Numbers of siRNAs expected for each mRNA.
	 */
	private static final int SIRNA_SCREEN_SIZE = 4;
	
	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger("cdd.RnaiScreenerFinder");
	
	/**
	 * Outputs an RNAi screener to the given writer based on the mRNAs retrieved from the given reader.
	 * 
	 * The input must contain one mRNA sequence by line. Empty lines and lines starting with the character '#' are ignored.
	 * For example:
	 * #mRNA #1
     * aauguacguauugacugacugaauacacuauacugacugacucuauuguacguacugacugacuga
     * 
     * #mRNA #2
	 * aaugucuaugcucacugacugaauucacuauacucacugagucaa
	 * 
	 * The output will be of the form <mRNA sequence>|<offset>:<siRNA sequence>...
	 * For example:
	 * aauguacguauugacugacugaauacacuauacugacugacucuauuguacguacugacugacuga|1:auguacguauugacugacuga|4:uacguauugacugacugaaua|10:uugacugacugaauacacuau|23:uacacuauacugacugacucu
	 * aaugucuaugcucacugacugaauucacuauacucacugagucaa|1:augucuaugcucacugacuga|3:gucuaugcucacugacugaau|8:ugcucacugacugaauucacu|10:cucacugacugaauucacuau
	 * 
	 * @param reader
	 * @param writer
	 * @throws IOException
	 */
	public void createScreener(Reader reader, Writer writer) 
		throws IOException
	{
		if (reader == null) throw new IllegalArgumentException("Null writer");
		if (writer == null) throw new IllegalArgumentException("Null reader");
		
		Set<Rna> mRnas = readMRnas(reader);

		if (mRnas.isEmpty()) {
			LOG.info("No valid mRNAs could be retrieved from input");
			return;
		}
		
		Map<Rna,Set<SiRna>> screener = createScreener(mRnas);
		if (screener == null) {
			LOG.info("Could not find a good screener for the given mRNAs");
			return;
		}
		
		writeScreener(screener, writer, mRnas);
	}

	/**
	 * Return a valid RNAi screener for the given mRNAs.
	 * 
	 * @param mRnas The set of mRNA to screen
	 * @return A set of 4 siRNAs for each mRNA. Each siRNA will not off-target the other mRNAs. Null if a screener cannot be created.
	 */
	public Map<Rna,Set<SiRna>> createScreener(Set<Rna> mRnas) {
		if (mRnas == null) throw new IllegalArgumentException("Null mRNA list");

		Map<Rna,Set<SiRna>> rnaiScreener = findCandidateSiRnas(mRnas);
		if (rnaiScreener == null) {
			// not enough siRNA candidates to create a screener.
			return null;
		}
		
		// For each mRNA, find 4 siRNA candidates that do not hybridize well with the other mRNA
		for(Rna mRna: mRnas) {
			Set<SiRna> currentMRnaCandidates = rnaiScreener.get(mRna);
			
			Set<SiRna> offTargetingFreeCandidates = selectOffTargetingFreeSiRnas(currentMRnaCandidates, mRnas);
			if (offTargetingFreeCandidates == null) {
				// not enough siRNA that would not off target other mRNAs.
				return null;
			}
			rnaiScreener.put(mRna, offTargetingFreeCandidates);
		}
		
		return rnaiScreener;
		
	}

	/**
	 * Reads a list of mRNAs from the given reader and return them as a set.
	 * 
	 * @throws IOException
	 */
	protected Set<Rna> readMRnas(Reader reader)
		throws IOException
	{
		Set<Rna> mRnas = new LinkedHashSet<Rna>();
		BufferedReader bufReader = new BufferedReader(reader);
		try {
			String line;
			int lineCount = 0;
			do {
				line = bufReader.readLine();
				lineCount++;
				if (line != null) {
					line = line.trim();
					if (line.length() != 0 && !line.startsWith("#")) {
						try {
							Rna mRna = NaFactory.newRna(line);
							mRnas.add(mRna);
						} catch(IllegalArgumentException iae) {
							// We make it an IO related exception since it is the input that is causing the error
							throw new IOException("Error at line " + lineCount + " of input: " + iae.getMessage());
						}
					}
				}
			} while (line != null);
		} finally {	
			bufReader.close();
		}
		return mRnas;
	}

	/**
	 * Write the screener to the provided writer
	 */
	private void writeScreener(Map<Rna, Set<SiRna>> screener, Writer writer, Set<Rna> mRnas)
		throws IOException
	{
		BufferedWriter bufWriter = new BufferedWriter(writer);
		try {
			Iterator<Rna> mRnaIter = mRnas.iterator();
			while(mRnaIter.hasNext()) {
				Rna mRna = mRnaIter.next();
				bufWriter.append(mRna.toString());
				Set<SiRna> siRnas = screener.get(mRna);
				for (SiRna siRna: siRnas) {
					bufWriter.append('|').append(Integer.toString(siRna.getMRnaOffset())).append(':').append(siRna.getAntisenseMatchingSequence().toString());
				}
				if (mRnaIter.hasNext()) {
					bufWriter.newLine();
				}
			}
			LOG.info("RNAi screener written to output");
		} finally {
			bufWriter.flush();
			bufWriter.close();
		}
	}


	/**
	 * Returns a set of of at least 4 siRNA candidates for each given mRNA.
	 *  
	 * Fails fast if one of the given mRNA does not have at least 4 siRNA candidates. Returns null then.
	 * 
	 * @param mRnas
	 * @return
	 */
	protected Map<Rna,Set<SiRna>> findCandidateSiRnas(Set<Rna> mRnas) {
		// Find siRNA candidates for each mRNA
		SiRnaFinder siRnaFinder = new SiRnaFinder();
		Map<Rna,Set<SiRna>> siRnaCandidates = new HashMap<Rna,Set<SiRna>>();
		for(Rna mRna: mRnas) {
			Set<SiRna> candidates = siRnaFinder.findCandidates(mRna);
			if (candidates.size() < SIRNA_SCREEN_SIZE) {
				LOG.fine("Found only " + candidates.size() + " siRNA for mRNA " + mRna);
				return null;
			} else {
				LOG.fine("siRNA candidates for " + mRna);
				for (SiRna siRna: candidates) {
					LOG.fine(siRna.toString());
				}
			}
			siRnaCandidates.put(mRna, candidates);
		}
		
		return siRnaCandidates;
	}
	
	/**
	 * Supposes there are at least 4 elements in the siRnas set.
	 * the target mRna can be part of the mRnas to check against. It will be ignored.
	 * 
	 * @param siRnas
	 * @param mRnas
	 * @return
	 */
	protected Set<SiRna> selectOffTargetingFreeSiRnas(Set<SiRna> siRnas, Set<Rna> mRnas) {
		Set<SiRna> offTargetingFreeCandidates = new LinkedHashSet<SiRna>();
		for (SiRna candidate: siRnas) {
			if (!candidate.canOffTargetOtherMRnas(mRnas)) {
				LOG.fine(candidate  + " would not off-target the other mRNAs");
				offTargetingFreeCandidates.add(candidate);
			} else {
				LOG.fine(candidate  + " could off-target the other mRNAs");
			}				
			if (offTargetingFreeCandidates.size() == SIRNA_SCREEN_SIZE) {
				break;
			}
		}
		if (offTargetingFreeCandidates.size() < SIRNA_SCREEN_SIZE) {
			LOG.fine("Found only " + offTargetingFreeCandidates.size() + " siRNA(s) that would not off-targe other mRNAs for mRNA " + siRnas.iterator().next().getTargetMRna());
			return null;
		}
		
		return offTargetingFreeCandidates;
	}
	
	/**
	 * Reads a list of mRNAs from an input file and writes an RNAi screener to the output file if one exists.
	 * Usage: java com.davidblondeau.cdd.RnaiScreenerCreator <inputFilePath> <outputFilePath>
	 */
	public static void main(String[] args) {
		FileReader reader = null;
		FileWriter writer = null;
		
		if (args.length != 2) {
			LOG.info("Usage: java com.davidblondeau.cdd.RnaiScreenerCreator <inputFilePath> <outputFilePath>");
			return;
		}
		
		String inputFileName = args[0].trim();
		String outputFileName = args[1].trim();
		
		
		try {
			reader = new FileReader(inputFileName);
			writer = new FileWriter(outputFileName);
		
			new RnaiScreenerCreator().createScreener(reader, writer);
		} catch(Exception e) {
			LOG.severe("Exception while creating RNAi screener: " + e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ioe) {
					// we tried
				}
			}
			if (writer != null) {
				try  {
					writer.close();
				} catch(IOException ioe) {
					// we tried
				}
			}
		}
	}
}