package net.ion.framework.rope;

/**
 * A rope that is directly backed by a data source.
 * 
 * @author Amin Ahmad
 */
interface FlatRope extends Rope {
	/**
	 * Returns a <code>String</code> representation of a range in this rope.
	 * 
	 * @param offset
	 *            the offset.
	 * @param length
	 *            the length.
	 * @return a <code>String</code> representation of a range in this rope.
	 */
	public String toString(int offset, int length);
}
