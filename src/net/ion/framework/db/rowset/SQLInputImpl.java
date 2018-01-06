// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SQLInputImpl.java

package net.ion.framework.db.rowset;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLXML;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;

public class SQLInputImpl implements SQLInput {

	SQLInputImpl(Object aobj[], Map map1) {
		attrib = aobj;
		idx = -1;
		map = map1;
	}

	private Object getNextAttribute() throws SQLException {
		if (++idx >= attrib.length)
			throw new SQLException("Invalid read position");
		else
			return attrib[idx];
	}

	public Array readArray() throws SQLException {
		Array array = (Array) getNextAttribute();
		if (array == null) {
			lastValueWasNull = true;
			return null;
		} else {
			lastValueWasNull = false;
			return array;
		}
	}

	public InputStream readAsciiStream() throws SQLException {
		InputStream inputstream = (InputStream) getNextAttribute();
		if (inputstream == null) {
			lastValueWasNull = true;
			return null;
		} else {
			lastValueWasNull = false;
			return inputstream;
		}
	}

	public BigDecimal readBigDecimal() throws SQLException {
		BigDecimal bigdecimal = (BigDecimal) getNextAttribute();
		if (bigdecimal == null) {
			lastValueWasNull = true;
			return null;
		} else {
			lastValueWasNull = false;
			return bigdecimal;
		}
	}

	public InputStream readBinaryStream() throws SQLException {
		InputStream inputstream = (InputStream) getNextAttribute();
		if (inputstream == null) {
			lastValueWasNull = true;
			return null;
		} else {
			lastValueWasNull = false;
			return inputstream;
		}
	}

	public Blob readBlob() throws SQLException {
		Blob blob = (Blob) getNextAttribute();
		if (blob == null) {
			lastValueWasNull = true;
			return null;
		} else {
			lastValueWasNull = false;
			return blob;
		}
	}

	public boolean readBoolean() throws SQLException {
		Boolean boolean1 = (Boolean) getNextAttribute();
		if (boolean1 == null) {
			lastValueWasNull = true;
			return false;
		} else {
			lastValueWasNull = false;
			return boolean1.booleanValue();
		}
	}

	public byte readByte() throws SQLException {
		Byte byte1 = (Byte) getNextAttribute();
		if (byte1 == null) {
			lastValueWasNull = true;
			return 0;
		} else {
			lastValueWasNull = false;
			return byte1.byteValue();
		}
	}

	public byte[] readBytes() throws SQLException {
		byte abyte0[] = (byte[]) getNextAttribute();
		if (abyte0 == null) {
			lastValueWasNull = true;
			return null;
		} else {
			lastValueWasNull = false;
			return abyte0;
		}
	}

	public Reader readCharacterStream() throws SQLException {
		Reader reader = (Reader) getNextAttribute();
		if (reader == null) {
			lastValueWasNull = true;
			return null;
		} else {
			lastValueWasNull = false;
			return reader;
		}
	}

	public Clob readClob() throws SQLException {
		Clob clob = (Clob) getNextAttribute();
		if (clob == null) {
			lastValueWasNull = true;
			return null;
		} else {
			lastValueWasNull = false;
			return clob;
		}
	}

	public Date readDate() throws SQLException {
		Date date = (Date) getNextAttribute();
		if (date == null) {
			lastValueWasNull = true;
			return null;
		} else {
			lastValueWasNull = false;
			return date;
		}
	}

	public double readDouble() throws SQLException {
		Double double1 = (Double) getNextAttribute();
		if (double1 == null) {
			lastValueWasNull = true;
			return 0.0D;
		} else {
			lastValueWasNull = false;
			return double1.doubleValue();
		}
	}

	public float readFloat() throws SQLException {
		Float float1 = (Float) getNextAttribute();
		if (float1 == null) {
			lastValueWasNull = true;
			return 0.0F;
		} else {
			lastValueWasNull = false;
			return float1.floatValue();
		}
	}

	public int readInt() throws SQLException {
		Integer integer = (Integer) getNextAttribute();
		if (integer == null) {
			lastValueWasNull = true;
			return 0;
		} else {
			lastValueWasNull = false;
			return integer.intValue();
		}
	}

	public long readLong() throws SQLException {
		Long long1 = (Long) getNextAttribute();
		if (long1 == null) {
			lastValueWasNull = true;
			return 0L;
		} else {
			lastValueWasNull = false;
			return long1.longValue();
		}
	}

	public Object readObject() throws SQLException {
		Object obj = getNextAttribute();
		if (obj == null) {
			lastValueWasNull = true;
			return null;
		}
		lastValueWasNull = false;
		if (obj instanceof Struct) {
			Struct struct = (Struct) obj;
			Class class1 = (Class) map.get(struct.getSQLTypeName());
			if (class1 != null) {
				SQLData sqldata = null;
				try {
					sqldata = (SQLData) class1.newInstance();
				} catch (InstantiationException instantiationexception) {
					throw new SQLException("Unable to instantiate: " + instantiationexception.getMessage());
				} catch (IllegalAccessException illegalaccessexception) {
					throw new SQLException("Unable to instantiate: " + illegalaccessexception.getMessage());
				}
				Object aobj[] = struct.getAttributes(map);
				SQLInputImpl sqlinputimpl = new SQLInputImpl(aobj, map);
				sqldata.readSQL(sqlinputimpl, struct.getSQLTypeName());
				return sqldata;
			}
		}
		return obj;
	}

	public Ref readRef() throws SQLException {
		Ref ref = (Ref) getNextAttribute();
		if (ref == null) {
			lastValueWasNull = true;
			return null;
		} else {
			lastValueWasNull = false;
			return ref;
		}
	}

	public short readShort() throws SQLException {
		Short short1 = (Short) getNextAttribute();
		if (short1 == null) {
			lastValueWasNull = true;
			return 0;
		} else {
			lastValueWasNull = false;
			return short1.shortValue();
		}
	}

	public String readString() throws SQLException {
		String s = (String) getNextAttribute();
		if (s == null) {
			lastValueWasNull = true;
			return null;
		} else {
			lastValueWasNull = false;
			return s;
		}
	}

	public Time readTime() throws SQLException {
		Time time = (Time) getNextAttribute();
		if (time == null) {
			lastValueWasNull = true;
			return null;
		} else {
			lastValueWasNull = false;
			return time;
		}
	}

	public Timestamp readTimestamp() throws SQLException {
		Timestamp timestamp = (Timestamp) getNextAttribute();
		if (timestamp == null) {
			lastValueWasNull = true;
			return null;
		} else {
			lastValueWasNull = false;
			return timestamp;
		}
	}

	public boolean wasNull() throws SQLException {
		return lastValueWasNull;
	}

	private boolean lastValueWasNull;
	private int idx;
	private Object attrib[];
	private Map map;

	// jdk 1.5 higher..
	public URL readURL() throws SQLException {
		return null;
	}

	public NClob readNClob() throws SQLException {
		
		return null;
	}

	public String readNString() throws SQLException {
		
		return null;
	}

	public SQLXML readSQLXML() throws SQLException {
		
		return null;
	}

	public RowId readRowId() throws SQLException {
		
		return null;
	}
}
