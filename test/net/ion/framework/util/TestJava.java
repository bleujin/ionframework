package net.ion.framework.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * <p>Title: TestJava.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: I-ON Communications</p>
 * <p>Date : 2011. 5. 12.</p>
 * @author novision
 * @version 1.0
 */
public class TestJava extends TestCase{

	
	public void testCollection() throws Exception {
		
		List<String> list = new ArrayList<String>() ;
		
		list.add("ad") ;
		list.add("cd") ;
		
		
		Object[] sa = list.toArray() ;
		Debug.debug(sa) ;
		
	}
}
