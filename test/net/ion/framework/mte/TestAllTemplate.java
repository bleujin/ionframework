package net.ion.framework.mte;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAllTemplate extends TestCase {
	
	public static TestSuite suite() {
		TestSuite ts = new TestSuite("Template Test ALL") ;
		
		ts.addTestSuite(TestMiniparser.class) ;
		ts.addTestSuite(TestEngine.class) ;
		ts.addTestSuite(TestMte.class) ;
		ts.addTestSuite(TestRender.class) ;
		ts.addTestSuite(TestMethodCall.class) ;

		ts.addTestSuite(TestCompile.class) ;
		ts.addTestSuite(TestLive.class) ;
		
		return ts ;
	}
}
