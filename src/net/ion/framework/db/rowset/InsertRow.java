// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InsertRow.java

package net.ion.framework.db.rowset;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.BitSet;

import javax.sql.RowSetMetaData;

// Referenced classes of package sun.jdbc.rowset:
//            BaseRow

class InsertRow extends BaseRow implements Serializable, Cloneable {

	InsertRow(int i) {
		super.origVals = new Object[i];
		colsInserted = new BitSet(i);
		cols = i;
	}

	protected Object getColumnObject(int i) throws SQLException {
		if (!colsInserted.get(i - 1))
			throw new SQLException("No value has been inserted");
		else
			return super.origVals[i - 1];
	}

	protected void initInsertRow() {
		for (int i = 0; i < cols; i++)
			colsInserted.clear(i);

	}

	protected boolean isCompleteRow(RowSetMetaData rowsetmetadata) throws SQLException {
		for (int i = 0; i < cols; i++)
			if (!colsInserted.get(i) && rowsetmetadata.isNullable(i + 1) == 0)
				return false;

		return true;
	}

	protected void markColInserted(int i) {
		colsInserted.set(i);
	}

	protected void setColumnObject(int i, Object obj) {
		super.origVals[i - 1] = obj;
		markColInserted(i - 1);
	}

	private BitSet colsInserted;
	private int cols;
}
