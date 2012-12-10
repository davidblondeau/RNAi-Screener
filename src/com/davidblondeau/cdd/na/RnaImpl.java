package com.davidblondeau.cdd.na;


/**
 * Implementation of the RNA.
 * @author david
 *
 */
public class RnaImpl extends NtSequenceImpl implements Rna {
	private static final int MAX_DISTANCE_TO_HYBRIDIZE_WELL = 2;
	
	public RnaImpl(CharSequence seq) {
		super(seq);
	}

	/*
	 * (non-Javadoc)
	 * @see com.davidblondeau.cdd.na.Rna#hybridizeWellWith(com.davidblondeau.cdd.SiRna)
	 */
	public boolean hybridizeWellWith(SiRna siRna) {
		if (siRna == null) throw new IllegalArgumentException("null siRna");
		
		if (this.equals(siRna.getTargetMRna())) return true;
		
		NtSequence siRnaSeq = siRna.getAntisenseMatchingSequence();
		for (int offset = 0; offset <= length() - siRnaSeq.length(); ++offset) {
			NtSequence subSeq = getSubNtSequence(offset+1, offset+siRnaSeq.length());
			if (subSeq.distanceTo(siRnaSeq) <= MAX_DISTANCE_TO_HYBRIDIZE_WELL) {
				// We found a sub-sequence that would hybridize with the given siRna
				return true;
			}
		}
		return false;
	}
	

}
