package net.ion.framework.parse.html;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;

import net.ion.framework.util.Debug;
import net.ion.framework.util.IOUtil;

import junit.framework.TestCase;

public class TestBigHtml extends TestCase{

	
	public void testParse() throws Exception {
		
		StringReader reader = new StringReader(IOUtil.toString(new FileReader(new File("c:/test/test.html")))) ;
		HTag tag  = HTag.createGeneral(reader, "html") ;
		//Debug.line(tag.toString()) ;
	}
}
