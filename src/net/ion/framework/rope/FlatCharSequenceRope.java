package net.ion.framework.rope;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A rope constructed from an underlying character sequence.
 * 
 * @author Amin Ahmad
 */
public final class FlatCharSequenceRope extends AbstractRope implements FlatRope {

	private final CharSequence sequence;

	/**
	 * Constructs a new rope from an underlying character sequence.
	 * 
	 * @param sequence
	 */
	public FlatCharSequenceRope(final CharSequence sequence) {
		this.sequence = sequence;
	}

	public char charAt(final int index) {
		return this.sequence.charAt(index);
	}

	public byte depth() {
		return 0;
	}

	public Iterator<Character> iterator(final int start) {
		if (start < 0 || start > this.length())
			throw new IndexOutOfBoundsException("Rope index out of range: " + start);
		return new Iterator<Character>() {
			int current = start;

			public boolean hasNext() {
				return this.current < FlatCharSequenceRope.this.length();
			}

			public Character next() {
				return FlatCharSequenceRope.this.sequence.charAt(this.current++);
			}

			public void remove() {
				throw new UnsupportedOperationException("Rope iterator is read-only.");
			}
		};
	}

	public int length() {
		return this.sequence.length();
	}

	public Matcher matcher(final Pattern pattern) {
		// optimized to return a matcher directly on the underlying sequence.
		return pattern.matcher(this.sequence);
	}

	public Rope reverse() {
		return new ReverseRope(this);
	}

	public Iterator<Character> reverseIterator(final int start) {
		if (start < 0 || start > this.length())
			throw new IndexOutOfBoundsException("Rope index out of range: " + start);
		return new Iterator<Character>() {
			int current = FlatCharSequenceRope.this.length() - start;

			public boolean hasNext() {
				return this.current > 0;
			}

			public Character next() {
				return FlatCharSequenceRope.this.sequence.charAt(--this.current);
			}

			public void remove() {
				throw new UnsupportedOperationException("Rope iterator is read-only.");
			}
		};
	}

	public Rope subSequence(final int start, final int end) {
		if (start == 0 && end == this.length())
			return this;
		if (end - start < 8 || this.sequence instanceof String /* special optimization for String */) {
			return new FlatCharSequenceRope(this.sequence.subSequence(start, end));
		} else {
			return new SubstringRope(this, start, end - start);
		}
	}

	public String toString() {
		return this.sequence.toString();
	}

	public String toString(final int offset, final int length) {
		return this.sequence.subSequence(offset, offset + length).toString();
	}

	public void write(final Writer out) throws IOException {
		this.write(out, 0, this.length());
	}

	public void write(final Writer out, final int offset, final int length) throws IOException {
		if (offset < 0 || offset + length > this.length())
			throw new IndexOutOfBoundsException("Rope index out of bounds:" + (offset < 0 ? offset : offset + length));

		if (this.sequence instanceof String) { // optimization for String
			out.write(((String) this.sequence).substring(offset, offset + length));
			return;
		}
		for (int j = offset; j < offset + length; ++j)
			out.write(this.sequence.charAt(j));
	}
}
