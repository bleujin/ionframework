// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SQLOutputImpl.java

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
import java.sql.SQLOutput;
import java.sql.SQLXML;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Vector;

// Referenced classes of package sun.jdbc.rowset:
//            SerialArray, SerialBlob, SerialClob, SerialRef, 
//            SerialStruct

public class SQLOutputImpl implements SQLOutput {

	SQLOutputImpl(Vector vector, Map map1) {
		attribs = vector;
		map = map1;
	}

	public void writeArray(Array array) throws SQLException {
		if (array == null) {
			attribs.add(array);
			return;
		} else {
			attribs.add(new SerialArray(array, map));
			return;
		}
	}

	public void writeAsciiStream(InputStream inputstream) throws SQLException {
	}

	public void writeBigDecimal(BigDecimal bigdecimal) throws SQLException {
		attribs.add(bigdecimal);
	}

	public void writeBinaryStream(InputStream inputstream) throws SQLException {
	}

	public void writeBlob(Blob blob) throws SQLException {
		if (blob == null) {
			attribs.add(blob);
			return;
		} else {
			attribs.add(new SerialBlob(blob));
			return;
		}
	}

	public void writeBoolean(boolean flag) throws SQLException {
		attribs.add(new Boolean(flag));
	}

	public void writeByte(byte byte0) throws SQLException {
		attribs.add(new Byte(byte0));
	}

	public void writeBytes(byte abyte0[]) throws SQLException {
		attribs.add(abyte0);
	}

	public void writeCharacterStream(Reader reader) throws SQLException {
	}

	public void writeClob(Clob clob) throws SQLException {
		if (clob == null) {
			attribs.add(clob);
			return;
		} else {
			attribs.add(new SerialClob(clob));
			return;
		}
	}

	public void writeDate(Date date) throws SQLException {
		attribs.add(date);
	}

	public void writeDouble(double d) throws SQLException {
		attribs.add(new Double(d));
	}

	public void writeFloat(float f) throws SQLException {
		attribs.add(new Float(f));
	}

	public void writeInt(int i) throws SQLException {
		attribs.add(new Integer(i));
	}

	public void writeLong(long l) throws SQLException {
		attribs.add(new Long(l));
	}

	public void writeObject(SQLData sqldata) throws SQLException {
		if (sqldata == null) {
			attribs.add(sqldata);
			return;
		} else {
			attribs.add(new SerialStruct(sqldata, map));
			return;
		}
	}

	public void writeRef(Ref ref) throws SQLException {
		if (ref == null) {
			attribs.add(ref);
			return;
		} else {
			attribs.add(new SerialRef(ref));
			return;
		}
	}

	public void writeShort(short word0) throws SQLException {
		attribs.add(new Short(word0));
	}

	public void writeString(String s) throws SQLException {
		attribs.add(s);
	}

	public void writeStruct(Struct struct) throws SQLException {
		SerialStruct serialstruct = new SerialStruct(struct, map);
		attribs.add(serialstruct);
	}

	public void writeTime(Time time) throws SQLException {
		attribs.add(time);
	}

	public void writeTimestamp(Timestamp timestamp) throws SQLException {
		attribs.add(timestamp);
	}

	private Vector attribs;
	private Map map;

	// jdk 1.5 higher.
	public void writeURL(URL x) throws SQLException {

	}

	public void writeNString(String x) throws SQLException {

	}

	public void writeNClob(NClob x) throws SQLException {

	}

	public void writeRowId(RowId x) throws SQLException {

	}

	public void writeSQLXML(SQLXML x) throws SQLException {

	}
}
