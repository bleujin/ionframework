package net.ion.framework.parse.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import junit.framework.TestCase;
import net.ion.framework.util.Debug;

public class TestParser extends TestCase {

	public void testElementValue() throws Exception {
		StringReader reader = new StringReader(new String("<a>a<b>b<c>[!CDATA[c]]</c></b></a>"));

		HTag root = GeneralParser.parseGeneral(reader, "a");

		assertEquals("[!CDATA[c]]", root.getElementValue("b/c"));
		assertEquals("b<c>[!CDATA[c]]</c>", root.getElementValue("b"));
	}
}
