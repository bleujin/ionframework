package net.ion.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.ion.framework.parse.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import junit.framework.TestCase;

public class TestHttpClient extends TestCase {

	public void testHttpClient() throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();

		HttpUriRequest request = new HttpGet("/index.html");
		HttpResponse response = client.execute(request);

		final InputStream input = response.getEntity().getContent();

		if (response.getStatusLine().getStatusCode() == 200) {
			Debug.line(IOUtil.toString(input));
		}

		input.close();
	}

	public void testRest() throws Exception {
		Rest rest = new Rest("http://www.i-on.net", Executors.newSingleThreadExecutor());
		rest.execute("index.htm", new JsonResponsHandler<Void>(){
			@Override
			public Void handle(JsonObject json) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

}

class Rest {
	private DefaultHttpClient client = new DefaultHttpClient();
	private ExceptionHandler ehandler = ExceptionHandler.DEFAULT;
	private ExecutorService es = Executors.newFixedThreadPool(3);
	private URL url;

	public Rest(String url, ExecutorService ex) throws MalformedURLException {
		this.url = new URL(url);
	}

	public <T> Future<T> execute(final String subPath, final ResponseHandler<T> handler) {
		return es.submit(new Callable<T>() {
			public T call() throws Exception {
				HttpResponse response = null;
				try {
					response = client.execute(new HttpGet(url.toString() + subPath));
					handler.handle(response);
				} catch (Throwable e) {
					ehandler.handle(e);
				} finally {
					try {
						final HttpEntity entity = response.getEntity();
						IOUtil.closeQuietly(entity.getContent());
					} catch (IOException ignore) {
						ehandler.handle(ignore);
					}
				}
				return null;
			}
		});

	}

}

interface ResponseHandler<T> {
	public T handle(HttpResponse response) throws IllegalStateException, IOException;
}

abstract class JsonResponsHandler<T> implements ResponseHandler<T>{
	public T handle(HttpResponse response) throws IllegalStateException, IOException{
		InputStream input = response.getEntity().getContent();
		return handle(JsonObject.fromString(IOUtil.toStringWithClose(input))) ;
	}
	
	public abstract T handle(JsonObject json) ;
	
}
