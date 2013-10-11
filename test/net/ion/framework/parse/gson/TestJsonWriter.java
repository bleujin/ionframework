package net.ion.framework.parse.gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map.Entry;

import net.ion.framework.parse.gson.stream.JsonReader;
import net.ion.framework.parse.gson.stream.JsonWriter;
import net.ion.framework.util.Debug;
import net.ion.framework.util.IOUtil;
import net.ion.framework.util.ReaderInputStream;
import net.ion.framework.util.StringBufferReader;
import net.ion.framework.util.StringBuilderWriter;
import junit.framework.TestCase;

public class TestJsonWriter extends TestCase {

	
	public void testWrite() throws Exception {
		final StringWriter sw = new StringWriter();
		
		JsonWriter jwriter = new JsonWriter(sw);
		
		
		jwriter.beginObject() ;
		
		jwriter.name("nodes") ;
		jwriter.beginArray() ;
		for (int i = 0; i < 10; i++) {
			
			jwriter.beginObject() ;
			jwriter.name("name").value("bleujin").name("index").value(i) ; 
			jwriter.endObject() ;
		}
		jwriter.endArray() ;
		
		jwriter.endObject() ;
		
		jwriter.close();
		
		
		
		
		Debug.line(sw.getBuffer()) ;
		InputStream input = new ReaderInputStream(new StringBufferReader(sw.getBuffer()), "UTF-8");
		
	}
	
	
	
	public void testInputStream() throws Exception {
		
		Reader reader = new StringReader("{\"name\":\"{name:'dd'}\", \"age\":20}");
		JsonReader jreader = new JsonReader(reader);
		
		jreader.beginObject() ;
		
		
		Debug.line(jreader.nextName(), jreader.nextString(), jreader.nextName(), jreader.nextInt()) ;
		jreader.endObject() ;
		
	}

	public void testJsonWriter() throws Exception {
		final StringBuilderWriter swriter = new StringBuilderWriter();
		JsonWriter jwriter = new JsonWriter(swriter);
		
		jwriter.beginObject() ;
		jwriter.name("prson") ;
		
		{
			jwriter.beginObject() ;
			jwriter.name("name").value("bleujin").name("age").value(20) ;
			jwriter.endObject() ;
			
//			jwriter.beginObject() ;
			jwriter.name("address") ;
//			{
				jwriter.beginArray() ;
				jwriter.value("seoul").value("busan") ;
				jwriter.endArray() ;
//			}
//			jwriter.endObject() ;
		}
		
		jwriter.endObject() ;
		
		Debug.line(swriter.getStringBuilder()) ;
		
	}
	
	public void testWriterSimple() throws Exception {
		JsonObject json = new JsonObject().put("name", "bleujin").put("age", 20).put("address", new JsonArray().adds("seoul", "busan", "inchon") );
		printJsonWriter(json);
	}
	
	public void testWriterSimple2() throws Exception {
		JsonObject json = new JsonObject().put("persons", new JsonArray().adds(new JsonObject().put("name", "bleujin"), new JsonObject().put("name", "hero")));
		printJsonWriter(json);
	}

	public void testWriterSimple3() throws Exception {
		printJsonWriter(JsonObject.fromString("{\"fieldIndexes\":{\"name\":\"KEYWORD\"}, \"ignoreBody\":false}") );
	}



	private void printJsonWriter(JsonObject json) throws IOException {
		final StringBuilderWriter swriter = new StringBuilderWriter();
		JsonWriter jwriter = new JsonWriter(swriter);
		jwriter.beginObject() ;
		jwriter.jsonElement("root", json) ;
		jwriter.endObject() ;
		Debug.line(swriter.getStringBuilder()) ;
	}
	
	
	
	
	
}
