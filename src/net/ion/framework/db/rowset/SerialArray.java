// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SerialArray.java

package net.ion.framework.db.rowset;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Map;

// Referenced classes of package sun.jdbc.rowset:
//            SerialBlob, SerialClob, SerialStruct

class SerialArray implements Array, Serializable, Cloneable {

	SerialArray(Array array, Map map) throws SQLException {
		elements = (Object[]) array.getArray(map);
		baseType = getBaseType();
		baseTypeName = getBaseTypeName();
		len = elements.length;
		switch (baseType) {
		default:
			break;

		case 2002:
			for (int i = 0; i < len; i++)
				elements[i] = new SerialStruct((Struct) elements[i], map);

			break;

		case 2003:
			for (int j = 0; j < len; j++)
				elements[j] = new SerialArray((Array) elements[j], map);

			break;

		case 2004:
			for (int k = 0; k < len; k++)
				elements[k] = new SerialBlob((Blob) elements[k]);

			break;

		case 2005:
			for (int l = 0; l < len; l++)
				elements[l] = new SerialClob((Clob) elements[l]);

			break;
		}
	}

	public Object getArray() throws SQLException {
		Object aobj[] = new Object[len];
		System.arraycopy(((Object) (elements)), 0, ((Object) (aobj)), 0, len);
		return ((Object) (aobj));
	}

	public Object getArray(long l, int i) throws SQLException {
		Object aobj[] = new Object[i];
		System.arraycopy(((Object) (elements)), (int) l, ((Object) (aobj)), 0, i);
		return ((Object) (aobj));
	}

	public Object getArray(long l, int i, Map map) throws SQLException {
		Object aobj[] = new Object[i];
		System.arraycopy(((Object) (elements)), (int) l, ((Object) (aobj)), 0, i);
		return ((Object) (aobj));
	}

	public Object getArray(Map map) throws SQLException {
		Object aobj[] = new Object[len];
		System.arraycopy(((Object) (elements)), 0, ((Object) (aobj)), 0, len);
		return ((Object) (aobj));
	}

	public int getBaseType() throws SQLException {
		return baseType;
	}

	public String getBaseTypeName() throws SQLException {
		return baseTypeName;
	}

	public ResultSet getResultSet() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public ResultSet getResultSet(long l, int i) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public ResultSet getResultSet(long l, int i, Map map) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public ResultSet getResultSet(Map map) throws SQLException {
		throw new UnsupportedOperationException();
	}

	private Object elements[];
	private int baseType;
	private String baseTypeName;
	private int len;
}
