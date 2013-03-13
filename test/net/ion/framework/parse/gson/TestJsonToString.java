package net.ion.framework.parse.gson;

import java.util.Map;

import net.ion.framework.util.Debug;
import net.ion.framework.util.MapUtil;
import junit.framework.TestCase;

public class TestJsonToString extends TestCase {

	
	public void testToString() throws Exception {
		Map<String, String> map = MapUtil.<String, String>chainMap().put("name", "bleujin").toMap() ;
		Debug.line(JsonObject.fromObject(map).toString()) ;
		
		
		Debug.line(JsonObject.create().put("name", "bleujin").toString()) ;
	}
}
