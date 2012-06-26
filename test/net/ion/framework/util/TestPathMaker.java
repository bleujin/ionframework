package net.ion.framework.util;

import junit.framework.TestCase;

public class TestPathMaker extends TestCase {

	public void testNull() throws Exception {
		String pdir = StringUtil.defaultIfEmpty(System.getProperty("abc,def"), "./") ;
		String path = PathMaker.getFilePath(pdir, "plugin/") ;
		Debug.line(path) ;
	}
}
