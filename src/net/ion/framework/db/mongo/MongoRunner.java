package net.ion.framework.db.mongo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MongoRunner {
	
	public void run(String cmd, String absoluteConfigFileName) {
		try {

			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmd + " -f " + absoluteConfigFileName);
			// any error message?
			StreamOuter errorOuter = new StreamOuter(proc.getErrorStream(), "error");

			// any output?
			StreamOuter outputOuter = new StreamOuter(proc.getInputStream(), "mongo");

			// kick them off
			errorOuter.start();
			outputOuter.start();

			// any error???
			int exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}


class StreamOuter extends Thread {
	InputStream is;
	String type;

	StreamOuter(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				System.out.println(type + ">" + line);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
