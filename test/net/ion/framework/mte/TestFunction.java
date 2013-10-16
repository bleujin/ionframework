package net.ion.framework.mte;

import java.util.List;

import org.apache.ecs.xhtml.pre;

import net.ion.framework.mte.util.MiniParser;
import net.ion.framework.mte.util.NestedParser;
import net.ion.framework.util.Debug;

import junit.framework.TestCase;

public class TestFunction extends TestCase {

	private MiniParser miniParser = MiniParser.rawOutputInstance() ;
	
	public void testFunction() throws Exception {
		String input = "person.nestedName(\"dd\", 3)";
		Debug.line(input) ;
		List<String> segments = miniParser.scan(input, "(", ")");
		assertEquals(2, segments.size());
		assertEquals("person.nestedName", segments.get(0));
		assertEquals("\"dd\", 3", segments.get(1));
		
		
		
	}
	
	public void testArg() throws Exception {
		FunArgumentParser parser = FunArgumentParser.getInstance();
		
		Object[] presult = parser.parseArgs("person.nestedName(\"dd\", 3)");
		assertEquals(2, presult.length) ;
		assertEquals("dd", presult[0]) ;
		assertEquals(3, presult[1]) ;

		presult = parser.parseArgs("person.nestedName(\"dd\")");
		assertEquals(1, presult.length) ;
		assertEquals("dd", presult[0]) ;
	}
}
