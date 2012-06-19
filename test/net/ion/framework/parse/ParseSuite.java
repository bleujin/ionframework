package net.ion.framework.parse;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.ion.framework.parse.gson.TestAdvanceJson;
import net.ion.framework.parse.gson.TestJsonUtil;
import net.ion.framework.parse.gson.TestSimpleJson;
import net.ion.framework.parse.html.HTMLTest;
import net.ion.framework.parse.html.TestParser;
import net.ion.framework.util.Debug;

public class ParseSuite extends TestCase{
	
	
	public static Test suite() {
		System.setProperty(Debug.PROPERTY_KEY, "off");
		TestSuite ts = new TestSuite("Htag Test");

		
		ts.addTestSuite(TestAdvanceJson.class);
		ts.addTestSuite(TestSimpleJson.class);
		ts.addTestSuite(TestJsonUtil.class);
		
		
		ts.addTestSuite(HTMLTest.class);
		ts.addTestSuite(TestParser.class);

		return ts;
	}
}
