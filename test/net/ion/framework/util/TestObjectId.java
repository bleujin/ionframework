package net.ion.framework.util;

import junit.framework.TestCase;

public class TestObjectId extends TestCase {

	public void testTime() throws Exception {
		for (int i = 0; i < 1000 ; i++) {
			assertEquals(-1, new ObjectId().compareTo(new ObjectId())) ;
		}
	}
}
