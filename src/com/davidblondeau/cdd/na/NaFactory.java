package com.davidblondeau.cdd.na;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class to create internal representation of nucleic acids.
 * Only support for RNA is provided for now.
 * 
 * @author david
 *
 */
public class NaFactory {
	/**
	 * The accepted nucleotides for RNA sequences are a, u, c and g
	 */
	private final static String ANTI_WHITE_LIST_REGEX = "[^aucg]";
	
	/**
	 * Will help validate RNA sequences
	 */
	private final static Pattern RNA_SEQ_VALIDATOR = Pattern.compile(ANTI_WHITE_LIST_REGEX);
	
	/**
	 * Checks that the given sequence is a valid RNA sequence and returns an internal representation of that RNA sequence.
	 * @param sequence The RNA sequence
	 * @throws IllegalArgumentException if the sequence contains other characters than aAuUcCgG
	 * @return An internal representation of the corresponding RNA
	 */
	public static Rna newRna(String sequence) {
		if (sequence == null) throw new IllegalArgumentException("Null sequence");
		
		sequence = sequence.toLowerCase();
		
		Matcher matcher = RNA_SEQ_VALIDATOR.matcher(sequence);
		if (matcher.find()) {
			throw new IllegalArgumentException("The given nt sequence is not a valid RNA sequence. It should only contain aAuUcCgG. It contains '" + matcher.group() + "' at nt #" + (matcher.start() + 1));
		}
		return new RnaImpl(sequence);
	}
}
