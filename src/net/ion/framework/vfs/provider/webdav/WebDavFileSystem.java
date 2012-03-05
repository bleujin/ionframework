package net.ion.framework.vfs.provider.webdav;

import java.util.Collection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileSystem;
import org.apache.commons.vfs2.provider.GenericFileName;

/**
 * A WebDAV file system.
 * 
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision: 1.6 $ $Date: 2012/02/20 01:21:49 $
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

	@Override
	protected FileObject createFile(AbstractFileName name) throws Exception {
		final GenericFileName fileName = (GenericFileName) name;
		return new WebdavFileObject(fileName, this);
	}
}
