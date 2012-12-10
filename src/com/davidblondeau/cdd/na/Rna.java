package com.davidblondeau.cdd.na;


/**
 * Ribonucleic acid (RNA) is a nucleic acid that consists of a long chain of nucleotide units.
 * 
 * @author david
 *
 */
public interface Rna extends NtSequence {
	
	/**
	 * Returns true if the given nucleotide sequence can hybridize well this RNA.
	 * Well means that there is at least one subsequence of this RNA that includes at most two 
	 * mismatches with the given sequence over its length
	 * 
	 * @param siRna
	 * @return
	 */
	public boolean hybridizeWellWith(SiRna siRna);
}
