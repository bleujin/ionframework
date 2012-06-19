// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WebRowSet.java

package net.ion.framework.db.rowset;

import java.io.Reader;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

// Referenced classes of package sun.jdbc.rowset:
//            CachedRowSet, XmlReader, XmlReaderImpl, XmlWriter, 
//            XmlWriterImpl

public class WebRowSet extends CachedRowSet {

	public static String PUBLIC_DTD_ID = "-//Sun Microsystems, Inc.//DTD RowSet//EN";
	public static String SYSTEM_ID = "http://java.sun.com/j2ee/dtds/RowSet.dtd";
	private transient XmlReader xmlReader;
	private transient XmlWriter xmlWriter;

	public WebRowSet() throws SQLException {
		xmlReader = new XmlReaderImpl();
		xmlWriter = new XmlWriterImpl();
	}

	public XmlReader getXmlReader() throws SQLException {
		return xmlReader;
	}

	public XmlWriter getXmlWriter() throws SQLException {
		return xmlWriter;
	}

	public void readXml(Reader reader) throws SQLException {
		XmlReader xmlreader = getXmlReader();
		if (xmlreader != null)
			xmlreader.readXML(this, reader);
		else
			throw new SQLException("Invalid reader");
	}

	public void setXmlReader(XmlReader xmlreader) throws SQLException {
		xmlReader = xmlreader;
	}

	public void setXmlWriter(XmlWriter xmlwriter) throws SQLException {
		xmlWriter = xmlwriter;
	}

	public void writeXml(Writer writer) throws SQLException {
		XmlWriter xmlwriter = getXmlWriter();
		if (xmlwriter != null)
			xmlwriter.writeXML(this, writer);
		else{
			System.err.println("Invalid writer");
		}
	}

	public static void writeXml(ResultSet resultset, Writer writer) throws SQLException {
		WebRowSet webrowset = new WebRowSet();
		webrowset.populate(resultset);
		webrowset.writeXml(writer);
	}

}
