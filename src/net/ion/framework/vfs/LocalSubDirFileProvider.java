package net.ion.framework.vfs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;
import org.apache.commons.vfs2.provider.local.LocalFileName;
import org.apache.commons.vfs2.provider.local.LocalFileNameParser;
import org.apache.commons.vfs2.provider.local.LocalFileSystem;

// Referenced classes of package org.apache.commons.vfs.provider.local:
//            WindowsFileNameParser, GenericFileNameParser, LocalFileNameParser, LocalFileName, 
//            LocalFileSystem

public class LocalSubDirFileProvider extends AbstractOriginatingFileProvider {

	private String prefixDir = "./";
	private static final Collection capabilities;

	static {
		capabilities = Collections.unmodifiableCollection(Arrays.asList(new Capability[] { Capability.CREATE, Capability.DELETE, Capability.RENAME, Capability.GET_TYPE, Capability.GET_LAST_MODIFIED, Capability.SET_LAST_MODIFIED_FILE, Capability.SET_LAST_MODIFIED_FOLDER, Capability.LIST_CHILDREN,
				Capability.READ_CONTENT, Capability.URI, Capability.WRITE_CONTENT, Capability.APPEND_CONTENT, Capability.RANDOM_ACCESS_READ, Capability.RANDOM_ACCESS_WRITE}));
	}

	public LocalSubDirFileProvider() {
		setFileNameParser(new SubDirFileNameParser());
	}

	public void setPrefixDir(String prefixDir) {
		this.prefixDir = prefixDir;
	}

	public boolean isAbsoluteLocalName(String name) {
		return ((LocalFileNameParser) getFileNameParser()).isAbsoluteName(name);
	}

	protected FileSystem doCreateFileSystem(FileName name, FileSystemOptions fileSystemOptions) throws FileSystemException {

		LocalFileName rootName = (LocalFileName) name;
		return new LocalFileSystem(rootName, prefixDir, fileSystemOptions);
	}

	public Collection getCapabilities() {
		return capabilities;
	}

	public FileObject findFile(FileObject baseFile, String uri, FileSystemOptions fileSystemOptions) throws FileSystemException {
		FileName name;
		try {
			name = parseUri(baseFile == null ? null : baseFile.getName(), uri);
			name.getPath();
		} catch (FileSystemException exc) {
			throw new FileSystemException("vfs.provider/invalid-absolute-uri.error", uri, exc);
		}
		return findFile(name, fileSystemOptions);
	}

	public FileName parseUri(FileName base, String uri) throws FileSystemException {
		if (getFileNameParser() != null)
			return getFileNameParser().parseUri(getContext(), base, uri);
		else
			throw new FileSystemException("vfs.provider/filename-parser-missing.error");
	}

}

class SubDirFileNameParser extends LocalFileNameParser {

	public SubDirFileNameParser() {
	}

	public static SubDirFileNameParser getInstance() {
		return INSTANCE;
	}

	protected String extractRootPrefix(String uri, StringBuilder name) throws FileSystemException {
		if (name.length() == 0 || name.charAt(0) != '/')
			throw new FileSystemException("vfs.provider.local/not-absolute-file-name.error", uri);
		else
			return "/";
	}

	protected FileName createFileName(String scheme, String rootFile, String path, FileType type) {
		return new SubDirFineName(scheme, "", path, type);
	}

	private static final SubDirFileNameParser INSTANCE = new SubDirFileNameParser();

}

class SubDirFineName extends LocalFileName {

	protected SubDirFineName(String scheme, String rootFile, String path, FileType type) {
		super(scheme, rootFile, path, type);
	}

	public String getRootFile() {
		return super.getRootFile();
	}

	public FileName createName(String path, FileType type) {
		return new SubDirFineName(getScheme(), getRootFile(), path, type);
	}

	protected void appendRootUri(StringBuilder buffer, boolean addPassword) {
		buffer.append(getScheme());
		buffer.append(":/");
		buffer.append(getRootFile());
	}


}
