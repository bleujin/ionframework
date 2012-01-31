package net.ion.framework.parse.gson;

import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import net.ion.framework.parse.gson.reflect.TypeToken;
import net.ion.framework.parse.gson.stream.JsonWriter;
import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.MapUtil;

import junit.framework.TestCase;

public class TestSimpleJson extends TestCase{
	
	public void testParseSimple() throws Exception {
		JsonObject jso =  JsonParser.fromString("{name:'bleujin', age:20, loc:{city:'seoul'}, color:['red', 'blue']}").getAsJsonObject() ;
		
		assertEquals("bleujin", jso.asString("name")) ; 
		assertEquals("20", jso.get("age").getAsString()) ;
		assertEquals(20, jso.get("age").getAsInt()) ;
		assertEquals("red", jso.get("color").getAsJsonArray().iterator().next().getAsString()) ;
	}
	
	public void testParseAsArray() throws Exception {
		JsonObject jo = JsonParser.fromString("{name:'bleujin', age:20, loc:{city:'seoul'}, color:['red', 'blue']}").getAsJsonObject() ;
		JsonArray ja = JsonParser.fromString("['red', 'blue']").getAsJsonArray() ;
	}
	
	public void testToJson() throws Exception {
		Map map = MapUtil.newMap() ;
		map.put("name", "bleujin") ;
		map.put("age", "20") ;
		map.put("loc", MapUtil.create("city", "seoul")) ;
		map.put("color", ListUtil.toList("red", "blue")) ;
		
		JsonObject jso = JsonParser.fromMap(map) ;
		assertEquals("bleujin", jso.asString("name")) ;
		assertEquals(20, jso.asInt("age")) ;
		assertEquals("seoul", jso.asJsonObject("loc").asString("city")) ;
		assertEquals("red", jso.asJsonArray("color").asString(0)) ;
	}
	
	public void testWrite() throws Exception {
		JsonObject ja = new JsonParser().parse("{name:'bleujin', age:20, loc:{city:'seoul'}, color:['red', 'blue']}").getAsJsonObject() ;
		
		assertEquals("{\"name\":\"bleujin\",\"age\":20,\"loc\":{\"city\":\"seoul\"},\"color\":[\"red\",\"blue\"]}", ja.toString()) ;
	}
	
	
	public void testArray() throws Exception {
		List list = ListUtil.newList() ;
		list.add(MapUtil.chainKeyMap().put("name", "bleujin").put("age", 20).toMap()) ;
		list.add(MapUtil.chainKeyMap().put("name", "hero").put("age", 25).toMap()) ;
		list.add(MapUtil.chainKeyMap().put("name", "novi").put("age", 30).toMap()) ;
		
		JsonArray ja = JsonParser.fromList(list) ;
		assertEquals("bleujin", ja.asJsonObject(0).asString("name")) ;
		assertEquals(20, ja.asJsonObject(0).asInt("age")) ;
		assertEquals("hero", ja.asJsonObject(1).asString("name")) ;
		assertEquals("novi", ja.asJsonObject(2).asString("name")) ;
	}
	
}
