package net.ion.framework.vfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.CacheStrategy;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.impl.DefaultFileSystemManager;
import org.apache.commons.vfs.provider.FileProvider;

public class FileSystemEntry {
	
	private DefaultFileSystemManager fsm ;
	private FileSystemEntry(DefaultFileSystemManager fsm) {
		this.fsm = fsm ;
	}

	public final static FileSystemEntry create(DefaultFileSystemManager fsm){
		return new FileSystemEntry(fsm) ;
	}

	public String[] getSchemes() {
		return fsm.getSchemes();
	}

	public VFile resolveFile(VPath vpath) throws FileSystemException {
		return resolveFile(vpath, new FileSystemOptions()) ;
	}

	public VFile resolveFile(String path) throws FileSystemException {
		return resolveFile(VPath.create(path));
	} 
	
	public VFile resolveFile(String path, FileSystemOptions options) throws FileSystemException {
		return resolveFile(VPath.create(path), options);
	}
	
	public VFile resolveFile(VPath vpath, FileSystemOptions options) throws FileSystemException {
		FileObject resolveFile = fsm.resolveFile(vpath.stringPath(), options);
		return VFile.create(resolveFile);
	}

	
	public VFile toVFile(File file) throws FileSystemException{
		return VFile.create(fsm.toFileObject(file)) ;
	}
	
	public VFile getBaseFile() throws FileSystemException {
		return VFile.create(fsm.getBaseFile()) ;
	}

	public CacheStrategy getCacheStrategy(){
		return fsm.getCacheStrategy() ;
	}

	public void addProvider(String[] scheme, FileProvider provider) throws FileSystemException {
		fsm.addProvider(scheme, provider) ;
	}
	
	public void addProvider(String scheme, FileProvider provider) throws FileSystemException {
		addProvider(new String[]{scheme}, provider) ;
	}

	public VFile write(InputStream input, String name) throws IOException {
		return write(input, name, RenamePolicy.OverWritePolicy);
	}

	public VFile write(InputStream input, String name, RenamePolicy policy) throws IOException {
		VFile vf = resolveFile(name) ;
		return write(input, vf, policy);
	}

	public VFile write(InputStream input, VFile vf, RenamePolicy policy) throws IOException {
		vf = policy.rename(vf, this);
		OutputStream output = vf.getOutputStream() ;
		try {
			IOUtils.copy(input, output) ;
		} finally {
			IOUtils.closeQuietly(output) ;
		}
		return vf;
	}
	
	public boolean hasProvider(String scheme) {
		return fsm.hasProvider(scheme);
	}

	
}
