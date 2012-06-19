package net.ion.framework.convert.test;

import java.io.File;
import java.io.FileOutputStream;

import net.ion.framework.convert.html.CleanerProperties;
import net.ion.framework.convert.html.PrettyHtmlSerializer;
import net.ion.framework.convert.html.TagNode;
import junit.framework.TestCase;

public class TestPrefix extends TestCase{

	
	public void testXMLPrefix() throws Exception {
		CleanerProperties prop = getProp() ;
		TagNode root = new TagNode("multistatus") ;
		root.setName("multistatus"); 
		
		
		
		
		new PrettyHtmlSerializer(prop).writeToStream(
			    root, System.out, "utf-8"
			);
	}
	
	private CleanerProperties getProp() {
		CleanerProperties props = new CleanerProperties();
		 
		// set some properties to non-default values
//		props.setTranslateSpecialEntities(true);
//		props.setTransResCharsToNCR(true);
//		props.setUseCdataForScriptAndStyle(true);
//		props.setOmitComments(true);
//		props.setOmitXmlDeclaration(true) ;
		return props;
	}
}
