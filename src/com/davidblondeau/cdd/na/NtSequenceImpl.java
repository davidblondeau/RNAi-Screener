package com.davidblondeau.cdd.na;

/**
 * Implementation of a nt sequence backed by a CharSequence.
 * 
 * @author david
 */
public class NtSequenceImpl implements NtSequence {
	private CharSequence _seq;
	
	public NtSequenceImpl(CharSequence seq) {
		if (seq == null) throw new IllegalArgumentException("Null seq");
		_seq = seq;
	}
	
	/* (non-Javadoc)
	 * @see com.davidblondeau.cdd.na.NtSequence#getSubNtSequence(int, int)
	 */
	public NtSequence getSubNtSequence(int firstNt, int lastNt) 
		throws IndexOutOfBoundsException
	{
		return new NtSequenceImpl(_seq.subSequence(firstNt-1, lastNt));
	}
	
	/* (non-Javadoc)
	 * @see com.davidblondeau.cdd.na.NtSequence#hasCorG(int)
	 */
	public boolean hasCorG(int pos) 
		throws IndexOutOfBoundsException
	{ 
		char c = _seq.charAt(pos-1);
		return c == 'g' || c == 'c';
	}
	
	/* (non-Javadoc)
	 * @see com.davidblondeau.cdd.na.NtSequence#hasAorU(int)
	 */
	public boolean hasAorU(int pos) 
		throws IndexOutOfBoundsException
	{ 
		char c = _seq.charAt(pos-1);
		return c == 'a' || c == 'u';
	}
	
	/* (non-Javadoc)
	 * @see com.davidblondeau.cdd.na.NtSequence#isAURich()
	 */
	public boolean isAURich() {
		int auCount = 0;
		for (int i = 1; i <= _seq.length(); ++i) {
			if (hasAorU(i)) {
				auCount++;
			}
		}
		
		return (2*auCount > (_seq.length()));
	}
	
	/* (non-Javadoc)
	 * @see com.davidblondeau.cdd.na.NtSequence#hasLongGCStretch(int)
	 */
	public boolean hasGCStretch(int minLength) {
		int longGC = 0;
		for (int i = 1; i < _seq.length(); ++i) {
			if (hasCorG(i)) {
				longGC++;
			} else {
				longGC = 0;
			}
			if (longGC>=minLength) {
				return true;
			}
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.davidblondeau.cdd.na.NtSequence#length()
	 */
	public int length() {
		return _seq.length();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.davidblondeau.cdd.na.NtSequence#distanceTo(com.davidblondeau.cdd.na.NtSequence)
	 */
	public int distanceTo(NtSequence sequence) 
	{
		if (sequence == null || (_seq.length() != sequence.length())) return Integer.MAX_VALUE;

		int distance = 0;
		
		
		CharSequence seq = sequence.toString();
		for (int i = 0; i < _seq.length(); ++i) {
			if (_seq.charAt(i) != seq.charAt(i)) {
				distance++;
			}
		}
		
		return distance;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return _seq.toString();
	}
}
