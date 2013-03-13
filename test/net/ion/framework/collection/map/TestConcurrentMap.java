package net.ion.framework.collection.map;

import net.ion.framework.util.Debug;
import junit.framework.TestCase;

public class TestConcurrentMap extends TestCase {

	public void testCreate() throws Exception {
		ConcurrentLinkedHashMap<String, String> cache = new ConcurrentLinkedHashMap.Builder<String, String>().maximumWeightedCapacity(5).build();
		
		for (int i = 0; i < 10; i++) {
			cache.put("" + i, "" + i) ;
		}
		
		Debug.line(cache.size()) ;
	}
}
