package net.ion.framework.parse.gson;

import java.util.Date;
import java.util.List;

import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.MapUtil;
import junit.framework.TestCase;

public class TestJsonObject extends TestCase{

	public void testDefaultValue() throws Exception {
		String str = "{key:3}" ;
		JsonObject json = JsonParser.fromString(str).getAsJsonObject() ;
		
		assertEquals(3, json.asInt("key", -1)) ;
		assertEquals(-1, json.asInt("un", -1)) ;
	}

	
	public void testFromObject() throws Exception {
		Debug.line(JsonParser.fromObject(new MyObj())) ;
	}
	
	
	public void testFromString() throws Exception {
		Debug.line(JsonParser.fromString("hello")) ;
		assertEquals(true, JsonParser.fromString("{greet:'hello'}").isJsonObject()) ;
		assertEquals(true, JsonParser.fromObject("{greet:'hello'}").isJsonPrimitive()) ;
	}
	
	public void testFromList() throws Exception {
		List list = ListUtil.toList(new MyBean()) ;
		assertEquals("bleujin", JsonParser.fromObject(list).getAsJsonArray().get(0).getAsJsonObject().asString("name")) ;
		assertEquals("city", JsonParser.fromObject(new MyObj()).getAsJsonObject().asString("address") ) ;
	}

	public void testFromMap() throws Exception {
		List list = ListUtil.toList(MapUtil.create("bean", new MyBean())) ;
		
		Debug.line(JsonParser.fromObject(list).toString()) ;
	}
	

	public void testDate() throws Exception {

		JsonObject json = JsonParser.fromMap(MapUtil.create("d", new Date())).getAsJsonObject();
		Date date = json.get("d").getAsJsonPrimitive().getAsDate() ;
		
		assertEquals(true,  Math.abs(new Date().getTime() - date.getTime()) < 1000 ) ;
	}


	private java.sql.Date newSQLDate() {
		return new java.sql.Date(new Date().getTime());
	}
	
	
	
	
}

class MyObj {
	private String address = "city" ;
	
	public String toJsonString() {
		return "{name:bleujin}";
	}
}


class MyBean implements JsonString{
	private String address = "city" ;
	
	public String toJsonString() {
		return "{name:bleujin}";
	}
}