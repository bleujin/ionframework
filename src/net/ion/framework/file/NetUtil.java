package net.ion.framework.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import net.ion.framework.util.IOUtil;
import net.ion.framework.util.StringUtil;

public class NetUtil {

	public static String findMyPublicIp() throws IOException{
		InputStream in = null ;
		try {
			in = new URL("http://ip-echo.appspot.com/").openStream();
			return StringUtil.trim(IOUtil.toString(in));
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtil.closeQuietly(in);
		}
	}
	
	public static String findMyLocalIp() throws UnknownHostException{
		return InetAddress.getLocalHost().getHostAddress() ;
	}
	
	
	
}
