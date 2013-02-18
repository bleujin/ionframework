package net.ion.framework.db.rowset;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import javax.sql.RowSetMetaData;

import net.ion.framework.db.Rows;
import net.ion.framework.db.RowsImpl;
import net.ion.framework.db.ScreenInfo;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.Queryable;

public class ProxyRows extends RowsImpl implements Rows {
	private static final long serialVersionUID = -7948909263945927470L;
	
	private int pos = -1;
	private int maxrow;
	private final Vector<Row> cached;
	private final ResultSetMetaData metaData;
	private final Rows source;
	private ScreenInfo sinfo;

	private ProxyRows(Rows source, IQueryable query) throws SQLException {
		super((Queryable) query);
		this.source = source;
		CachedRowSet csource = (CachedRowSet) source;
		this.cached = csource.getCachedRows();
		this.metaData = csource.getMetaData();
		this.maxrow = cached.size();
		this.sinfo = source.getScreenInfo();

		setReader(csource.getReader());
		setWriter(csource.getWriter());
		setMetaData((RowSetMetaData) csource.getMetaData());
	}

	public final static ProxyRows create(Rows source, IQueryable query) throws SQLException {
		return new ProxyRows(source, query);
	}

	protected BaseRow getRow(int rowindex) {
		return (Row) cached.get(rowindex);
	}

	protected Vector<Row> getCachedRows() {
		return cached;
	}

	public ResultSetMetaData getMedaData() {
		return metaData;
	}

	public int getRowCount() {
		return maxrow;
	}

	public ScreenInfo getScreenInfo() {
		return this.sinfo;
	}

	protected Object getColumnObject(BaseRow row, int i) throws SQLException {
		checkIndex(i);
		checkCursor();
		Object result = row.getColumnObject(i);
		setLastValueNull(result == null);
		return result;
	}

	private void checkCursor() throws SQLException {
		if (isAfterLast() || isBeforeFirst())
			throw new SQLException("Invalid cursor position");
		else
			return;
	}

	private void checkIndex(int i) throws SQLException {
		if (i < 1 || i > metaData.getColumnCount())
			throw new SQLException("Invalid column index");
		else
			return;
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return metaData;
	}

	public boolean next() throws SQLException {
		return ++pos < maxrow;
	}

	public boolean isAfterLast() throws SQLException {
		return pos == maxrow;
	}

	public boolean isBeforeFirst() throws SQLException {
		return pos == -1;
	}

	public void beforeFirst() throws SQLException {
		pos = -1;
	}

	public void afterLast() throws SQLException {
		pos = maxrow;
	}

	public boolean isFirst() throws SQLException {
		return pos == 0;
	}

	public boolean isLast() throws SQLException {
		return pos == maxrow - 1;
	}

	public boolean first() throws SQLException {
		if (maxrow >= 0)
			pos = 0;
		return maxrow >= 0;
	}

	public int getRow() throws SQLException {
		return pos;
	}
	
	protected BaseRow getCurrentRow() {
		return this.getRow(pos);
	}

	public boolean absolute(int i) throws SQLException {
		if (i < maxrow)
			pos = i;
		return i < maxrow;
	}

	public boolean relative(int i) throws SQLException {
		if (getRow() + i < maxrow)
			pos += i;
		return getRow() + i < maxrow;
	}

	public boolean previous() {
		if (pos <= -1)
			return false;
		pos--;
		return true;
	}

	private int getRowIndex() throws SQLException {
		if (isAfterLast() || isBeforeFirst())
			throw new SQLException("Invalid cursor position");
		else {
			return pos;
		}
	}

	public Array getArray(int i) throws SQLException {
		return getArray(getRowIndex(), i);
	}

	public InputStream getAsciiStream(int i) throws SQLException {
		super.asciiStream = null;
		return getAsciiStream(getRowIndex(), i);
	}

	public BigDecimal getBigDecimal(int i) throws SQLException {
		return getBigDecimal(getRowIndex(), i);
	}

	public BigDecimal getBigDecimal(int i, int j) throws SQLException {
		return getBigDecimal(getRowIndex(), i, j);
	}

	public InputStream getBinaryStream(int i) throws SQLException {
		super.binaryStream = null;
		return getBinaryStream(getRowIndex(), i);
	}

	public Blob getBlob(int i) throws SQLException {
		return getBlob(getRowIndex(), i);
	}

	public boolean getBoolean(int i) throws SQLException {
		return getBoolean(getRowIndex(), i);
	}

	public byte getByte(int i) throws SQLException {
		return getByte(getRowIndex(), i);
	}

	public byte[] getBytes(int i) throws SQLException {
		return getBytes(getRowIndex(), i);
	}

	public Reader getCharacterStream(int i) throws SQLException {
		return getCharacterStream(getRowIndex(), i);
	}

	public Clob getClob(int i) throws SQLException {
		return getClob(getRowIndex(), i);
	}

	public Date getDate(int i) throws SQLException {
		return getDate(getRowIndex(), i);
	}

	public Date getDate(int i, Calendar calendar) throws SQLException {
		return getDate(getRowIndex(), i, calendar);
	}

	public double getDouble(int i) throws SQLException {
		return getDouble(getRowIndex(), i);
	}

	public float getFloat(int i) throws SQLException {
		return getFloat(getRowIndex(), i);
	}

	public int getInt(int i) throws SQLException {
		return getInt(getRowIndex(), i);
	}

	public long getLong(int i) throws SQLException {
		return getLong(getRowIndex(), i);
	}

	public Object getObject(int i) throws SQLException {
		try {
			return getObject(getRowIndex(), i, getTypeMap());
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw ex;
		}
	}

	public Object getObject(int i, Map map) throws SQLException {
		return getObject(getRowIndex(), i, map);
	}

	public Ref getRef(int i) throws SQLException {
		return getRef(getRowIndex(), i);
	}

	public short getShort(int i) throws SQLException {
		return getShort(getRowIndex(), i);
	}

	public String getString(int i) throws SQLException {
		return getString(getRowIndex(), i);
	}

	public Time getTime(int i) throws SQLException {
		return getTime(getRowIndex(), i);
	}

	public Time getTime(int i, Calendar calendar) throws SQLException {
		return getTime(getRowIndex(), i, calendar);
	}

	public Timestamp getTimestamp(int i) throws SQLException {
		return getTimestamp(getRowIndex(), i);
	}

	public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
		return getTimestamp(getRowIndex(), i, calendar);
	}

	public InputStream getUnicodeStream(int i) throws SQLException {
		super.unicodeStream = null;
		return getUnicodeStream(getRowIndex(), i);
	}

	public ProxyRows toClone(IQueryable query) throws SQLException {
		return new ProxyRows(this.source, query);
	}

}
