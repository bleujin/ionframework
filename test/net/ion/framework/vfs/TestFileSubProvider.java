package net.ion.framework.vfs;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Date;

import junit.framework.TestCase;
import net.ion.framework.util.Debug;

import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.filters.StringInputStream;
import org.restlet.data.MediaType;

public class TestFileSubProvider extends TestVFSBase {

	public void testFS() throws Exception {
		Debug.debug(entry.getSchemes()) ;
		VFile fo = entry.resolveFile(VPath.create("afield:/Word.ko-kr/setup.xml"));
		
		Debug.debug(IOUtils.toString(fo.getInputStream())) ;
		VFile fo2 = entry.resolveFile("afield:/Word.ko-kr/mydir/abddd/setup.xml2");
		
		Writer writer = new OutputStreamWriter(fo2.getOutputStream(), "UTF-8") ;
		writer.write("하이 블루진") ;
		writer.close() ;
	}
	
	
	public void testRam() throws Exception {
		VFile wfo = entry.resolveFile(VPath.create("ram:/mydir/abcdefg"));
		Writer writer = new OutputStreamWriter(wfo.getOutputStream(), "UTF-8") ;
		writer.write("하이 블루진") ;
		writer.close() ;
		
		VFile rfo = entry.resolveFile(VPath.create("ram:/mydir/abcdefg"));
		Debug.debug(IOUtils.toString(rfo.getInputStream(), "UTF-8")) ;
	}
	
	
	public void testUndefinedMediaType() throws Exception {
		MediaType m = MediaType.valueOf("abcd") ;
	}
	
	public void testConflict() throws Exception {
		VFile wfo1 = entry.resolveFile(VPath.create("ram:/mydir/abcdefg"));
		final OutputStream o1 = wfo1.getOutputStream();
		o1.write(45) ;
		
		
		VFile wfo2 = entry.resolveFile(VPath.create("ram:/mydir/abcdefg"));
		Debug.debug(wfo2.exists()) ;
	}
	
	
	public void testAddProvider() throws Exception {
		final LocalSubDirFileProvider provider = new LocalSubDirFileProvider();
		// provider.setPrefixDir("d:/doc/") ;
		entry.addProvider(new String[]{"myfile"}, provider) ;
		
		VFile vfile = entry.resolveFile("myfile:/doc/abcd.txt") ;
		InputStream input = new StringInputStream("Hello Bleujin" + new Date()) ;
		
		new OutputStreamWriter(vfile.getOutputStream()).write("Hello bleujin") ;
		
		Debug.debug(vfile.getName().getURI(), vfile.getName(), vfile.getParent()) ;
		vfile.close() ;
	}
	
	public void testDuplicateFileName() throws Exception {
		InputStream input = new StringInputStream("Hello Bleujin" + new Date()) ;
		VFile vf = entry.write(input, "myfile:/doc/abcd.txt") ;
	}
}
