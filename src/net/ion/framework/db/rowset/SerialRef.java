// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SerialRef.java

package net.ion.framework.db.rowset;

import java.io.Serializable;
import java.sql.Ref;
import java.sql.SQLException;
import java.util.Map;

class SerialRef implements Ref, Serializable, Cloneable {

	SerialRef(Ref ref) throws SQLException {
		baseTypeName = new String(ref.getBaseTypeName());
	}

	public String getBaseTypeName() throws SQLException {
		return baseTypeName;
	}

	private String baseTypeName;

	// jdk 1.5 higher...
	public Object getObject() throws SQLException {
		return null;
	}

	public Object getObject(Map<String, Class<?>> map) throws SQLException {
		return null;
	}

	public void setObject(Object value) throws SQLException {
		
	}
}
