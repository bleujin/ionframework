package net.ion.framework;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import net.ion.framework.db.sample.TestAllDB;
import net.ion.framework.parse.html.HTagAllTest;
import net.ion.framework.rope.RopeAllTest;
import net.ion.framework.util.Debug;

public class TestAllFramework extends TestCase{
	public static void main(String[] args) {
		suite().run(new TestResult());
	}

	public static TestSuite suite() {
		System.setProperty(Debug.PROPERTY_KEY, "off") ;
		TestSuite ts = new TestSuite("Framework Test ALL") ;
		
		ts.addTest(HTagAllTest.suite()) ;
		ts.addTest(RopeAllTest.suite()) ;
		ts.addTest(TestAllDB.suite()) ;
		

		return ts ;
	}
}
