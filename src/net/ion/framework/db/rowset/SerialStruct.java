// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SerialStruct.java

package net.ion.framework.db.rowset;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Map;
import java.util.Vector;

import net.ion.framework.util.Debug;

// Referenced classes of package sun.jdbc.rowset:
//            SQLOutputImpl, SerialArray, SerialBlob, SerialClob, 
//            SerialRef

class SerialStruct implements Struct, Serializable, Cloneable {

	SerialStruct(SQLData sqldata, Map map) throws SQLException {
		SQLTypeName = new String(sqldata.getSQLTypeName());
		Vector vector = new Vector();
		sqldata.writeSQL(new SQLOutputImpl(vector, map));
		attribs = vector.toArray();
		System.out.println("Dump: " + attribs[0] + "," + attribs[1]);
	}

	SerialStruct(Struct struct, Map map) throws SQLException {
		SQLTypeName = new String(struct.getSQLTypeName());
		Debug.debug("SQLTypeName: " + SQLTypeName);
		attribs = struct.getAttributes(map);
		mapToSerial(map);
	}

	public Object[] getAttributes() throws SQLException {
		return attribs;
	}

	public Object[] getAttributes(Map map) throws SQLException {
		return attribs;
	}

	public String getSQLTypeName() throws SQLException {
		return SQLTypeName;
	}

	private void mapToSerial(Map map) throws SQLException {
		for (int i = 0; i < attribs.length; i++)
			if (attribs[i] instanceof Struct)
				attribs[i] = new SerialStruct((Struct) attribs[i], map);
			else if (attribs[i] instanceof SQLData)
				attribs[i] = new SerialStruct((SQLData) attribs[i], map);
			else if (attribs[i] instanceof Blob)
				attribs[i] = new SerialBlob((Blob) attribs[i]);
			else if (attribs[i] instanceof Clob)
				attribs[i] = new SerialClob((Clob) attribs[i]);
			else if (attribs[i] instanceof Ref)
				attribs[i] = new SerialRef((Ref) attribs[i]);
			else if (attribs[i] instanceof Array)
				attribs[i] = new SerialArray((Array) attribs[i], map);

	}

	private String SQLTypeName;
	private Object attribs[];
}
