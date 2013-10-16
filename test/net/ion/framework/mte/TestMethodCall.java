package net.ion.framework.mte;

import java.util.Locale;

import net.ion.framework.util.Debug;
import net.ion.framework.util.MapUtil;
import junit.framework.TestCase;

public class TestMethodCall extends TestCase{

	
	private Engine engine;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.engine = Engine.createDefaultEngine() ;
	}

	public void testField() throws Exception {
		assertEquals("bleujin", engine.transform("${person.name}", MapUtil.<String, Object>create("person", new PersonField("bleujin", true)))) ;
		assertEquals("bleujin", engine.transform("${person.myName}", MapUtil.<String, Object>create("person", new PersonField("bleujin", true)))) ;
		assertEquals("bleujin", engine.transform("${person.meName}", MapUtil.<String, Object>create("person", new PersonField("bleujin", true)))) ;
		assertEquals("bleujin", engine.transform("${person.privateName}", MapUtil.<String, Object>create("person", new PersonField("bleujin", true)))) ;
	}
	
	public void testCompile() throws Exception {
		assertEquals("bleujin", engine.getTemplate("${person.name}").transform(MapUtil.<String, Object>create("person", new PersonField("bleujin", true)), Locale.KOREA)) ;
	}
	
	public void testArgumentFunction() throws Exception {
		assertEquals("bleujin hello 3", engine.transform("${person.nestedName(\" hello \",  3)}", MapUtil.<String, Object>create("person", new PersonField("bleujin", true)))) ;
	}
	

	public void testTemplateFunction() throws Exception {
		assertEquals("bleujin", engine.getTemplate("${person.meName}").transform(MapUtil.<String, Object>create("person", new PersonField("bleujin", true)), Locale.KOREA)) ;
		assertEquals("bleujin", engine.getTemplate("${person.myName}").transform(MapUtil.<String, Object>create("person", new PersonField("bleujin", true)), Locale.KOREA)) ;
		assertEquals("bleujin", engine.getTemplate("${person.privateName}").transform(MapUtil.<String, Object>create("person", new PersonField("bleujin", true)), Locale.KOREA)) ;
	}


	public void testArgumentTemplateFunction() throws Exception {
		assertEquals("bleujin hello 3", engine.getTemplate("${person.nestedName(\" hello \",  3)}").transform(MapUtil.<String, Object>create("person", new PersonField("bleujin", true)), Locale.KOREA)) ;
	}
	

}



class PersonField {
	
	private String name ;
	private boolean man ;
	
	PersonField(String name, boolean man){
		this.name = name ;
		this.man = man ;
	}
	
	public String getMyName(){
		return name ;
	}
	
	public String meName(){
		return name ;
	}

	private String privateName(){
		return name ;
	}
	
	public String nestedName(String add, int i){
		return name + add + i;
	}

}