package net.ion.framework.rope;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * A rope represents character strings. Ropes are immutable which means that once they are created, they cannot be changed. This makes them suitable for sharing in multi-threaded environments.
 * </p>
 * <p>
 * Rope operations, unlike string operations, scale well to very long character strings. Most mutation operations run in O(log n) time or better. However, random-access character retrieval is generally slower than for a String. By traversing
 * consecutive characters with an iterator instead, performance improves to O(1).
 * </p>
 * <p>
 * This rope implementation implements all performance optimizations outlined in "<a href="http://www.cs.ubc.ca/local/reading/proceedings/spe91-95/spe/vol25/issue12/spe986.pdf">Ropes: an Alternative to Strings</a>" by Hans-J. Boehm, Russ Atkinson and
 * Michael Plass, including, notably, deferred evaluation of long substrings and automatic rebalancing.
 * </p>
 * <h4>Immutability (a Caveat)</h4> A rope is immutable. Specifically, calling any mutator function on a rope always returns a modified copy; the original rope is left untouched. However, care must be taken to build ropes from immutable
 * <code>CharSequences</code> such as <code>Strings</code>, or else from mutable <code>CharSequences</code> that your program <emph>guarantees will not change</emph>. Failure to do so will result in logic errors.
 * 
 * @author bleujin
 */
/* @ pure @ */
public interface Rope extends CharSequence, Iterable<Character>, Comparable<CharSequence>, Serializable {

	/**
	 * A factory used for constructing ropes.
	 */
	Rope addTo(CharSequence... suffixs);

	// @ ensures \result.length() == length() + 1;
	Rope addTo(char c);

	// @ requires suffix != null;
	// @ ensures \result.length() == length() + suffix.length();
	Rope addTo(CharSequence suffix);

	// @ requires start <= end && start > -1 && end <= csq.length();
	// @ ensures \result.length() == (length() + (end-start));
	Rope addTo(CharSequence csq, int start, int end);

	// @ requires start <= end && start > -1 && end <= length();
	// @ ensures \result.length() == (length() - (end-start));
	Rope delete(int start, int end);

	// @ ensures \result >= -1 && \result < length();
	int indexOf(char ch);

	// @ requires fromIndex > -1 && fromIndex < length();
	// @ ensures \result >= -1 && \result < length();
	int indexOf(char ch, int fromIndex);

	// @ requires sequence != null;
	// @ ensures \result >= -1 && \result < length();
	int indexOf(CharSequence sequence);

	// @ requires sequence != null && fromIndex > -1 && fromIndex < length();
	// @ ensures \result >= -1 && \result < length();
	int indexOf(CharSequence sequence, int fromIndex);

	// @ requires dstOffset > -1 && dstOffset <= length();
	Rope insert(int dstOffset, CharSequence s);

	// @ requires start > -1 && start < length();
	Iterator<Character> iterator(int start);

	// @ ensures \result.length() <= length();
	Rope trimStart();

	// @ requires pattern != null;
	Matcher matcher(Pattern pattern);

	public boolean matches(Pattern regex);

	public boolean matches(String regex);

	public Rope rebalance();

	public Rope reverse();

	Iterator<Character> reverseIterator();

	Iterator<Character> reverseIterator(int start);

	// @ ensures \result.length() <= length();
	Rope trimEnd();

	Rope subSequence(int start, int end);

	Rope trim();

	public void write(Writer out) throws IOException;

	public void write(Writer out, int offset, int length) throws IOException;

	public Rope padStart(int toLength);

	public Rope padStart(int toLength, char padChar);

	public Rope padEnd(int toLength);

	public Rope padEnd(int toLength, char padChar);

	public boolean isEmpty();

	public boolean startsWith(CharSequence prefix);

	public boolean startsWith(CharSequence prefix, int offset);

	public boolean endsWith(CharSequence suffix);

	public boolean endsWith(CharSequence suffix, int offset);
}
