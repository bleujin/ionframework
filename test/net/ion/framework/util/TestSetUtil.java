package net.ion.framework.util;

import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

public class TestSetUtil extends TestCase {

	
	public void testCreate() throws Exception {
		String[] names = new String[]{"bleujin", "hero"} ;
		
		Set<String> set = SetUtil.create(names) ;
		
		assertEquals(2, set.size());
		
		Iterator<String> iter = set.iterator();
		assertEquals("hero", iter.next());
		assertEquals("bleujin", iter.next());
	}
}
