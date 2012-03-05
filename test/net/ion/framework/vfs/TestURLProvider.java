package net.ion.framework.vfs;

import java.io.OutputStream;

import net.ion.framework.util.Debug;

import org.apache.commons.vfs2.provider.url.UrlFileProvider;

public class TestURLProvider extends TestVFSBase{

	
	public void testURL() throws Exception {
		entry.addProvider("rest", new UrlFileProvider()) ;
		
		VFile vf = entry.resolveFile("webdav://i1.media.daumcdn.net/img-media/2010ci/daumlogo.gif") ;
		
		Debug.line(vf.getContent().getLastModifiedTime(), vf.getContent().getSize()) ;
	
		OutputStream output = vf.getOutputStream() ;
	}
}
