package net.ion.framework.vfs.provider.webdav;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.vfs.Capability;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractOriginatingFileProvider;
import org.apache.commons.vfs.provider.GenericFileName;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * A provider for WebDAV.
 * 
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision: 1.3 $ $Date: 2011/03/13 04:30:10 $
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
