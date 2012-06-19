package net.ion.framework.io;

import java.io.File;
import java.net.URI;
import java.net.URL;

import junit.framework.TestCase;
import net.ion.framework.configuration.Configuration;
import net.ion.framework.dio.FSInputStream;
import net.ion.framework.dio.IOSystem;
import net.ion.framework.dio.Path;
import net.ion.framework.util.Debug;

import org.apache.commons.io.IOUtils;

public class TestUri extends TestCase{

	
	public void testHTTPURI() throws Exception {
		URI uri = URI.create("http://www.java2s.com/Code/Java/Ant/Jarwithincludesandexcludes.htm") ;
		printURI(uri) ;
		
		URI f = URI.create("file:///") ;
		printURI(f) ;
		
		
	}
	
	
	public void testEqual() throws Exception {
		URL u1 = new File("./test/net/ion/framework/io/myprovider.xml").toURL() ;
		URL u2 = new File("./test/net/ion/framework/io/myprovider.xml").toURL() ;
		
		assertEquals(u1, u2) ;
	}
	
	public void testWhat() throws Exception {
		URI uri = URI.create("file:///") ;
		printURI(uri);
		
		
		final String hpathString = "http://www.java2s.com/Code/Java/Ant/Jarwithincludesandexcludes.htm";
		URI huri = URI.create(hpathString) ;
		printURI(huri);
		
		Configuration conf = null ;
		IOSystem is = IOSystem.get(URI.create("file:///"), conf) ;
		
		FSInputStream input = IOSystem.get(URI.create(hpathString), conf).open(Path.create(hpathString)) ;
		Debug.debug(IOUtils.toString(input)) ;
		
		input.close() ;
	}

	private void printURI(URI uri) {
		Debug.debug("scheme:", uri.getScheme(), "authority:", uri.getAuthority(), "path:", uri.getPath(), "host:", uri.getHost(), "port:", uri.getPort()) ;
	}
	
	public void testFileSystem() throws Exception {
		Configuration conf = null ;
		IOSystem is = IOSystem.get(URI.create("file:///"), conf) ;
		
		Debug.debug(is.getWorkingDirectory(), is.getHomeDirectory()) ;
		
		final Path newpath = Path.create("file:///c:/temp/im.htm");
		is.setWorkingDirectory(newpath) ;
		Debug.debug(is.getWorkingDirectory(), is.getHomeDirectory(), newpath.getParent(), newpath.getFileSystem(null), newpath.getName()) ;
	
		Debug.debug(newpath.depth(), newpath.toUri()) ;
		
		
		FSInputStream input = is.open(newpath) ;
		Debug.debug(IOUtils.toString(input)) ;
		input.close() ;
	}
	
	
	
	
	
	
}
