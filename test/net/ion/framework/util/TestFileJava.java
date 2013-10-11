package net.ion.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.ecs.vxml.Throw;
import org.apache.ecs.xhtml.br;

import junit.framework.TestCase;

public class TestFileJava extends TestCase {

	private File file;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.file = new File("./resource/temp/hello.txt");
	}

	public void testHello() {
		try {
			FileOutputStream fout = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fout);
			writer.write("Hello");
			writer.close();
		} catch (Exception e) {
		}

		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		for (StackTraceElement s : stacks) {
			Debug.line(s);
		}
	}

	public void testRead() throws Exception {
		InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
		BufferedReader breader = new BufferedReader(reader);
		String readed = breader.readLine();
		assertEquals("Hello", readed);
	}

	public void testUtil() throws Exception {
		List<String> readed = IOUtil.readLines(new FileInputStream(file));
		assertEquals("Hello", readed.get(0));
	}

	public static void main(String[] args) throws Exception {
		new TestFileJava().testHello();
	}

	public void testManIO() throws Exception {

		IOSystem ios = new IOSystem();

		Integer result = ios.write("hello.txt", new WriteJob<Integer>() {
			public Integer write(Writer writer) throws IOException {
				writer.write("Hello");

//				throw new IllegalArgumentException();
				return 5 ;
			}
		});
		Debug.line(result);
	}
}

class IOSystem {

	private ExecutorService es = Executors.newFixedThreadPool(5);
	private ExceptionHandler ehandler = ExceptionHandler.DEFAULT;
	private String basePath = "./resource/temp/" ;
	public IOSystem exceptionHandler(ExceptionHandler ehandler) {
		this.ehandler = ehandler;
		return this;
	}

	public <T> T write(String path, WriteJob<T> writeJob) throws InterruptedException, ExecutionException {
		return writeAsync(path, writeJob).get();
	}

	public <T> Future<T> writeAsync(final String path, final WriteJob<T> writeJob) {
		return es.submit(new Callable<T>() {
			public T call() throws Exception {
				FileOutputStream fout = null;
				OutputStreamWriter writer = null;
				try {
					fout = new FileOutputStream(new File(basePath + path));
					writer = new OutputStreamWriter(fout);
					T result = writeJob.write(writer);
					return result;
				} catch (Throwable ex) {
					ehandler.handle(ex);
				} finally {
					IOUtil.closeQuietly(writer);
				}
				return null;
			}
		});
	}

}

interface ExceptionHandler {

	public final static ExceptionHandler DEFAULT = new ExceptionHandler() {

		public void handle(Throwable ex) {
			ex.printStackTrace();
		}

	};

	public void handle(Throwable ex);
}

interface WriteJob<T> {
	public T write(Writer writer) throws IOException;
}