package net.ion.framework.mte;

import java.util.Locale;

import net.ion.framework.mte.template.Template;
import net.ion.framework.util.Debug;
import net.ion.framework.util.MapUtil;
import net.ion.framework.util.RandomUtil;
import junit.framework.TestCase;

public class TestCompile extends TestCase {

	
	public void testCompile() throws Exception {
		Engine engine = Engine.createCompilingEngine();
		
		Locale locale = Locale.getDefault();
		Template template = engine.getTemplate("${if name} greeting ${name} ${end}", "hello");
		long start = System.currentTimeMillis() ;
		for (int i = 0; i < 100000; i++) {
			template.transform(MapUtil.<String,Object>create("name", RandomUtil.nextRandomString(10)), locale) ;
		}
		
		Debug.line(System.currentTimeMillis() - start) ;
	}
	
	public void testNotCompile() throws Exception {
		Engine engine = Engine.createDefaultEngine() ;
		
		long start = System.currentTimeMillis() ;
		for (int i = 0; i < 100000; i++) {
			String result = engine.transform("${if name} greeting ${name} ${end}", MapUtil.<String,Object>create("name", RandomUtil.nextRandomString(10)));
		}
		
		Debug.line(System.currentTimeMillis() - start) ;
	}
	
	public void xtestNamedCache() throws Exception {
		Engine engine = EngineConfig.newBuilder().useCompilation(true).createEngine();

		final Template template = engine.getTemplate("Hello ${name}", "hello");
		assertEquals("Hello bleujin", template.transform(MapUtil.stringMap("name", "bleujin"), Locale.KOREA)) ;
		
		
		assertEquals("Hi bleujin", engine.getTemplate("Hi ${name}", "hello").transform(MapUtil.stringMap("name", "bleujin"), Locale.KOREA)) ;
	}
	
	
}
