package net.ion.framework;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import net.ion.framework.db.sample.DBSampleAllTest;
import net.ion.framework.parse.ParseSuite;
import net.ion.framework.rope.RopeSuite;
import net.ion.framework.util.Debug;

public class TestAllFramework extends TestCase{
	public static void main(String[] args) {
		suite().run(new TestResult());
	}

	public static TestSuite suite() {
		System.setProperty(Debug.PROPERTY_KEY, "off") ;
		TestSuite ts = new TestSuite("Framework Test ALL") ;
		
		ts.addTest(ParseSuite.suite()) ;
		ts.addTest(RopeSuite.suite()) ;
		ts.addTest(DBSampleAllTest.suite()) ;
		

		return ts ;
	}
}
