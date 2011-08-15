package net.ion.framework.util.nio;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.DeferredFileOutputStream;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class TestIOSpeed extends TimerTestCase {

	public void testSpeed() throws Exception {
		start();
		copyIO("c:/temp/stest/article1.xml");
		end("copyIO");

		start();
		writeNIO("c:/temp/stest/article2.xml");
		end("copyNIO");

		start();
		writeByteIO("c:/temp/stest/article3.xml");
		end("byteIO");

	}

	private void copyIO(String filePath) throws Exception {
		File file = new File("c:/temp/stest/out1.bin");
		FileInputStream fis = new FileInputStream(filePath);
		FileOutputStream fos = new FileOutputStream(file);

		IOUtils.copy(fis, fos);
		fis.close();
		fos.close();
		// String str = FileUtils.readFileToString(file, "UTF-8") ;
	}

	private void writeNIO(String filePath) throws Exception {
		File file = new File("c:/temp/stest/out2.bin");
		FileInputStream fis = new FileInputStream(filePath);
		FileOutputStream fos = new FileOutputStream(file);
		NIOUtils.copyCompletely(fis, fos);

		fis.close();
		fos.close();
		// String str = FileUtils.readFileToString(file, "UTF-8") ;
	}

	private void writeByteIO(String filePath) throws Exception {
		File file = new File("c:/temp/stest/out3.bin");
		FileInputStream fis = new FileInputStream(filePath);
		DeferredFileOutputStream fos = new DeferredFileOutputStream(1500000, file);
		IOUtils.copy(fis, fos);
		fis.close();
		fos.close();

		InputStream is = null;
		if (fos.isInMemory())
			is = new ByteArrayInputStream(fos.getData());
		else
			is = new FileInputStream(file);

		is.close();
		// String str = IOUtils.toString(is, "UTF-8") ;
	}

}
