package net.ion.framework.parse.gson;

import java.io.StringReader;
import java.util.List;

import net.ion.framework.parse.gson.stream.JsonReader;
import net.ion.framework.util.Debug;
import junit.framework.TestCase;

public class TestJsonReader extends TestCase {

	public void testRead() throws Exception {
		JsonObject json = new JsonObject().put("name", "bleujin").put("address", new JsonObject().put("city", "seoul")).put("age", 1L);

		JsonReader reader = new JsonReader(new StringReader(json.toString()));

		reader.beginObject();

		while (reader.hasNext()) {
			String name = reader.nextName();

			if ("name".equals(name)) {
				assertEquals("bleujin", reader.nextString());
			} else if ("address".equals(name)) {
//				reader.beginObject();
//				reader.endObject();
				reader.skipValue() ;
			} else {
				reader.skipValue();
			}
		}

		reader.endObject();
		reader.close();
	}
	
	
	public void testNextJsonObject() throws Exception {
		
		JsonObject json = new JsonObject()
			.put("name", "bleujin")
			.put("val", new JsonObject().put("props", 
							new JsonObject().put("name", new JsonArray().adds("hero"))
											.put("age", new JsonArray().adds(20)))).put("age", 1L);
		
		JsonReader jreader = new JsonReader(new StringReader(json.toString()));
		jreader.beginObject() ;
		while(jreader.hasNext()){
			String nextName = jreader.nextName() ;
			if ("name".equals(nextName)){
				jreader.nextString() ;
			} else if ("age".equals(nextName)) {
				jreader.nextInt() ;
			} else if ("val".equals(nextName)){
				JsonObject find = jreader.nextJsonObject() ;
				Debug.line(find) ;
			}
		}
		jreader.endObject() ;
		
		
//		Debug.line(jreader.nextJsonObject()) ; 
	}
	
	public void testNextJsonArray() throws Exception {
		JsonArray json = new JsonArray().add(new JsonObject().put("city", "seoul")).add(new JsonObject().put("name", "bleujin"));
		JsonReader jreader = new JsonReader(new StringReader(json.toString()));
		
		JsonArray result = JsonUtil.nextJsonArray(jreader);
		Debug.line(result) ;
		jreader.close() ;
	}
	
	public void testNextJsonElement() throws Exception {
		JsonObject json = new JsonObject().put("name", "bleujin").put("address", new JsonObject().put("city", "seoul")).put("age", 1L);
		JsonReader jreader = new JsonReader(new StringReader(json.toString()));
		
	}
	
	
	public void testNumeric() throws Exception {
		JsonObject json = new JsonObject().put("int", 1).put("long", 1L).put("float", 1.1f).put("double", 1.1d);
		JsonReader reader = new JsonReader(new StringReader(json.toString()));
		
		reader.beginObject() ;
		while(reader.hasNext()){
			Debug.line(reader.nextName(), reader.nextLong()) ;
		}
		reader.endObject() ;
		reader.close() ;
	}
	
}
