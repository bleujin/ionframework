package net.ion.framework.db.procedure;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import net.ion.framework.parse.gson.JsonParser;
import net.ion.framework.util.IOUtil;

import org.xml.sax.SAXException;

public class HSQLParser {

	private Reader reader;
	HSQLBean hbean;
	private boolean isFinished = false;

	public HSQLParser(Reader expression) {
		this.reader = expression;
	}

	// for test
	public HSQLParser(String fileName) throws FileNotFoundException {
		this.reader = new FileReader(fileName);
	}

	public HSQLBean getHQLBean() throws SAXException, IOException {

		if (isFinished && hbean != null)
			return hbean;
		
		hbean = JsonParser.fromString(IOUtil.toString(reader)).getAsJsonObject().getAsObject(HSQLBean.class) ;
		isFinished = true;

		return hbean;
	}

}
