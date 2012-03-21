package net.ion.framework.convert.test;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;
import net.ion.framework.util.Debug;

public class TestHttpParsing extends TestCase {

	public void testname() throws Exception {
		URL url = new URL("http://comic.naver.com/webtoon/weekday.nhn");
		Debug.debug(makeAbsolute(url, "/comic/common.css")) ;
		Debug.debug(makeAbsolute(url, "../comic/common.css")) ;
		Debug.debug(makeAbsolute(url, "./comic/common.css")) ;
		Debug.debug(makeAbsolute(url, "http://comic.naver.com/comic/common.css")) ;
	}

	private String makeAbsolute(URL url, String link) throws MalformedURLException {
		String protocol = url.getProtocol() ;
		
		if (link.startsWith(protocol + ":")) {
			return link ;
		} else if ( link.startsWith("/")) {
			return new URL(url.getProtocol(), url.getHost(), url.getPort(), link).toString() ;
		} else if (link.startsWith("..") || link.startsWith(".")) {
			return new URL(url, link).toString() ;
		} else {
			throw new MalformedURLException(url.toString() + ", " + link) ;
		}
	}

}
