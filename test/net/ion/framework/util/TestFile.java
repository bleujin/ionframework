package net.ion.framework.util;

import java.io.File;
import java.io.FileFilter;

import junit.framework.TestCase;

public class TestFile extends TestCase{
	
	public void testFind() throws Exception {
		File found = FileUtil.findFile(new File("./lib_source"), new FileFilter() {
			public boolean accept(File file) {
				return file.getName().equals("json.jar");
			}
		}, true) ;
		assertEquals("json.jar", found.getName()) ;
	}

	public void testFinds() throws Exception {
		File[] founds = FileUtil.findFiles(new File("./lib_source"), new FileFilter() {
			public boolean accept(File file) {
				return file.getName().equals("json.jar1");
			}
		}, true) ;
		assertEquals(0, founds.length) ;
	}


}
