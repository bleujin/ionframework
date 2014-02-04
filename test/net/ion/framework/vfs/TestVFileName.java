package net.ion.framework.vfs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import net.ion.framework.util.Debug;

public class TestVFileName extends TestVFSBase{

	public void testCall() throws Exception {
		VFile fo = entry.resolveFile(VPath.create("afield:/Word.ko-kr/setup.xml"));
		assertEquals(fo.exists(), true) ;
	}
	
	public void testMethod() throws Exception {
		VFile fo = entry.resolveFile(VPath.create("afield:/Word.ko-kr/setup.xml"));
		VFileName vname = fo.getName() ;
		
		Debug.debug(vname.getURI(), vname.getRoot().getBaseName(), vname.getScheme(), vname.getBaseName(), vname.getParent(), vname.getDepth()) ;
		
		Debug.debug(fo.getParent().getName().getBaseName()) ;
	}
	
	
	
	public void testFile() throws Exception {
		File f = new File("abcd.txt") ;
		FileWriter fw = new FileWriter(f) ;
		
		fw.append("Hello") ;
		
		fw.close() ;
		
		Debug.debug(f.toURI()) ;
	}
	
	
	public void testVFSFile() throws Exception {
		final String name = "db://board/article/123.row";
		VFile vf = entry.resolveFile(name) ;
		Writer writer = new OutputStreamWriter(vf.getOutputStream()) ;
		writer.append("<name>bleujin</name>");
		writer.append("<age>bleujin</age>")  ;
		writer.append("<address>bleujin</address>");
		
		vf.setAttribute("age", 11) ;
		writer.close() ;
		
		vf.close() ;
		readFile(name) ;
	}

	private void readFile(String name) throws IOException {
		
		VFile vf = entry.resolveFile("gallery://abcd/") ;
		List<VFile> children = vf.getChildren() ;
		
		for (VFile vfile : children) {
			Debug.debug(vfile.getName().getPath()) ;
		}
	}
	
}
