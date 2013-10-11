package net.ion.framework.parse.gson;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class TestToJson extends TestCase{

	
	public void testSimple() throws Exception {
		JsonObject json = JsonObject.fromString("{name:'bleujin', age:20}");
		Simple simple = json.getAsObject(Simple.class);
		
		assertEquals("bleujin", simple.name) ;
	}
	
	
	public void testWrapper() throws Exception {
		JsonObject json = JsonObject.fromString("{name:'wrapper', simple:{name:'bleujin', age:20}}");
		Wrapper wrapper = json.getAsObject(Wrapper.class);
		
		assertEquals("wrapper", wrapper.name) ;
		assertEquals("bleujin", wrapper.simple.name) ;
	}
	
	public void testMapper() throws Exception {
		JsonObject json = JsonObject.fromString("{name:'mapper', map:{name:'bleujin', age:20}}");
		Mapper mapper = json.getAsObject(Mapper.class);
		
		assertEquals("mapper", mapper.name) ;
		assertEquals("bleujin", mapper.map.get("name")) ;
	}
	
	public void testMapperArray() throws Exception {
		JsonObject json = JsonObject.fromString("{name:'mapper', map:[{name:'bleujin', age:20}]}");
		MapArray mapper = json.getAsObject(MapArray.class);
		
		assertEquals("mapper", mapper.name) ;
		assertEquals("bleujin", mapper.map[0].get("name")) ;
	}
}

class Simple {
	String name ;
	int age ;
}

class Wrapper {
	Simple simple ;
	String name ;
}

class Mapper {
	Map map = new HashMap();
	String name ;
}

class MapArray {
	Map[] map = new Map[0];
	String name ;
}