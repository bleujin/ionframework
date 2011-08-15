package net.ion.framework.vfs;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs.Capability;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractOriginatingFileProvider;
import org.apache.commons.vfs.provider.LocalFileProvider;
import org.apache.commons.vfs.provider.UriParser;
import org.apache.commons.vfs.provider.local.GenericFileNameParser;
import org.apache.commons.vfs.provider.local.LocalFileName;
import org.apache.commons.vfs.provider.local.LocalFileNameParser;
import org.apache.commons.vfs.provider.local.LocalFileSystem;
import org.apache.commons.vfs.util.Os;

// Referenced classes of package org.apache.commons.vfs.provider.local:
//            WindowsFileNameParser, GenericFileNameParser, LocalFileNameParser, LocalFileName, 
//            LocalFileSystem

public class LocalSubDirFileProvider extends AbstractOriginatingFileProvider  {

	private String prefixDir = "./" ;
	public static final Collection capabilities;

	static {
		capabilities = Collections.unmodifiableCollection(Arrays.asList(new Capability[] { Capability.CREATE, Capability.DELETE, Capability.RENAME, Capability.GET_TYPE, Capability.GET_LAST_MODIFIED, Capability.SET_LAST_MODIFIED_FILE,
				Capability.SET_LAST_MODIFIED_FOLDER, Capability.LIST_CHILDREN, Capability.READ_CONTENT, Capability.URI, Capability.WRITE_CONTENT, Capability.APPEND_CONTENT, Capability.RANDOM_ACCESS_READ, Capability.RANDOM_ACCESS_WRITE }));
	}
	
	public LocalSubDirFileProvider() {
		setFileNameParser(new GenericFileNameParser());
	}

	public void setPrefixDir(String prefixDir){
		this.prefixDir = prefixDir ;
	}
	
	
	public boolean isAbsoluteLocalName(String name) {
		return ((LocalFileNameParser) getFileNameParser()).isAbsoluteName(name);
	}

	protected FileSystem doCreateFileSystem(FileName name, FileSystemOptions fileSystemOptions) throws FileSystemException {
		LocalFileName rootName = (LocalFileName) name;
		return new LocalFileSystem(rootName, prefixDir + rootName.getRootFile(), fileSystemOptions);
	}

	public Collection getCapabilities() {
		return capabilities;
	}

}
