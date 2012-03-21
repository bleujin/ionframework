package net.ion.framework.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
 
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Spider {

	public Reader getPageContent(String httpURL) throws HttpException, IOException {
		String content = IOUtils.toString(getInputStream(httpURL), "EUC-KR");
		return new StringReader(content);
	}

	public InputStream getInputStream(String httpURL) throws HttpException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(httpURL);
		try {
			HttpResponse response = httpclient.execute(httpget);

			InputStream input = response.getEntity().getContent() ;
			InputStream result = new ByteArrayInputStream(IOUtils.toByteArray(input));
			input.close();
			return result;

		} catch (Exception ex) {
			throw new HttpException(ex.getMessage(), ex);
		} finally {
			httpclient.getConnectionManager().shutdown() ;
		}
	}

	public String getString(String httpURL, String encode) throws HttpException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(httpURL);
		try {
			HttpResponse response = httpclient.execute(httpget);

			InputStream input = response.getEntity().getContent() ;
			StringWriter writer = new StringWriter();
			IOUtils.copy(input, writer, encode);
			input.close();
			return writer.toString();
		} catch (Exception ex) {
			throw new HttpException(ex.getMessage(), ex);
		} finally {
			httpclient.getConnectionManager().shutdown() ;
		}
	}

}
