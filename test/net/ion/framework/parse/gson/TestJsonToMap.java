package net.ion.framework.parse.gson;

import java.util.List;
import java.util.Map;

import net.ion.framework.util.Debug;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.MapUtil;
import junit.framework.TestCase;

public class TestJsonToMap extends TestCase {

	public void testJsonToMap() throws Exception {
		Outer outer = new Outer("jin", 20);
		JsonObject json = JsonObject.fromObject(outer);
		final Map<String, Object> map = json.toMap();
		Debug.line(map, map.get("inner").getClass(), map.get("list").getClass(), map.get("array").getClass()) ;
	}
	
	public void testJsonToFlatMap() throws Exception {
		Outer outer = new Outer("jin", 20);
		JsonObject json = JsonObject.fromObject(outer);
		final Map<String, Object> map = json.toMap();

		Map flatMap = MapUtil.toFlat(map);
		
		Debug.line(flatMap) ;
	}
}


class Outer {
	private String name ;
	private int age ;
	private Inner inner ;
	private List<Integer> list = ListUtil.rangeNum(3) ;
	private Integer[] array = new Integer[]{0, 1, 2} ;
	
	public Outer(String name, int age){
		this.name = name ;
		this.age = age ;
		this.inner = new Inner(name) ;
	}

}


class Inner {
	private String name ;
	private GrandInner inner ;
	public Inner(String name){
		this.name = name ;
		this.inner = new GrandInner(name) ;
	}
}

class GrandInner {
	private String name ;
	private String nullString ; 
	public GrandInner(String name){
		this.name = name ;
	}
}


