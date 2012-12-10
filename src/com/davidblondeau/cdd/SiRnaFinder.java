/**
 * 
 */
package com.davidblondeau.cdd;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.davidblondeau.cdd.na.NaFactory;
import com.davidblondeau.cdd.na.NtSequence;
import com.davidblondeau.cdd.na.Rna;
import com.davidblondeau.cdd.na.SiRna;

/**
 * Find small interfering RNA (siRNA) candidates for a given mRNA based on Ui-Tei et al. Nucelic Acids Res., 32, 936-948:
 * i. A/U at the 5' end of the antisense strand;
 * ii. G/C at the 5' end of the sense strand;
 * iii. AU-richness in the 5' terminal one-third of the antisense strand; and
 * iv. the absence of any GC stretch over 9bp in length.
 * 
 * This will find siRNA of the given length
 *  
 * @author david
 *
 */
public class SiRnaFinder {

	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger("cdd.SiRnaFinder");
	
	private int _siRnaLen;

	public SiRnaFinder() {
		this(21);
	}
	
	public SiRnaFinder(int length) {
		// Don't know if the rules to select are the same for 22 and 23 nt sequences. Might have to remove this parameter if it is not the case.
		// Also check the length is between 21 and 23
		_siRnaLen = length;
	}

	/**
	 * Find the siRNA candidates for the given mRNA 
	 * @param mRna The mRNA
	 * @return An ordered set of siRNA candidates
	 */
	public Set<SiRna> findCandidates(Rna mRna) {
		
		Set<SiRna> candidates = new  LinkedHashSet<SiRna>();

		for (int i = 1; i <= mRna.length() - _siRnaLen  + 1; ++i) {
			NtSequence siRnaCandidate = mRna.getSubNtSequence(i, i + _siRnaLen - 1);
			if (siRnaCandidate.hasAorU(_siRnaLen) // i. A/U at the 5' end of the antisense strand;
				&& siRnaCandidate.hasCorG(3) // ii. G/C at the 5' end of the sense strand -> that is at the 3rd nt from the 3' end of the antisense strand
				&& siRnaCandidate.getSubNtSequence(_siRnaLen*2/3 + 1, _siRnaLen).isAURich() // iii. AU-richness in the 5' terminal one-third of the antisense strand
				&& !siRnaCandidate.hasGCStretch(10) //iv. the absence of any GC stretch over 9bp in length.		
				) 
			{
				candidates.add(new SiRna(mRna, i-1, _siRnaLen));
			}
		}
		
		return candidates;
	}

	
	/**
	 * Takes a mRNA sequence and prints the corresponding 21nt siRNA candidate list.
	 * Usage: java com.davidblondeau.cdd.SiRnaFinder <mRnaSequence>
	 */
	public static void main(String[] args) {
		if (args == null || args.length != 1) {
			LOG.info("Usage: java com.davidblondeau.cdd.SiRnaFinder <mRnaSequence>");
			return;
		}
		
		Rna mRna;
		try {
			mRna = NaFactory.newRna(args[0]);
		} catch(IllegalArgumentException iae) {
			LOG.info("Error while reading mRna: " + iae.getMessage());
			return;
		}
		
		Set<SiRna> candidates = new SiRnaFinder(21).findCandidates(mRna);
		
		if (candidates.isEmpty()) {
			LOG.info("No siRNA candidates found");
		} else {
			for (SiRna candidate: candidates) {
				LOG.info(candidate.toString());
			}
		}
	}

}
