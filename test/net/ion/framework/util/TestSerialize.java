package net.ion.framework.util;

import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

public class TestSerialize extends TestCase{
	
	public void testAsStream() throws Exception {
		
		Debug.debug(this.getClass().getClassLoader().getResourceAsStream("net/ion/framework/util/TestSerialize.class")) ;

		InputStream input = getClass().getResourceAsStream("net.ion.framework.util.TestSerialize") ;
		
		assertEquals(true, input != null) ;
		
		
		
		input.close() ;
	}

}
