package net.ion.framework.io;

import junit.framework.TestCase;
import net.ion.framework.util.Debug;
import net.ion.framework.vfs.FileSystemEntry;
import net.ion.framework.vfs.LocalSubDirFileProvider;
import net.ion.framework.vfs.VFS;
import net.ion.framework.vfs.VFile;

public class TestAttribute extends TestCase {

	public void testVFile() throws Exception {
		FileSystemEntry efs = VFS.createEmpty() ;
		LocalSubDirFileProvider aprovider = new LocalSubDirFileProvider();
		aprovider.setPrefixDir("./resource") ;
		efs.addProvider("resource", aprovider);
		
		VFile vfile = efs.resolveFile("resource:/temp/hello.txt");
		assertEquals(true, vfile.exists());
		
		Debug.line(vfile.getFileObject().getClass());
		
		vfile.getFileObject().getContent().setAttribute("ics.hello", "hello.bleujin");
		
		Debug.line(vfile.getFileObject().getContent().getAttribute("ics.hello")) ;
		
	}
}
