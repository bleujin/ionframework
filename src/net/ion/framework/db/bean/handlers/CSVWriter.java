package net.ion.framework.db.bean.handlers;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.lang.SystemUtils;

public class CSVWriter implements Closeable{

	private char colDiv = ',';
	private String lineSep = SystemUtils.LINE_SEPARATOR;
	private OutputStreamWriter writer = null;
	// private int nbrCols = 0;
	private int nbrRows = 0;

	public CSVWriter(File file, String encoding) throws IOException {
		this(file, ',', SystemUtils.LINE_SEPARATOR, encoding);
	}

	public CSVWriter(File file, char colDiv, String lineSep, String encoding) throws IOException {
		this.colDiv = colDiv;
		this.lineSep = lineSep;

		if (encoding == null) {
			encoding = SystemUtils.FILE_ENCODING;
		}

		FileOutputStream fout = new FileOutputStream(file);
		writer = new OutputStreamWriter(fout, encoding);
	}

	public void writeHeader(String[] header) throws IOException {
		// this.nbrCols = header.length;
		doWriteData(header);
	}

	public void writeData(String[] data) throws IOException {
		doWriteData(data);
	}

	public void close() throws IOException {
		this.writer.close();
	}

	private void doWriteData(String[] values) throws IOException {

		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				this.writer.write(colDiv);
			}

			if (values[i] != null) {
				this.writer.write("\"");
				this.writer.write(this.toCsvValue(values[i]));
				this.writer.write("\"");
			}
		}

		this.writer.write(lineSep);

		this.nbrRows++;
	}

	private String toCsvValue(String str) {

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			sb.append(c);

			switch (c) {
			case '"':
				sb.append('"');
				break;
			}
		}

		return sb.toString();
	}
}
