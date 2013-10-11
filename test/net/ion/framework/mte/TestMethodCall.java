package net.ion.framework.mte;

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
	
	public void testNestedFunction() throws Exception {
		Debug.debug(engine.transform("${person.name}", MapUtil.<String, Object>create("person", new PersonField("bleujin[maxLength=5]", true)))) ;
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

}