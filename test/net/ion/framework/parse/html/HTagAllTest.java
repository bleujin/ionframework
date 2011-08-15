package net.ion.framework.parse.html;

import net.ion.framework.util.Debug;
import junit.framework.Test;
import junit.framework.TestSuite;

public class HTagAllTest extends TestSuite {

	public static Test suite() {
		System.setProperty(Debug.PROPERTY_KEY, "off");
		TestSuite ts = new TestSuite("Htag Test");

		ts.addTestSuite(HTMLTest.class);
		ts.addTestSuite(TestParser.class);

		return ts;
	}
}
