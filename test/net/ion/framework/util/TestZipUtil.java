package net.ion.framework.util;

import java.io.File;

import net.ion.framework.util.ZipUtil.ZipFilter;

import junit.framework.TestCase;

public class TestZipUtil extends TestCase {
	
	public void testZip() throws Exception {
		ZipUtil zipper = new ZipUtil() ;
		File target = File.createTempFile("zip", "test") ;
		
		int entryCount = zipper.zip(new File("./imsi"), target) ;
		assertEquals(3, entryCount) ;
		
		
		zipper.unzip(target, "c:/tmp") ;
	}

	public void testZipFilter() throws Exception {
		ZipUtil zipper = new ZipUtil() ;
		File target = File.createTempFile("zip", "test") ;
		
		ZipFilter zipFilter = new SubDirFilter() ;   
		zipper.setZipFilter(zipFilter) ;
		
		int entryCount = zipper.zip(new File("./imsi"), target) ;
		assertEquals(1, entryCount) ;

		zipper.unzip(target, "c:/tmp") ;
	}

}


class SubDirFilter implements ZipFilter{

	public boolean allow(File file) {
		return file.getParent().endsWith("subdir") ;
	}
	
}