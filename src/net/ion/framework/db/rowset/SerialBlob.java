// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SerialBlob.java

package net.ion.framework.db.rowset;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.Blob;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

class SerialBlob implements Blob, Serializable, Cloneable {

	SerialBlob(Blob blob) throws SQLException {
		try {
			len = blob.length();
			// buf = blob.getBytes(0L, (int) len);
			buf = IOUtils.toByteArray(blob.getBinaryStream());
		} catch (IOException e) {
			throw new SQLException(e.getMessage());
		}
	}

	public InputStream getBinaryStream() throws SQLException {
		ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(buf);
		return bytearrayinputstream;
	}

	public byte[] getBytes(long l, int i) throws SQLException {
		if ((long) i > len)
			i = (int) len;
		if (l < 0L || (long) i - l < 0L) {
			throw new SQLException("Invalid Arguments");
		} else {
			Class class1 = buf.getClass();
			Class class2 = class1.getComponentType();
			Object obj = Array.newInstance(class2, (int) ((long) i - l));
			System.arraycopy(buf, (int) l, obj, 0, (int) ((long) i - l));
			return (byte[]) obj;
		}
	}

	public long length() throws SQLException {
		return len;
	}

	public long position(Blob blob, long l) throws SQLException {
		return position(blob.getBytes(0L, (int) blob.length()), l);
	}

	public long position(byte abyte0[], long l) throws SQLException {
		int i = (int) (l - 1L);
		boolean flag = false;
		long l2 = abyte0.length;
		if (l < 0L || l > len)
			return -1L;
		while ((long) i < len) {
			int j = 0;
			long l1 = i + 1;
			while (abyte0[j++] == buf[i++])
				if ((long) j == l2)
					return l1;
		}
		return -1L;
	}

	byte buf[];
	long len;

	// jdk 1.5 higher
	public OutputStream setBinaryStream(long pos) throws SQLException {
		return null;
	}

	public int setBytes(long pos, byte[] bytes) throws SQLException {
		return 0;
	}

	public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
		return 0;
	}

	public void truncate(long len) throws SQLException {
		
	}
}
