// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlReaderImpl.java

package net.ion.framework.db.rowset;

import java.io.Reader;
import java.sql.SQLException;

// Referenced classes of package sun.jdbc.rowset:
//            WebRowSet, XmlReader, XmlReaderDocHandler

public class XmlReaderImpl implements XmlReader {

	public XmlReaderImpl() {
	}

	public void readXML(WebRowSet webrowset, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
		// try {
		// InputSource inputsource = new InputSource(reader);
		// XmlReaderDocHandler xmlreaderdochandler = new XmlReaderDocHandler(webrowset);
		//
		// Resolver resolver = new Resolver();
		// resolver.registerCatalogEntry(WebRowSet.PUBLIC_DTD_ID, "file:RowSet.dtd");
		//
		// Parser parser = ParserFactory.makeParser();
		// parser.setDocumentHandler(xmlreaderdochandler);
		// parser.setEntityResolver(resolver);
		// parser.parse(inputsource);
		// } catch (SAXParseException saxparseexception) {
		// System.out.println("** Parsing error, line " + saxparseexception.getLineNumber() + ", uri " + saxparseexception.getSystemId());
		// System.out.println("   " + saxparseexception.getMessage());
		// saxparseexception.printStackTrace();
		// throw new SQLException(saxparseexception.getMessage());
		// } catch (SAXException saxexception) {
		// Object obj = saxexception;
		// if (saxexception.getException() != null)
		// obj = saxexception.getException();
		// ((Throwable) (obj)).printStackTrace();
		// throw new SQLException(((Throwable) (obj)).getMessage());
		// } catch (Throwable throwable) {
		// throw new SQLException("readXML: " + throwable.getMessage());
		// }
	}
}
