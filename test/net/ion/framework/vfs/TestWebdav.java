package net.ion.framework.vfs;

import java.io.InputStream;

import net.ion.framework.util.Debug;

import org.apache.commons.vfs2.provider.webdav.WebdavFileProvider;

public class TestWebdav extends TestVFSBase{

	
	public void testWebDav() throws Exception {
		
		final WebdavFileProvider provider = new WebdavFileProvider();
		
		entry.addProvider("webdav", provider) ;
		
		VFile vfile = entry.resolveFile("webdav://localhost:9002/webdav/afield/dd.xml") ;
		InputStream input = vfile.getInputStream() ;

		Debug.debug(input) ;
		
		input.close() ;
	}
}
