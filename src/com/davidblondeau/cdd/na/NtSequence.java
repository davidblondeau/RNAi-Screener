package com.davidblondeau.cdd.na;

/**
 * A Nucleotide sequence.
 * Provides methods to explore the sequence as well as helper methods to find out about the sequence characteristics.
 * 
 * The first element of the sequence is at position 1.
 * 
 * @author david
 */
public interface NtSequence {

	/**
	 * Length of the sequence
	 * @return
	 */
	public int length();
	
	/**
	 * Returns a subsequence of the current sequence.
	 * 
	 * The first and last nucleotides are included in the subsequence
	 * 
	 * @param firstNt The first nucleotide to include
	 * @param lastNt The last nucleotide to include
	 * @return The subsequence
	 * @throws IndexOutOfBoundsException If the positions are out of this sequence boundaries
	 */
	public NtSequence getSubNtSequence(int firstNt, int lastNt) throws IndexOutOfBoundsException;

	/**
	 * Returns true if the nucleotide at the given position is a Cytidine or a Guanosine 	
	 * @param pos
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public boolean hasCorG(int pos) throws IndexOutOfBoundsException;

	/**
	 * Returns true if the nucleotide at the given position is an Adenosine or a Uridine
	 * @param pos
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public boolean hasAorU(int pos) throws IndexOutOfBoundsException;

	/**
	 * Returns true if the sequence has more Adenosine or Uridine nucleotides than Cytidine or Guanosine nucleotides
	 * @return
	 */
	public boolean isAURich();

	/**
	 * Returns true if the sequence has at least one stretch of Cytidine and Guanosine nucleotides of at least minlength
	 * @param minLength The minimum length of the stretch to look for
	 * @return
	 */
	public boolean hasGCStretch(int minLength);

	/**
	 * Returns the distance between the given sequence and this sequence
	 * If the sequences have different length, then Integer.MAX_VALUE is returned
	 *  
	 * The distance increases by 1 for each position at which the nucleotides of the sequences do not match
	 * @param sequence
	 * @return
	 */
	public int distanceTo(NtSequence sequence);
	
	/**
	 * User readable representation of the sequence
	 * @return
	 */
	public String toString();
}