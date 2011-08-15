package net.ion.framework.db.procedure;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.digester.Digester;
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

		Digester digester = new Digester();
		digester.setValidating(false);
		digester.setUseContextClassLoader(true);

		digester.addObjectCreate("hsql", HSQLBean.class);
		digester.addSetProperties("hsql/server");

		digester.addObjectCreate("hsql/procedures/procedure", ProcedureBean.class);
		digester.addSetProperties("hsql/procedures/procedure");
		digester.addBeanPropertySetter("hsql/procedures/procedure", "cmd");

		digester.addSetNext("hsql/procedures/procedure", "addProcedure");

		hbean = (HSQLBean) digester.parse(reader);
		isFinished = true;

		return hbean;
	}

}
