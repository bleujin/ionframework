package net.ion.framework.vfs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileSystemException;

public class UTF8TextFile {

	private VFile vfile ;
	private UTF8TextFile(VFile vfile){
		this.vfile = vfile ;
	}
	
	public final static UTF8TextFile create(VFile vfile){
		return new UTF8TextFile(vfile) ;
	} 

	public Reader getReader() throws UnsupportedEncodingException, FileSystemException{
		return new BufferedReader(new InputStreamReader(vfile.getInputStream(), "UTF-8")) ; 
	}

	public Writer getWriter() throws UnsupportedEncodingException, FileSystemException{
		return new BufferedWriter(new OutputStreamWriter(vfile.getOutputStream(), "UTF-8")) ; 
	}
	
	public String readAsString() throws IOException, FileSystemException{
		Reader reader = null;
		try {
			reader = getReader() ;
			return IOUtils.toString(reader) ;
		} finally {
			IOUtils.closeQuietly(reader) ;
		}
	}
	
	public void close() throws FileSystemException{
		vfile.close() ;
	}
}
