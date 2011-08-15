// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BaseRow.java

package net.ion.framework.db.rowset;

import java.io.Serializable;
import java.sql.SQLException;

abstract class BaseRow implements Serializable, Cloneable {

	BaseRow() {
	}

	protected abstract Object getColumnObject(int i) throws SQLException;

	protected Object[] getOrigRow() {
		return origVals;
	}

	protected abstract void setColumnObject(int i, Object obj) throws SQLException;

	protected Object origVals[];
}
