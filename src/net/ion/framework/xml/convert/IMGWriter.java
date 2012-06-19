package net.ion.framework.xml.convert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.ion.framework.util.StreamUtils;
import sun.misc.BASE64Encoder;

public class IMGWriter {
	/**
	 * 이미지를 BASE64인코딩하여 return
	 * 
	 * @param urlSrc
	 *            String
	 * @throws Exception
	 * @return String 이미지가 BASE64로 인코딩된 문자열
	 */
	public static String createIMG(String urlSrc) throws Exception {
		String result = "<null/>";
		try {
			File file = new File(urlSrc);

			FileInputStream fis = new FileInputStream(file);
			byte[] bit = StreamUtils.toByteArray(fis);

			BASE64Encoder encoder = new BASE64Encoder();
			result = encoder.encode(bit);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
