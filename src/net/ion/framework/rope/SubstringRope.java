package net.ion.framework.rope;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

/**
 * Represents a lazily-evaluated substring of another rope. For performance reasons, the target rope must be a <code>FlatRope</code>.
 * 
 * @author aahmad
 */
public class SubstringRope extends AbstractRope {

	private final FlatRope rope;
	private final int offset;
	private final int length;

	public SubstringRope(final FlatRope rope, final int offset, final int length) {
		if (length < 0 || offset < 0 || offset + length > rope.length())
			throw new IndexOutOfBoundsException("Invalid substring offset (" + offset + ") and length (" + length + ") for underlying rope with length "
					+ rope.length());

		this.rope = rope;
		this.offset = offset;
		this.length = length;
	}

	public char charAt(final int index) {
		if (index >= this.length())
			throw new IndexOutOfBoundsException("Rope index out of range: " + index);

		return this.rope.charAt(this.offset + index);
	}

	public byte depth() {
		return RopeUtilities.INSTANCE.depth(getRope());
	}

	int getOffset() {
		return this.offset;
	}

	/**
	 * Returns the rope underlying this one.
	 * 
	 * @return the rope underlying this one.
	 */
	public Rope getRope() {
		return this.rope;
	}

	public Iterator<Character> iterator(final int start) {
		if (start < 0 || start > this.length())
			throw new IndexOutOfBoundsException("Rope index out of range: " + start);
		return new Iterator<Character>() {

			final Iterator<Character> u = SubstringRope.this.getRope().iterator(SubstringRope.this.getOffset() + start);
			int position = start;

			public boolean hasNext() {
				return this.position < SubstringRope.this.length();
			}

			public Character next() {
				++this.position;
				return this.u.next();
			}

			public void remove() {
				this.u.remove();
			}

		};
	}

	public int length() {
		return this.length;
	}

	public Rope reverse() {
		return new ReverseRope(this);
	}

	public Iterator<Character> reverseIterator(final int start) {
		if (start < 0 || start > this.length())
			throw new IndexOutOfBoundsException("Rope index out of range: " + start);
		return new Iterator<Character>() {
			final Iterator<Character> u = SubstringRope.this.getRope().reverseIterator(
					SubstringRope.this.getRope().length() - SubstringRope.this.getOffset() - SubstringRope.this.length() + start);
			int position = SubstringRope.this.length() - start;

			public boolean hasNext() {
				return this.position > 0;
			}

			public Character next() {
				--this.position;
				return this.u.next();
			}

			public void remove() {
				this.u.remove();
			}
		};
	}

	public Rope subSequence(final int start, final int end) {
		if (start == 0 && end == this.length())
			return this;
		return new SubstringRope(this.rope, this.offset + start, end - start);
	}

	public String toString() {
		return this.rope.toString(this.offset, this.length);
	}

	public void write(final Writer out) throws IOException {
		this.rope.write(out, this.offset, this.length);
	}

	public void write(final Writer out, final int offset, final int length) throws IOException {
		this.rope.write(out, this.offset + offset, length);
	}
}
