package net.ion.framework.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

public class TestFileUtil extends TestCase {

	public void testCount() throws Exception {
		
		final AtomicInteger inc = new AtomicInteger() ;
		
		FileUtil.visit(new File("c:\\temp"), new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return true;
			}
		}, new VisitorFile<Void>(){
			public Void visit(File file) {
				int v = inc.incrementAndGet() ;
				if (v % 1000 == 0) Debug.line(); 
				return null;
			}
			
		}) ;
		
		Debug.line(inc);
		
	}
	

}
