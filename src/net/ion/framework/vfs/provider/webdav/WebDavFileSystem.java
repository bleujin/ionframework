package net.ion.framework.vfs.provider.webdav;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.apache.commons.vfs.provider.GenericFileName;
import org.apache.webdav.lib.WebdavFile;

import java.util.Collection;

/**
 * A WebDAV file system.
 * 
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision: 1.4 $ $Date: 2011/03/13 04:30:10 $
 */
public class WebDavFileSystem extends AbstractFileSystem implements FileSystem {
	private final HttpClient client;

	protected WebDavFileSystem(final GenericFileName rootName, final HttpClient client, final FileSystemOptions fileSystemOptions) {
		super(rootName, null, fileSystemOptions);

		this.client = client;
	}

	/**
	 * Adds the capabilities of this file system.
	 */
	protected void addCapabilities(final Collection caps) {
		caps.addAll(WebdavFileProvider.capabilities);
	}

	/**
	 * Returns the client for this file system.
	 */
	protected HttpClient getClient() throws FileSystemException {
		return client;
	}

	/**
	 * Creates a file object. This method is called only if the requested file is not cached.
	 */
	protected FileObject createFile(final FileName name) {
		final GenericFileName fileName = (GenericFileName) name;
		return new WebdavFileObject(fileName, this);
	}
}
