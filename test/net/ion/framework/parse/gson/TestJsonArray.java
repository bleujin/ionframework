package net.ion.framework.parse.gson;

import java.util.Date;
import java.util.Set;

import junit.framework.TestCase;
import net.ion.framework.util.SetUtil;

public class TestJsonArray extends TestCase {

	
	public void testUseJsonParse() throws Exception {
		Set<Date> set = SetUtil.newSet() ;
		set.add(new Date()) ;
		
		for (int i = 0; i < 2000000 ; i++) {
			JsonArray jarray = JsonParser.fromObject(set).getAsJsonArray();
		}
	}

	public void testUseJsonArray() throws Exception {
		Set<Date> set = SetUtil.newSet() ;
		set.add(new Date()) ;
		
		for (int i = 0; i < 2000000 ; i++) {
			JsonArray jarray = new JsonArray() ;
			for (Object str : set) {
				jarray.add(JsonPrimitive.create(str)) ;
			}
		}
	}

}
