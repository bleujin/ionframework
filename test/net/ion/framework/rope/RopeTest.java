/*
 *  RopeTest.java
 *  Copyright (C) 2007 Amin Ahmad. 
 *  
 *  This file is part of Java Ropes.
 *  
 *  Java Ropes is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  Java Ropes is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with Java Ropes.  If not, see <http://www.gnu.org/licenses/>.
 *  	
 *  Amin Ahmad can be contacted at amin.ahmad@gmail.com or on the web at 
 *  www.ahmadsoft.org.
 */
package net.ion.framework.rope;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.regex.Pattern;

import junit.framework.Assert;
import junit.framework.TestCase;

public class RopeTest extends TestCase {

	private String fromRope(Rope rope, int start, int end) {
		try {
			Writer out = new StringWriter(end - start);
			rope.write(out, start, end - start);
			return out.toString();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void testSubstringDeleteBug() {
		String s = "12345678902234567890";

		Rope rope = RopeBuilder.build(s.toCharArray()); // bugs

		rope = rope.delete(0, 1);
		assertEquals("23", fromRope(rope, 0, 2));
		assertEquals("", fromRope(rope, 0, 0));
		assertEquals("902", fromRope(rope, 7, 10));

		rope = RopeBuilder.build(s); // no bugs
		rope = rope.delete(0, 1);
		assertEquals("23", fromRope(rope, 0, 2));
		assertEquals("", fromRope(rope, 0, 0));
		assertEquals("902", fromRope(rope, 7, 10));
	}

	/**
	 * Bug reported by ugg.ugg@gmail.com.
	 */
	public void testRopeWriteBug() {
		Rope r = RopeBuilder.build("");
		r = r.addTo("round ");
		r = r.addTo(Integer.toString(0));
		r = r.addTo(" 1234567890");

		assertEquals("round ", fromRope(r, 0, 6));
		assertEquals("round 0", fromRope(r, 0, 7));
		assertEquals("round 0 ", fromRope(r, 0, 8));
		assertEquals("round 0 1", fromRope(r, 0, 9));
		assertEquals("round 0 12", fromRope(r, 0, 10));
		assertEquals("round 0 1234567890", fromRope(r, 0, 18));
		assertEquals("round 0 1234567890", fromRope(r, 0, r.length()));
	}

	public void testTemp() {
		// insert temporary code here.
	}

	public void testLengthOverflow() {
		Rope x1 = RopeBuilder.build("01");
		for (int j = 2; j < 31; ++j)
			x1 = x1.addTo(x1);
		assertEquals(1073741824, x1.length());
		try {
			x1 = x1.addTo(x1);
			fail("Expected overflow.");
		} catch (IllegalArgumentException e) {
			// this is what we expect
		}
	}

	public void testMatches() {
		Rope x1 = new FlatCharSequenceRope("0123456789");
		Rope x2 = new ConcatenationRope(x1, x1);

		assertTrue(x2.matches("0.*9"));
		assertTrue(x2.matches(Pattern.compile("0.*9")));

		assertTrue(x2.matches("0.*90.*9"));
		assertTrue(x2.matches(Pattern.compile("0.*90.*9")));
	}

	public void testConcatenationFlatFlat() {
		Rope r1 = RopeBuilder.build("alpha");
		final Rope r2 = RopeBuilder.build("beta");
		Rope r3 = r1.addTo(r2);
		Assert.assertEquals("alphabeta", r3.toString());

		r1 = RopeBuilder.build("The quick brown fox jumped over");
		r3 = r1.addTo(r1);
		Assert.assertEquals("The quick brown fox jumped overThe quick brown fox jumped over", r3.toString());
	}

	public void testIterator() {
		Rope x1 = new FlatCharSequenceRope("0123456789");
		Rope x2 = new FlatCharSequenceRope("0123456789");
		Rope x3 = new FlatCharSequenceRope("0123456789");
		ConcatenationRope c1 = new ConcatenationRope(x1, x2);
		ConcatenationRope c2 = new ConcatenationRope(c1, x3);

		Iterator<Character> i = c2.iterator();
		for (int j = 0; j < c2.length(); ++j) {
			assertTrue("Has next (" + j + "/" + c2.length() + ")", i.hasNext());
			i.next();
		}
		assertTrue(!i.hasNext());

		FlatCharSequenceRope z1 = new FlatCharSequenceRope("0123456789");
		Rope z2 = new SubstringRope(z1, 2, 0);
		Rope z3 = new SubstringRope(z1, 2, 2);
		Rope z4 = new ConcatenationRope(z3, new SubstringRope(z1, 6, 2)); // 2367

		i = z2.iterator();
		assertTrue(!i.hasNext());
		i = z3.iterator();
		assertTrue(i.hasNext());
		assertEquals((char) '2', (char) i.next());
		assertTrue(i.hasNext());
		assertEquals((char) '3', (char) i.next());
		assertTrue(!i.hasNext());
		for (int j = 0; j <= z3.length(); ++j) {
			try {
				z3.iterator(j);
			} catch (Exception e) {
				fail(j + " " + e.toString());
			}
		}
		assertTrue(4 == z4.length());
		for (int j = 0; j <= z4.length(); ++j) {
			try {
				z4.iterator(j);
			} catch (Exception e) {
				fail(j + " " + e.toString());
			}
		}
		i = z4.iterator(4);
		assertTrue(!i.hasNext());
		i = z4.iterator(2);
		assertTrue(i.hasNext());
		assertEquals((char) '6', (char) i.next());
		assertTrue(i.hasNext());
		assertEquals((char) '7', (char) i.next());
		assertTrue(!i.hasNext());

	}

	public void testReverse() {
		Rope x1 = new FlatCharSequenceRope("012345");
		Rope x2 = new FlatCharSequenceRope("67");
		Rope x3 = new ConcatenationRope(x1, x2);

		assertEquals("543210", x1.reverse().toString());
		assertEquals("76543210", x3.reverse().toString());
		assertEquals(x3.reverse(), x3.reverse().reverse().reverse());
		assertEquals("654321", x3.reverse().subSequence(1, 7).toString());
	}

	public void testTrim() {
		Rope x1 = new FlatCharSequenceRope("\u0012  012345");
		Rope x2 = new FlatCharSequenceRope("\u0002 67	       \u0007");
		Rope x3 = new ConcatenationRope(x1, x2);

		assertEquals("012345", x1.trimStart().toString());
		assertEquals("67	       \u0007", x2.trimStart().toString());
		assertEquals("012345\u0002 67	       \u0007", x3.trimStart().toString());

		assertEquals("\u0012  012345", x1.trimEnd().toString());
		assertEquals("\u0002 67", x2.trimEnd().toString());
		assertEquals("\u0012  012345\u0002 67", x3.trimEnd().toString());
		assertEquals("012345\u0002 67", x3.trimEnd().reverse().trimEnd().reverse().toString());

		assertEquals(x3.trimStart().trimEnd(), x3.trimEnd().trimStart());
		assertEquals(x3.trimStart().trimEnd(), x3.trimStart().reverse().trimStart().reverse());
		assertEquals(x3.trimStart().trimEnd(), x3.trim());
	}

	public void testCreation() {
		try {
			RopeBuilder.build("The quick brown fox jumped over");
		} catch (final Exception e) {
			Assert.fail("Nonempty string: " + e.getMessage());
		}
		try {
			RopeBuilder.build("");
		} catch (final Exception e) {
			Assert.fail("Empty string: " + e.getMessage());
		}
	}

	public void testEquals() {
		final Rope r1 = RopeBuilder.build("alpha");
		final Rope r2 = RopeBuilder.build("beta");
		final Rope r3 = RopeBuilder.build("alpha");

		Assert.assertEquals(r1, r3);
		Assert.assertFalse(r1.equals(r2));
	}

	public void testHashCode() {
		final Rope r1 = RopeBuilder.build("alpha");
		final Rope r2 = RopeBuilder.build("beta");
		final Rope r3 = RopeBuilder.build("alpha");

		Assert.assertEquals(r1.hashCode(), r3.hashCode());
		Assert.assertFalse(r1.hashCode() == r2.hashCode());
	}

	public void testHashCode2() {
		Rope r1 = new FlatCharSequenceRope(new StringBuffer("The quick brown fox."));
		Rope r2 = new ConcatenationRope(new FlatCharSequenceRope(""), new FlatCharSequenceRope("The quick brown fox."));

		assertTrue(r1.equals(r2));
		assertTrue(r1.equals(r2));
	}

	public void testIndexOf() {
		final Rope r1 = RopeBuilder.build("alpha");
		final Rope r2 = RopeBuilder.build("beta");
		final Rope r3 = r1.addTo(r2);
		Assert.assertEquals(1, r3.indexOf('l'));
		Assert.assertEquals(6, r3.indexOf('e'));

		Rope r = RopeBuilder.build("abcdef");
		assertEquals(-1, r.indexOf('z'));
		assertEquals(0, r.indexOf('a'));
		assertEquals(1, r.indexOf('b'));
		assertEquals(5, r.indexOf('f'));

		assertEquals(1, r.indexOf('b', 0));
		assertEquals(0, r.indexOf('a', 0));
		assertEquals(-1, r.indexOf('z', 0));
		assertEquals(-1, r.indexOf('b', 2));
		assertEquals(5, r.indexOf('f', 5));

		assertEquals(2, r.indexOf("cd", 1));

		r = RopeBuilder.build("The quick brown fox jumped over the jumpy brown dog.");
		assertEquals(0, r.indexOf("The"));
		assertEquals(10, r.indexOf("brown"));
		assertEquals(10, r.indexOf("brown", 10));
		assertEquals(42, r.indexOf("brown", 11));
		assertEquals(-1, r.indexOf("brown", 43));
		assertEquals(-1, r.indexOf("hhe"));

		r = RopeBuilder.build("zbbzzz");
		assertEquals(-1, r.indexOf("ab", 1));
	}

	public void testInsert() {
		final Rope r1 = RopeBuilder.build("alpha");
		Assert.assertEquals("betaalpha", r1.insert(0, "beta").toString());
		Assert.assertEquals("alphabeta", r1.insert(r1.length(), "beta").toString());
		Assert.assertEquals("abetalpha", r1.insert(1, "beta").toString());
	}

	public void testPrepend() {
		Rope r1 = RopeBuilder.build("alphabeta");
		for (int j = 0; j < 2; ++j)
			r1 = r1.subSequence(0, 5).addTo(r1);
		Assert.assertEquals("alphaalphaalphabeta", r1.toString());
		r1 = r1.addTo(r1.subSequence(5, 15));
		Assert.assertEquals("alphaalphaalphabetaalphaalpha", r1.toString());
	}

	public void testCompareTo() {
		final Rope r1 = RopeBuilder.build("alpha");
		final Rope r2 = RopeBuilder.build("beta");
		final Rope r3 = RopeBuilder.build("alpha");
		final Rope r4 = RopeBuilder.build("alpha1");
		final String s2 = "beta";

		assertTrue(r1.compareTo(r3) == 0);
		assertTrue(r1.compareTo(r2) < 0);
		assertTrue(r2.compareTo(r1) > 0);
		assertTrue(r1.compareTo(r4) < 0);
		assertTrue(r4.compareTo(r1) > 0);
		assertTrue(r1.compareTo(s2) < 0);
		assertTrue(r2.compareTo(s2) == 0);
	}

	public void testToString() {
		String phrase = "The quick brown fox jumped over the lazy brown dog. Boy am I glad the dog was asleep.";
		final Rope r1 = RopeBuilder.build(phrase);
		assertTrue(phrase.equals(r1.toString()));
		assertTrue(phrase.subSequence(7, 27).equals(r1.subSequence(7, 27).toString()));
	}

	public void testReverseIterator() {
		FlatCharSequenceRope r1 = new FlatCharSequenceRope("01234");
		ReverseRope r2 = new ReverseRope(r1);
		SubstringRope r3 = new SubstringRope(r1, 0, 3);
		ConcatenationRope r4 = new ConcatenationRope(new ConcatenationRope(r1, r2), r3); // 0123443210012

		Iterator<Character> x = r1.reverseIterator();
		assertTrue(x.hasNext());
		assertEquals((char) '4', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '3', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '2', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '1', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '0', (char) x.next());
		assertFalse(x.hasNext());

		x = r1.reverseIterator(4);
		assertTrue(x.hasNext());
		assertEquals((char) '0', (char) x.next());
		assertFalse(x.hasNext());

		x = r2.reverseIterator();
		assertTrue(x.hasNext());
		assertEquals((char) '0', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '1', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '2', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '3', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '4', (char) x.next());
		assertFalse(x.hasNext());

		x = r2.reverseIterator(4);
		assertTrue(x.hasNext());
		assertEquals((char) '4', (char) x.next());
		assertFalse(x.hasNext());

		x = r3.reverseIterator();
		assertTrue(x.hasNext());
		assertEquals((char) '2', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '1', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '0', (char) x.next());
		assertFalse(x.hasNext());

		x = r3.reverseIterator(1);
		assertTrue(x.hasNext());
		assertEquals((char) '1', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '0', (char) x.next());
		assertFalse(x.hasNext());

		x = r4.reverseIterator(); // 0123443210012
		assertTrue(x.hasNext());
		assertEquals((char) '2', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '1', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '0', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '0', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '1', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '2', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '3', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '4', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '4', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '3', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '2', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '1', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '0', (char) x.next());
		assertFalse(x.hasNext());

		x = r4.reverseIterator(7);
		assertEquals((char) '4', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '4', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '3', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '2', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '1', (char) x.next());
		assertTrue(x.hasNext());
		assertEquals((char) '0', (char) x.next());
		assertFalse(x.hasNext());

		x = r4.reverseIterator(12);
		assertTrue(x.hasNext());
		assertEquals((char) '0', (char) x.next());
		assertFalse(x.hasNext());

		x = r4.reverseIterator(13);
		assertFalse(x.hasNext());

	}

	public void testSerialize() {
		FlatCharSequenceRope r1 = new FlatCharSequenceRope("01234");
		ReverseRope r2 = new ReverseRope(r1);
		SubstringRope r3 = new SubstringRope(r1, 0, 1);
		ConcatenationRope r4 = new ConcatenationRope(new ConcatenationRope(r1, r2), r3); // 01234432100

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(r4);
			oos.close();
			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(in);
			Rope r = (Rope) ois.readObject();
			assertTrue(r instanceof FlatCharSequenceRope);
		} catch (Exception e) {
			fail(e.toString());
		}

	}

	public void testPadStart() {
		Rope r = RopeBuilder.build("hello");
		assertEquals("hello", r.padStart(5).toString());
		assertEquals("hello", r.padStart(0).toString());
		assertEquals("hello", r.padStart(-1).toString());
		assertEquals(" hello", r.padStart(6).toString());
		assertEquals("  hello", r.padStart(7).toString());
		assertEquals("~hello", r.padStart(6, '~').toString());
		assertEquals("~~hello", r.padStart(7, '~').toString());
		assertEquals("~~~~~~~~~~~~~~~~~~~~~~~~~hello", r.padStart(30, '~').toString());
	}

	public void testPadEnd() {
		Rope r = RopeBuilder.build("hello");
		assertEquals("hello", r.padEnd(5).toString());
		assertEquals("hello", r.padEnd(0).toString());
		assertEquals("hello", r.padEnd(-1).toString());
		assertEquals("hello ", r.padEnd(6).toString());
		assertEquals("hello  ", r.padEnd(7).toString());
		assertEquals("hello~", r.padEnd(6, '~').toString());
		assertEquals("hello~~", r.padEnd(7, '~').toString());
		assertEquals("hello~~~~~~~~~~~~~~~~~~~~~~~~~", r.padEnd(30, '~').toString());
	}

	public void testSubstringBounds() {
		Rope r = RopeBuilder.build("01234567890123456789012345678901234567890123456789012345678901234567890123456789".toCharArray());
		Rope r2 = r.subSequence(0, 30);
		try {
			r2.charAt(31);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			// success
		}
	}

	public void testAppend() {
		Rope r = RopeBuilder.build("");
		r = r.addTo('a');
		assertEquals("a", r.toString());
		r = r.addTo("boy");
		assertEquals("aboy", r.toString());
		r = r.addTo("test", 0, 4);
		assertEquals("aboytest", r.toString());
	}

	public void testEmpty() {
		Rope r1 = RopeBuilder.build("");
		Rope r2 = RopeBuilder.build("012345");

		assertTrue(r1.isEmpty());
		assertFalse(r2.isEmpty());
		assertTrue(r2.subSequence(2, 2).isEmpty());
	}

	public void testCharAt() {
		FlatCharSequenceRope r1 = new FlatCharSequenceRope("0123456789");
		SubstringRope r2 = new SubstringRope(r1, 0, 1);
		SubstringRope r3 = new SubstringRope(r1, 9, 1);
		ConcatenationRope r4 = new ConcatenationRope(r1, r3);

		assertEquals('0', r1.charAt(0));
		assertEquals('9', r1.charAt(9));
		assertEquals('0', r2.charAt(0));
		assertEquals('9', r3.charAt(0));
		assertEquals('0', r4.charAt(0));
		assertEquals('9', r4.charAt(9));
		assertEquals('9', r4.charAt(10));
	}

	public void testRegexp() {
		ConcatenationRope r = new ConcatenationRope(new FlatCharSequenceRope("012345"), new FlatCharSequenceRope("6789"));
		CharSequence c = r.getForSequentialAccess();
		for (int j = 0; j < 10; ++j) {
			assertEquals(r.charAt(j), c.charAt(j));
		}
		c = r.getForSequentialAccess();

		int[] indices = { 1, 2, 1, 3, 5, 0, 6, 7, 8, 1, 7, 7, 7 };
		for (int i : indices) {
			assertEquals("Index: " + i, r.charAt(i), c.charAt(i));
		}
	}

	public void testStartsEndsWith() {
		final Rope r = RopeBuilder.build("Hello sir, how do you do?");
		assertTrue(r.startsWith(""));
		assertTrue(r.startsWith("H"));
		assertTrue(r.startsWith("He"));
		assertTrue(r.startsWith("Hello "));
		assertTrue(r.startsWith("", 0));
		assertTrue(r.startsWith("H", 0));
		assertTrue(r.startsWith("He", 0));
		assertTrue(r.startsWith("Hello ", 0));
		assertTrue(r.startsWith("", 1));
		assertTrue(r.startsWith("e", 1));
		assertTrue(r.endsWith("?"));
		assertTrue(r.endsWith("do?"));
		assertTrue(r.endsWith("o", 1));
		assertTrue(r.endsWith("you do", 1));
	}

	/**
	 * Reported by Blake Watkins <blakewatkins@gmail.com> on 21 Mar 2009.
	 */
	public void testIndexOfBug() {
		{ // original test, bwatkins
			String s1 = "CCCCCCPIFPCFFP";
			String s2 = "IFPCFFP";

			Rope r1 = RopeBuilder.build(s1);
			Assert.assertEquals(s1.indexOf(s2), r1.indexOf(s2));
		}
		{ // extra test, aahmad
			String s1 = "ABABAABBABABBAAABBBAAABABABABBBBAA";
			String s2 = "ABABAB";

			Rope r1 = RopeBuilder.build(s1);
			Assert.assertEquals(s1.indexOf(s2), r1.indexOf(s2));
		}
	}
}
