package net.ion.framework.db.rowset;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.BitSet;

// Referenced classes of package sun.jdbc.rowset:
//            BaseRow

class Row extends BaseRow implements Serializable, Cloneable {

	private Object currentVals[];
	private BitSet colsChanged;
	private boolean deleted;
	private boolean updated;
	private boolean inserted;
	private int numCols;

	Row(int i) {
		super.origVals = new Object[i];
		currentVals = new Object[i];
		colsChanged = new BitSet(i);
		numCols = i;
	}

	Row(int i, Object aobj[]) {
		super.origVals = new Object[i];
		for (int j = 0; j < i; j++)
			super.origVals[j] = aobj[j];

		currentVals = new Object[i];
		colsChanged = new BitSet(i);
		numCols = i;
	}

	protected void clearDeleted() {
		deleted = false;
	}

	protected void clearInserted() {
		inserted = false;
	}

	protected void clearUpdated() {
		updated = false;
		for (int i = 0; i < numCols; i++) {
			currentVals[i] = null;
			colsChanged.clear(i);
		}

	}

	protected boolean getColUpdated(int i) {
		return colsChanged.get(i);
	}

	protected Object getColumnObject(int i) throws SQLException {
		if (getColUpdated(i - 1))
			return currentVals[i - 1];
		else
			return super.origVals[i - 1];
	}

	protected boolean getDeleted() {
		return deleted;
	}

	protected boolean getInserted() {
		return inserted;
	}

	protected boolean getUpdated() {
		return updated;
	}

	protected void initColumnObject(int i, Object obj) {
		super.origVals[i - 1] = obj;
	}

	protected void moveCurrentToOrig() {
		for (int i = 0; i < numCols; i++)
			if (getColUpdated(i)) {
				super.origVals[i] = currentVals[i];
				currentVals[i] = null;
				colsChanged.clear(i);
			}

		updated = false;
	}

	private void setColUpdated(int i) {
		colsChanged.set(i);
	}

	protected void setColumnObject(int i, Object obj) {
		currentVals[i - 1] = obj;
		setColUpdated(i - 1);
	}

	protected void setDeleted() {
		deleted = true;
	}

	protected void setInserted() {
		inserted = true;
	}

	protected void setUpdated() {
		for (int i = 0; i < numCols; i++)
			if (getColUpdated(i)) {
				updated = true;
				return;
			}

	}

}
