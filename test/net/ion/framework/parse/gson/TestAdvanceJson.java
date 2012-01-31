package net.ion.framework.parse.gson;

import java.util.List;

import junit.framework.TestCase;
import net.ion.framework.parse.gson.annotations.SerializedName;
import net.ion.framework.parse.gson.annotations.Since;
import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;

public class TestAdvanceJson extends TestCase {

	public void testObject() throws Exception {
		JsonObject json = JsonParser.fromObject(new People("bleujin", 20)).getAsJsonObject();

		assertEquals("bleujin", json.asString("username"));
		assertEquals(20, json.asInt("age"));
	}

	public void testCompli() throws Exception {
		JsonObject json = JsonParser.fromObject(new Student(new People("bleujin", 20), new Address("seoul", "busan"))).getAsJsonObject();
		assertEquals("bleujin", json.asJsonObject("people").asString("username"));
		assertEquals(20, json.asJsonObject("people").asInt("age"));
		assertEquals("seoul", json.asJsonObject("address").asJsonArray("city").asString(0));
		
		Debug.line(json.toString()) ;
		
		People people = json.asObject("people", People.class) ;
		assertEquals("bleujin", people.name) ;
		Address address = json.asObject("address", Address.class) ;
		assertEquals("seoul", address.city.get(0)) ;

	}

	public void testVersion() throws Exception {
		VersionedClass obj = new VersionedClass();
		Gson gson = new GsonBuilder().setVersion(1.0).create();
		System.out.println(gson.toJson(obj));
		gson = new Gson();
		System.out.println(gson.toJson(obj));

		// {"newField":"new","field":"old"}
		// {"newerField":"newer","newField":"new","field":"old"}
	}

	public void testNullObject() throws Exception {
		Gson gson = new GsonBuilder().serializeNulls().create();
		Foo foo = new Foo();
		String json = gson.toJson(foo);
		System.out.println(json);

		json = gson.toJson(null);
		System.out.println(json);

		// {"s":null,"i":5}
		// null
	}

}

class People {
	@SerializedName("username") String name;
	int age;

	People(String name, int age) {
		this.name = name;
		this.age = age;
	}
}

class Address {
	List<String> city;

	Address(String... city) {
		this.city = ListUtil.toList(city);
	}
}

class Student {

	private People people;
	private Address address;

	Student(People peo, Address add) {
		this.people = peo;
		this.address = add;
	}

}

class VersionedClass {
	@Since(1.1) private final String newerField;
	@Since(1.0) private final String newField;
	private final String field;

	public VersionedClass() {
		this.newerField = "newer";
		this.newField = "new";
		this.field = "old";
	}
}

class Foo {
	private final String s;
	private final int i;

	public Foo() {
		this(null, 5);
	}

	public Foo(String s, int i) {
		this.s = s;
		this.i = i;
	}
}