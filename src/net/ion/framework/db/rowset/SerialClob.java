// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SerialClob.java

package net.ion.framework.db.rowset;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

class SerialClob implements Clob, Serializable, Cloneable {

	SerialClob(Clob clob) throws SQLException {
		len = clob.length();
		buf = new char[(int) len];
		int i = 0;
		int j = 0;
		BufferedReader bufferedreader = new BufferedReader(clob.getCharacterStream());
		try {
			do {
				i = bufferedreader.read(buf, j, (int) (len - (long) j));
				j += i;
			} while (i > 0);
		} catch (IOException ioexception) {
			throw new SQLException("SerialClob: " + ioexception.getMessage());
		}
	}

	public InputStream getAsciiStream() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Reader getCharacterStream() throws SQLException {
		return new CharArrayReader(buf);
	}

	public String getSubString(long l, int i) throws SQLException {
		if (l < 0L || (long) i > len || l + (long) i > len)
			throw new SQLException("Invalid Arguments");
		else
			return new String(buf, (int) l, i);
	}

	public long length() throws SQLException {
		return len;
	}

	public long position(String s, long l) throws SQLException {
		if (l < 0L || l > len || l + (long) s.length() > len)
			throw new SQLException("Invalid Arguments");
		char ac[] = s.toCharArray();
		int i = (int) (l - 1L);
		boolean flag = false;
		long l2 = ac.length;
		if (l < 1L || l > len)
			return -1L;
		while ((long) i < len) {
			int j = 0;
			long l1 = i + 1;
			while (ac[j++] == buf[i++])
				if ((long) j == l2)
					return l1;
		}
		return -1L;
	}

	public long position(Clob clob, long l) throws SQLException {
		throw new UnsupportedOperationException();
	}

	char buf[];
	long len;

	// jdk 1.5 higher..
	public OutputStream setAsciiStream(long pos) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public Writer setCharacterStream(long pos) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int setString(long pos, String str) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int setString(long pos, String str, int offset, int len) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void truncate(long len) throws SQLException {
		throw new UnsupportedOperationException();
	}
}
