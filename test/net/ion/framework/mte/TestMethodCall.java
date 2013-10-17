package net.ion.framework.mte;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;
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
		assertEquals("bleujin", engine.transform("${person.name}", MapUtil.<String, Object>create("person", new Person("bleujin", true)))) ;
		assertEquals("bleujin", engine.transform("${person.myName}", MapUtil.<String, Object>create("person", new Person("bleujin", true)))) ;
		assertEquals("bleujin", engine.transform("${person.meName}", MapUtil.<String, Object>create("person", new Person("bleujin", true)))) ;
		assertEquals("bleujin", engine.transform("${person.privateName}", MapUtil.<String, Object>create("person", new Person("bleujin", true)))) ;
	}
	
	public void testCompile() throws Exception {
		assertEquals("bleujin", engine.getTemplate("${person.name}").transform(MapUtil.<String, Object>create("person", new Person("bleujin", true)), Locale.KOREA)) ;
		assertEquals("bleujin", engine.transform("${person.myName()}", MapUtil.<String, Object>create("person", new Person("bleujin", true)))) ;
	}
	
	public void testArgumentFunction() throws Exception {
		assertEquals("bleujin hello 3", engine.transform("${person.nestedName(hello,3) }", MapUtil.<String, Object>create("person", new Person("bleujin", true)))) ;
	}
	

	public void testTemplateFunction() throws Exception {
		assertEquals("bleujin", engine.getTemplate("${person.meName}").transform(MapUtil.<String, Object>create("person", new Person("bleujin", true)), Locale.KOREA)) ;
		assertEquals("bleujin", engine.getTemplate("${person.meName}").transform(MapUtil.<String, Object>create("person", new Person("bleujin", true)), Locale.KOREA)) ;
		assertEquals("bleujin", engine.getTemplate("${person.myName}").transform(MapUtil.<String, Object>create("person", new Person("bleujin", true)), Locale.KOREA)) ;
		assertEquals("bleujin", engine.getTemplate("${person.myName}").transform(MapUtil.<String, Object>create("person", new Person("bleujin", true)), Locale.KOREA)) ;
		assertEquals("bleujin", engine.getTemplate("${person.privateName}").transform(MapUtil.<String, Object>create("person", new Person("bleujin", true)), Locale.KOREA)) ;
		assertEquals("bleujin", engine.getTemplate("${person.privateName()}").transform(MapUtil.<String, Object>create("person", new Person("bleujin", true)), Locale.KOREA)) ;
	}


	public void testArgumentTemplateFunction() throws Exception {
		assertEquals("bleujin hello 3", engine.getTemplate("${person.nestedName(hello,3)}").transform(MapUtil.<String, Object>create("person", new Person("bleujin", true)), Locale.KOREA)) ;
	}
	
	public void testChainMethod() throws Exception {
		assertEquals("sungnam", engine.transform("${person.address().cityName(\"sungnam\")}", MapUtil.<String, Object>create("person", new Person("bleujin", true)))) ;
		assertEquals("sungnam", engine.transform("${person.address().chain().chain.cityName(\"sungnam\")}", MapUtil.<String, Object>create("person", new Person("bleujin", true)))) ;
	}
	
	public void testChainMethod2() throws Exception {
		assertEquals("sungnam", engine.transform("${person.address().chain().chain().cityName(\"sungnam\")}", MapUtil.<String, Object>create("person", new Person("bleujin", true)))) ;
	}
	
	public void testChainIn() throws Exception {
		String result = engine.transform("${persons.children().eq()}", MapUtil.<String, Object>create("persons", new Persons())) ;
		Debug.line(result) ;
	}
	
	public void xtestInChain() throws Exception {
		String result = engine.transform("${foreach persons.children().eq() person \\$ }${person.address()}${end}", MapUtil.<String, Object>create("persons", new Persons())) ;
		Debug.line(result) ;
	}
	
	
	

}


class Persons {
	private PersonWrapper w = new PersonWrapper() ;
	public PersonWrapper children(){
		 return w;
	}
}


class PersonWrapper implements Iterable<Person>{
	private List<Person> datas = ListUtil.toList(new Person("bleujin", true), new Person("jin", true), new Person("hero", true), new Person("hero", true), new Person("jin", true)) ;
	
	public Iterator<Person> iterator() {
		return datas.iterator();
	}
	
	public PersonWrapper eq(){
//		datas = datas.subList(1, datas.size()) ;
		Debug.line() ;
		return this ;
	}
	
}


class Person {
	
	private String name ;
	private boolean man ;
	private Address address = new Address();
	
	Person(String name, boolean man){
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
		return name + " " + add + " " + i;
	}

	public Address address(){
		return address ;
	}
	
	public String toString(){
		return "Person:" + name ;
	}
	
}

class Address {
	private String city = "seoul" ;
	
	public String cityName(String cityName){
		return cityName ;
	}
	
	public Address chain(){
		return this ;
	}
	public String toString(){
		return "Address:" + city ;
	}
}

