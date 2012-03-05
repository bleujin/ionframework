package net.ion.framework.vfs;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

public class TestVFSBase extends TestCase{

	
	protected static FileSystemEntry entry ;
	
	static {
		init() ;
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	private static void init(){
		URL config;
		try {
			config = new File("./src/net/ion/framework/vfs/myprovider.xml").toURL();
			entry = VFS.getManger(config) ;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
}
