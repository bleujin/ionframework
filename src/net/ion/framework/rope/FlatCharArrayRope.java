package net.ion.framework.rope;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A rope constructed from a character array. This rope is even flatter than a regular flat rope.
 * 
 * @author Amin Ahmad
 */
public final class FlatCharArrayRope extends AbstractRope implements FlatRope {

	private final char[] sequence;

	/**
	 * Constructs a new rope from a character array.
	 * 
	 * @param sequence
	 *            the character array.
	 */
	public FlatCharArrayRope(final char[] sequence) {
		this(sequence, 0, sequence.length);
	}

	/**
	 * Constructs a new rope from a character array range.
	 * 
	 * @param sequence
	 *            the character array.
	 * @param offset
	 *            the offset in the array.
	 * @param length
	 *            the length of the array.
	 */
	public FlatCharArrayRope(final char[] sequence, final int offset, final int length) {
		if (length > sequence.length)
			throw new IllegalArgumentException("Length must be less than " + sequence.length);
		this.sequence = new char[length];
		System.arraycopy(sequence, offset, this.sequence, 0, length);
	}

	public char charAt(final int index) {
		return this.sequence[index];
	}

	public byte depth() {
		return 0;
	}

	/*
	 * Implementation Note: This is a reproduction of the AbstractRope indexOf implementation. Calls to charAt have been replaced with direct array access to improve speed.
	 */
	public int indexOf(final char ch) {
		for (int j = 0; j < this.sequence.length; ++j)
			if (this.sequence[j] == ch)
				return j;
		return -1;
	}

	/*
	 * Implementation Note: This is a reproduction of the AbstractRope indexOf implementation. Calls to charAt have been replaced with direct array access to improve speed.
	 */
	public int indexOf(final char ch, final int fromIndex) {
		if (fromIndex < 0 || fromIndex >= this.length())
			throw new IndexOutOfBoundsException("Rope index out of range: " + fromIndex);
		for (int j = fromIndex; j < this.sequence.length; ++j)
			if (this.sequence[j] == ch)
				return j;
		return -1;
	}

	/*
	 * Implementation Note: This is a reproduction of the AbstractRope indexOf implementation. Calls to charAt have been replaced with direct array access to improve speed.
	 */
	public int indexOf(final CharSequence sequence, final int fromIndex) {
		// Implementation of Boyer-Moore-Horspool algorithm with
		// special support for unicode.

		// step 0. sanity check.
		final int length = sequence.length();
		if (length == 0)
			return -1;
		if (length == 1)
			return this.indexOf(sequence.charAt(0), fromIndex);

		final int[] bcs = new int[256]; // bad character shift
		Arrays.fill(bcs, length);

		// step 1. preprocessing.
		for (int j = 0; j < length - 1; ++j) {
			final char c = sequence.charAt(j);
			final int l = (c & 0xFF);
			bcs[l] = Math.min(length - j - 1, bcs[l]);
		}

		// step 2. search.
		for (int j = fromIndex + length - 1; j < this.length();) {
			int x = j, y = length - 1;
			while (true) {
				if (sequence.charAt(y) != this.sequence[x]) {
					j += bcs[(this.sequence[x] & 0xFF)];
					break;
				}
				if (y == 0)
					return x;
				--x;
				--y;
			}

		}

		return -1;
	}

	public Iterator<Character> iterator(final int start) {
		if (start < 0 || start > this.length())
			throw new IndexOutOfBoundsException("Rope index out of range: " + start);
		return new Iterator<Character>() {
			int current = start;

			public boolean hasNext() {
				return this.current < FlatCharArrayRope.this.length();
			}

			public Character next() {
				return FlatCharArrayRope.this.sequence[this.current++];
			}

			public void remove() {
				throw new UnsupportedOperationException("Rope iterator is read-only.");
			}
		};
	}

	public int length() {
		return this.sequence.length;
	}

	public Rope reverse() {
		return new ReverseRope(this);
	}

	public Iterator<Character> reverseIterator(final int start) {
		if (start < 0 || start > this.length())
			throw new IndexOutOfBoundsException("Rope index out of range: " + start);
		return new Iterator<Character>() {
			int current = FlatCharArrayRope.this.length() - start;

			public boolean hasNext() {
				return this.current > 0;
			}

			public Character next() {
				return FlatCharArrayRope.this.sequence[--this.current];
			}

			public void remove() {
				throw new UnsupportedOperationException("Rope iterator is read-only.");
			}
		};
	}

	public Rope subSequence(final int start, final int end) {
		if (start == 0 && end == this.length())
			return this;
		if (end - start < 16) {
			return new FlatCharArrayRope(this.sequence, start, end - start);
		} else {
			return new SubstringRope(this, start, end - start);
		}
	}

	public String toString() {
		return new String(this.sequence);
	}

	public String toString(final int offset, final int length) {
		return new String(this.sequence, offset, length);
	}

	public void write(final Writer out) throws IOException {
		this.write(out, 0, this.length());
	}

	public void write(final Writer out, final int offset, final int length) throws IOException {
		out.write(this.sequence, offset, length);
	}
}
