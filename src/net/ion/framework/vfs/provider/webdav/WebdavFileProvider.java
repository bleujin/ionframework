package net.ion.framework.vfs.provider.webdav;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;
import org.apache.commons.vfs2.provider.GenericFileName;

/**
 * A provider for WebDAV.
 * 
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision: 1.4 $ $Date: 2012/02/19 02:26:18 $
 */
public class WebdavFileProvider extends AbstractOriginatingFileProvider {
	protected final static Collection capabilities = Collections.unmodifiableCollection(Arrays.asList(new Capability[] { Capability.CREATE, Capability.DELETE, Capability.RENAME, Capability.GET_TYPE, Capability.LIST_CHILDREN,
			Capability.READ_CONTENT, Capability.URI, Capability.WRITE_CONTENT, Capability.GET_LAST_MODIFIED, Capability.ATTRIBUTES }));

	public WebdavFileProvider() {
		super();

		setFileNameParser(WebdavFileNameParser.getInstance());
	}

	/**
	 * Creates a filesystem.
	 */
	protected FileSystem doCreateFileSystem(final FileName name, final FileSystemOptions fileSystemOptions) throws FileSystemException {
		// Create the file system
		final GenericFileName rootName = (GenericFileName) name;

		HttpClient httpClient = WebdavClientFactory.createConnection(rootName.getHostName(), rootName.getPort(), rootName.getUserName(), rootName.getPassword(), fileSystemOptions);

		return new WebDavFileSystem(rootName, httpClient, fileSystemOptions);
	}

	public Collection getCapabilities() {
		return capabilities;
	}
}
