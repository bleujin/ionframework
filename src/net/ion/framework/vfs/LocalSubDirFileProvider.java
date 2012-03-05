package net.ion.framework.vfs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;
import org.apache.commons.vfs2.provider.local.GenericFileNameParser;
import org.apache.commons.vfs2.provider.local.LocalFileName;
import org.apache.commons.vfs2.provider.local.LocalFileNameParser;
import org.apache.commons.vfs2.provider.local.LocalFileSystem;

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
