/*
 *  AllTests.java
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

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import net.ion.framework.util.Debug;

public class RopeAllTest {

	public static void main(String[] args) {
		suite().run(new TestResult());
	}

	public static Test suite() {
		System.setProperty(Debug.PROPERTY_KEY, "off");
		TestSuite ts = new TestSuite("Rope Test ALL");

		ts.addTestSuite(RopeCharSequenceTest.class);

		ts.addTestSuite(RopeTest.class);
		//		
		ts.addTestSuite(TestRopeInputStream.class);
		ts.addTestSuite(TestRopeReader.class);
		ts.addTestSuite(TestRopeWriter.class);

		return ts;
	}

}
