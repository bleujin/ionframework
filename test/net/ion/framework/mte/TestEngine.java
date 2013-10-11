package net.ion.framework.mte;

import net.ion.framework.util.MapUtil;
import junit.framework.TestCase;

public class TestEngine extends TestCase{
	
	private Engine engine;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.engine = Engine.createDefaultEngine() ;
	}
	
	
	public void testToken() throws Exception {
		assertEquals("${", engine.config().exprStartToken()) ;
		assertEquals("}", engine.config().exprEndToken()) ;
	}
	
	public void testModifyToken() throws Exception {
		engine = EngineConfig.newBuilder().exprStartToken("<").exprEndToken(">").createEngine() ;
		
		String result = engine.transform("greeting <name>", MapUtil.<String,Object>create("name", "bleujin"));
		assertEquals("greeting bleujin", result) ;
	}
	
	
	
	
}
