package net.ion.framework.vfs.provider.webdav;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.webdav.lib.WebdavResource;

/**
 * Create a HttpClient instance
 * 
 * @author <a href="mailto:imario@apache.org">Mario Ivankovits</a>
 * @version $Revision: 1.5 $ $Date: 2012/02/19 02:26:17 $
 */
public class WebdavClientFactory {
	private WebdavClientFactory() {
	}

	/**
	 * Creates a new connection to the server.
	 */
	public static HttpClient createConnection(String hostname, int port, String username, String password, FileSystemOptions fileSystemOptions) throws FileSystemException {
		HttpClient client;
		try {
			final HttpURL url = new HttpURL(username, password, hostname, port, "/");

			// WebdavResource resource = null;
			WebdavResource resource = new WebdavResource() {
			};

			if (fileSystemOptions != null) {
				String proxyHost = WebdavFileSystemConfigBuilder.getInstance().getProxyHost(fileSystemOptions);
				int proxyPort = WebdavFileSystemConfigBuilder.getInstance().getProxyPort(fileSystemOptions);

				if (proxyHost != null && proxyPort > 0) {
					// resource = new WebdavResource(url, proxyHost, proxyPort);
					resource.setProxy(proxyHost, proxyPort);
				}
			}

			/*
			 * if (resource == null) { resource = new WebdavResource(url); } resource.setProperties(WebdavResource.NOACTION, 1);
			 */
			resource.setHttpURL(url, WebdavResource.NOACTION, 1);

			client = resource.retrieveSessionInstance();
		} catch (final IOException e) {
			throw new FileSystemException("vfs.provider.webdav/connect.error", hostname, e);
		}

		return client;
	}
}
