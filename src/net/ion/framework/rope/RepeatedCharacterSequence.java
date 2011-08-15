package net.ion.framework.rope;

import java.util.Arrays;

/**
 * A character sequence defined by a character and a repeat count.
 * 
 * @author Amin Ahmad
 */
public class RepeatedCharacterSequence implements CharSequence {

	private char character;
	private int repeat;

	public RepeatedCharacterSequence(char character, int repeat) {
		super();
		this.character = character;
		this.repeat = repeat;
	}

	public char charAt(int index) {
		return getCharacter();
	}

	public int length() {
		return repeat;
	}

	public CharSequence subSequence(int start, int end) {
		return new RepeatedCharacterSequence(getCharacter(), end - start);
	}

	public String toString() {
		char[] result = new char[repeat];
		Arrays.fill(result, character);
		return new String(result);
	}

	/**
	 * Returns the character used to construct this sequence.
	 * 
	 * @return the character used to construct this sequence.
	 */
	public char getCharacter() {
		return character;
	}

}
