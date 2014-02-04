package net.ion.framework.vfs;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.RandomAccessContent;
import org.apache.commons.vfs2.util.RandomAccessMode;

public class VFileContent {

	private FileContent content ;
	private VFileContent(FileContent content) {
		this.content = content ;
	}

	public static VFileContent create(FileContent content) {
		return new VFileContent(content);
	}
	
	public long getLastModifiedTime() throws FileSystemException{
		return content.getLastModifiedTime() ;
	}
	
	public long getSize() throws FileSystemException{
		return content.getSize() ;
	}
	
	
	public void setAttribute(String key, Object value) throws FileSystemException{
		content.setAttribute(key, value) ;
	}
	
	public Map<String, Object> getAttributes() throws FileSystemException{
		return content.getAttributes() ;
	}
	
	public void close() throws FileSystemException{
		content.close() ;
	}
	
	public boolean isOpen(){
		return content.isOpen() ;
	}

	public String getEncoding() throws FileSystemException{
		return content.getContentInfo().getContentEncoding() ;
	}
	
	public String getContentType() throws FileSystemException {
		return content.getContentInfo().getContentType() ;
	}
	
	public RandomAccessContent getRandomAccessContent(RandomAccessMode mode) throws FileSystemException{
		return content.getRandomAccessContent(mode) ;
	}

	public InputStream getInputStream() throws FileSystemException{
		return content.getInputStream() ;
	}
}
