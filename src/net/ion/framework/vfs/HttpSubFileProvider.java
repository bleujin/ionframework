package net.ion.framework.vfs;

import net.ion.framework.util.StringUtil;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.http.HttpFileProvider;

public class HttpSubFileProvider extends HttpFileProvider {

	private String prefixDir = "http://localhost/";

	public void setPrefixDir(String prefixDir) {
		this.prefixDir = prefixDir;
	}

	public FileObject findFile(final FileObject baseFile, final String uri, final FileSystemOptions fileSystemOptions) throws FileSystemException {
		// Parse the URI
		String newUri = prefixDir + "/" + StringUtil.substringAfter(uri, ":/");
		final FileName name;
		try {
			name = parseUri(baseFile != null ? baseFile.getName() : null, newUri);
		} catch (FileSystemException exc) {
			throw new FileSystemException("vfs.provider/invalid-absolute-uri.error", uri, exc);
		}

		// Locate the file
		return findFile(name, fileSystemOptions);
	}
}