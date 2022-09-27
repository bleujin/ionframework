package net.ion.framework.xml.convert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.codec.binary.Base64;

import net.ion.framework.util.StreamUtils;

public class IMGWriter {
	/**
	 * 
	 * 
	 * @param urlSrc
	 *            String
	 * @throws Exception
	 * @return String 
	 */
	public static String createIMG(String urlSrc) throws Exception {
		String result = "<null/>";
		try {
			File file = new File(urlSrc);

			FileInputStream fis = new FileInputStream(file);
			byte[] bit = StreamUtils.toByteArray(fis);
			result = Base64.encodeBase64String(bit) ;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
