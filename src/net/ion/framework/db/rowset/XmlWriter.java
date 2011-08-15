// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlWriter.java

package net.ion.framework.db.rowset;

import java.io.Writer;
import java.sql.SQLException;

public interface XmlWriter {

	public abstract void writeXML(WebRowSet webrowset, Writer writer) throws SQLException;
}
