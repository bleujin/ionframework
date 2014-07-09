package net.ion.framework.parse.gson;

import java.util.List;
import java.util.Map;

import net.ion.framework.util.ListUtil;
import net.ion.framework.util.MapUtil;

import junit.framework.TestCase;

public class TestJsonUtil extends TestCase {

	public void testEncode() throws Exception {
		JsonObject jso =  JsonParser.fromString("{name:'bleujin', age:20, loc:{city:'seoul'}, color:['red', 'blue']}").getAsJsonObject() ;
		
		Map<String, Object> map = (Map<String, Object>) JsonUtil.toSimpleObject(jso) ;
		assertEquals("bleujin", map.get("name")) ;
		assertEquals(20L, map.get("age")) ;
		assertEquals("seoul", ((Map)map.get("loc")).get("city")) ;
		assertEquals("red", ((List)map.get("color")).get(0)) ;
	}
	
	public void testDecode() throws Exception {
		JsonObject jso = new JsonObject() ;
		jso.put("map", MapUtil.chainKeyMap().put("name", "bleujin").put("age", 20).toMap()) ;
		jso.put("list", ListUtil.toList("red", "blue")) ;
		jso.put("array", new String[]{"red", "blue"}) ;
		jso.put("string", "{name:1}") ;
		jso.put("int", 3) ;
		
		assertEquals(true, jso.get("map").isJsonObject()) ;
		assertEquals(true, jso.get("list").isJsonArray()) ;
		assertEquals(true, jso.get("array").isJsonArray()) ;
		assertEquals(true, jso.get("string").isJsonPrimitive()) ;
		assertEquals(true, jso.get("int").isJsonPrimitive()) ;

		assertEquals("bleujin", jso.asJsonObject("map").asString("name")) ;
		assertEquals(20, jso.asJsonObject("map").asInt("age")) ;
		assertEquals("red", jso.asJsonArray("list").asString(0)) ;
		assertEquals("red", jso.asJsonArray("array").asString(0)) ;
		assertEquals("{name:1}", jso.asString("string")) ;
		assertEquals(3, jso.asInt("int")) ;
	}
	
	public void testFindElement() throws Exception {
		JsonObject jso =  JsonParser.fromString("{name:'bleujin', age:20, loc:{city:'seoul'}, color:['red', 'blue']}").getAsJsonObject() ;

		assertEquals("bleujin", JsonUtil.findElement(jso, "name").getAsString()) ;
		assertEquals(20, JsonUtil.findElement(jso, "age").getAsInt()) ;
		
		assertEquals("seoul", JsonUtil.findElement(jso, "loc.city").getAsString()) ;
		assertEquals(true, JsonUtil.findElement(jso, "loc.city").isJsonPrimitive()) ;
		
		assertEquals(true, JsonUtil.findElement(jso, "Name") != null) ;
		assertEquals(true, JsonUtil.findElement(jso, "un") == null) ;
		assertEquals(true, JsonUtil.findElement(jso, "loc.un") == null) ;
		
		assertEquals(true, JsonUtil.findSimpleObject(jso, "loc.city").equals("seoul")) ;
		assertEquals(true, JsonUtil.findSimpleObject(jso, "loc.notfound") == null) ;
	}
	
	
	
	
	
	
	
}
