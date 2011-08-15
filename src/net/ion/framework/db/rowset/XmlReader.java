// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlReader.java

package net.ion.framework.db.rowset;

import java.io.Reader;
import java.sql.SQLException;

// Referenced classes of package sun.jdbc.rowset:
//            WebRowSet

public interface XmlReader {

	public abstract void readXML(WebRowSet webrowset, Reader reader) throws SQLException;
}
