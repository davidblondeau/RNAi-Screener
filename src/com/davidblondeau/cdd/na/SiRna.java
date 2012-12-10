/**
 * 
 */
package com.davidblondeau.cdd.na;

import java.util.Set;


/**
 * Small interfering RNA (siRNA) 
 * 
 * Provides the target mRna and the offset of the sequence matching this siRNA antisense strand.
 * 
 * @todo Way to compute the sense strand
 * @todo Methods to return the strands
 * @todo Should it be extending Rna?
 *  
 * @author david
 *
 */
public class SiRna {
	private Rna _mRna;
	private int _offset;
	private int _length;
	
	/**
	 * 
	 * @param targetMRna The mRNA that this siRNA targets 
	 * @param offset The offset of the targeted nt sequence on the mRNA
	 * @param length The length of the sequences
	 */
	public SiRna(Rna targetMRna, int offset, int length) {
		_mRna = targetMRna;
		_offset = offset;
		_length = length;
	}
	
	/**
	 * Returns the mRNA this siRNA targets
	 * @return
	 */
	public Rna getTargetMRna() {
		return _mRna;
	}
	
	/**
	 * Returns the offset of the target mRNA matching sequence
	 * @return
	 */
	public int getMRnaOffset() {
		return _offset;
	}
	
	/**
	 * Returns the sequence of the target mRNA that matches the antisense strand of this siRNA
	 * @return
	 */
	public NtSequence getAntisenseMatchingSequence() {
		return _mRna.getSubNtSequence(_offset+1, _offset+_length);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return _offset + ": " + getAntisenseMatchingSequence(); 
	}
	
	/**
	 * Check if this siRNA would off-target other mRnas than its target mRNA.
	 * A siRna can off-target a mRna if that mRna would hybridize well with it.
	 * 
	 * @param mRnas
	 * @return
	 */
	public boolean canOffTargetOtherMRnas(Set<Rna> mRnas) {
		if (mRnas == null) throw new IllegalArgumentException("Null mRna set");
		
		for (Rna mRna: mRnas) {
			if (!mRna.equals(_mRna)) {
				if (mRna.hybridizeWellWith(this)) {
					return true;
				}
			}
		}
		return false;
	}
	
}