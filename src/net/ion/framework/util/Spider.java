package net.ion.framework.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

public class Spider {

	public Reader getPageContent(String httpURL) throws HttpException, IOException {
		String content = IOUtils.toString(getInputStream(httpURL), "EUC-KR");
		return new StringReader(content);
	}

	public InputStream getInputStream(String httpURL) throws HttpException, IOException {
		HttpClient httpclient = new HttpClient();
		GetMethod httpget = new GetMethod(httpURL);
		try {
			httpclient.executeMethod(httpget);

			InputStream input = httpget.getResponseBodyAsStream();
			InputStream result = new ByteArrayInputStream(IOUtils.toByteArray(input));
			input.close();
			return result;

		} catch (Exception ex) {
			throw new HttpException(ex.getMessage(), ex);
		} finally {
			httpget.releaseConnection();
		}
	}

	public String getString(String httpURL, String encode) throws HttpException {
		HttpClient httpclient = new HttpClient();
		GetMethod httpget = new GetMethod(httpURL);
		try {
			httpclient.executeMethod(httpget);

			InputStream input = httpget.getResponseBodyAsStream();
			StringWriter writer = new StringWriter();
			IOUtils.copy(input, writer, encode);
			input.close();
			return writer.toString();
		} catch (Exception ex) {
			throw new HttpException(ex.getMessage(), ex);
		} finally {
			httpget.releaseConnection();
		}
	}

}
