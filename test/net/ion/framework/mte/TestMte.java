package net.ion.framework.mte;

import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.MapUtil;

public class TestMte extends TestCase{

//   // sample template 	
//	 ${foreach cart.items item}
//	   ${item.amount}x ${item.name} ${item.price} each = ${item.total}
//	 ${end}
//
//	 ${if cart.addShipping}
//	   Shipping = ${cart.shippingCost}
//	 ${end}
	
	private Engine engine;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.engine = Engine.createDefaultEngine() ;
	}

	
	public void testHello() throws Exception {
		String result = engine.transform("greeting ${name}", MapUtil.<String,Object>create("name", "bleujin"));
		assertEquals("greeting bleujin", result) ;
	}
	
	public void testCondition() throws Exception {
		String result = engine.transform("${if name}greeting ${name}${end}", MapUtil.<String,Object>create("name", "bleujin"));
		assertEquals("greeting bleujin", result) ;
	}
	
	public void testConditionDefault() throws Exception {
		assertEquals("bleujin", engine.transform("${name[empty]}", MapUtil.<String,Object>create("name", "bleujin")));
		assertEquals("empty", engine.transform("${name[empty]}", new HashMap<String, Object>()));
	}
	
	
	public void testConditionShortcuts() throws Exception {
		assertEquals("unknown", engine.transform("${if name}${name}${else}unknown${end}", new HashMap<String, Object>()));
		assertEquals("unknown", engine.transform("${name[unknown]}", new HashMap<String, Object>()));
		assertEquals("", engine.transform("${<h1>,name,</h1>}", new HashMap<String, Object>()));
		assertEquals("<h1>bleujin</h1>", engine.transform("${<h1>,name,</h1>}", MapUtil.<String,Object>create("name", "bleujin")));
	}
	
	
	
	
	public void testLoop() throws Exception {
		List<String> set = ListUtil.toList("bleujin", "hero") ;
		String result = engine.transform("${foreach names item}${item} ${end}", MapUtil.<String,Object>create("names", set));
		assertEquals("bleujin hero ", result) ;
	}
	
	public void testLoopSeperator() throws Exception {
		List<String> set = ListUtil.toList("bleujin", "hero") ;
		String result = engine.transform("${foreach names item ,}${item}${end}", MapUtil.<String,Object>create("names", set));
		assertEquals("bleujin,hero", result) ;
	}
	
	
	public void testLoopSpecial() throws Exception {
		List<String> set = ListUtil.toList("bleujin", "hero") ;
		String first = 	"${foreach names item} " +
						 "${if first_item}${item} is first" +
						 "${else}${item}" +
						 "${end}" +
						"${end}" ;
		
		String result = engine.transform(first, MapUtil.<String,Object>create("names", set));
		assertEquals("bleujin is first hero", result.trim()) ;
		

		String last = 	"${foreach names item} " +
						 "${if last_item}${item} is last" +
						 "${else}${item}" +
						 "${end}" +
						"${end}" ;

		assertEquals("bleujin hero is last", engine.transform(last, MapUtil.<String,Object>create("names", set)).trim()) ;


		String odd = 	"${foreach names item ,} " +
						 "${if odd_item}${item} is odd" +
						 "${else}${item}" +
						 "${end}" +
						"${end}" ;

		assertEquals("bleujin, hero is odd", engine.transform(odd, MapUtil.<String,Object>create("names", set)).trim()) ;
	}
	
	public void testEscape() throws Exception {
		String result = engine.transform("greeting \\${name}${name}", MapUtil.<String,Object>create("name", "bleujin"));
		assertEquals("greeting ${name}bleujin", result) ;
	}
	
}
