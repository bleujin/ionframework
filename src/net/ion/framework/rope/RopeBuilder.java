package net.ion.framework.rope;

import java.io.IOException;
import java.io.Reader;

import org.apache.commons.io.IOUtils;

/**
 * A factory for building ropes.
 * 
 * @author Amin Ahmad
 */
public final class RopeBuilder {

	/**
	 * Construct a rope from a character array.
	 * 
	 * @param sequence
	 *            a character array
	 * @return a rope representing the underlying character array.
	 */
	public static Rope build(final char[] sequence) {
		return new FlatCharArrayRope(sequence);
	}

	public static Rope build(Reader reader) throws IOException {
		if (reader == null)
			return null;
		if (reader instanceof RopeReader) {
			return ((RopeReader) reader).getRope();
		}
		RopeWriter rw = new RopeWriter();
		IOUtils.copy(reader, rw);

		return rw.getRope();
	}

	/**
	 * Construct a rope from an underlying character sequence.
	 * 
	 * @param sequence
	 *            the underlying character sequence.
	 * @return a rope representing the underlying character sequnce.
	 */
	public static Rope build(final CharSequence sequence) {
		if (sequence == null)
			return build();
		if (sequence instanceof Rope)
			return (Rope) sequence;
		return new FlatCharSequenceRope(sequence);
	}

	public static Rope build() {
		return new FlatCharArrayRope(new char[0]);
	}
}
