package net.ion.framework.vfs.provider.webdav;

import org.apache.commons.vfs.provider.FileNameParser;
import org.apache.commons.vfs.provider.URLFileNameParser;

/**
 * Implementation for the webdav filesystem.
 * < p/>
 * Additionally encodes every character below space (' ')
 */
public class WebdavFileNameParser extends URLFileNameParser {
	private final static WebdavFileNameParser INSTANCE = new WebdavFileNameParser();

	public WebdavFileNameParser() {
		super(80);
	}

	public boolean encodeCharacter(char ch) {
		return super.encodeCharacter(ch) || ch < ' ';
	}

	public static FileNameParser getInstance() {
		return INSTANCE;
	}
}
