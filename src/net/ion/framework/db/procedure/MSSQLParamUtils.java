package net.ion.framework.db.procedure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import net.ion.framework.rope.Rope;
import net.ion.framework.rope.RopeBuilder;
import net.ion.framework.rope.RopeReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;

public class MSSQLParamUtils {

	private static final int COMMAND_LOC = 1;
	private static final int PROCEDURE_LOC = 2;

	MSSQLParamUtils() {

	}

	public static void setCommandParam(PreparedStatement pstmt, List params, List types) throws SQLException {
		setParam(pstmt, params, types, COMMAND_LOC);
	}

	public static void setProcedureParam(PreparedStatement pstmt, List params, List types) throws SQLException {
		setParam(pstmt, params, types, PROCEDURE_LOC);
	}

	private static void setParam(PreparedStatement pstmt, List<Object> _param, List<Integer> _types, int mediateLoc) throws SQLException {
		Object[] params = _param.toArray(new Object[0]);
		int[] types = ArrayUtils.toPrimitive((Integer[]) _types.toArray(new Integer[0]));

		for (int i = 0, last = params.length; i < last; i++) {
			Object obj = params[i];
			int type = types[i];

			if (isNull(obj)) {
				pstmt.setNull(i + mediateLoc, type);
			} else if (isBlobType(type) && (obj instanceof InputStream)) {
				InputStream input = (InputStream) obj;
				try {
					byte[] bytes = IOUtils.toByteArray(input);
					pstmt.setBinaryStream(i + mediateLoc, new ByteArrayInputStream(bytes), bytes.length);
					// pstmt.setBytes(i+1, bytes) ;
				} catch (IOException e) {
					throw new SQLException(e.getMessage());
				} finally {
					IOUtils.closeQuietly(input);
				}
			} else if (isClobType(type) && (obj instanceof Reader)) {
				Reader reader = null;
				try {
					reader = (Reader) obj;
					Rope rope = RopeBuilder.build(reader);
					pstmt.setCharacterStream(i + mediateLoc, new RopeReader(rope), rope.length());

				} catch (IOException e) {
					throw new SQLException(e.getMessage());
				} finally {
					IOUtils.closeQuietly(reader);
				}
			} else if (isClobType(type)) {
				CharSequence cseq = (CharSequence) obj;
				Rope rope = RopeBuilder.build(cseq);
				pstmt.setCharacterStream(i + mediateLoc, new RopeReader(rope), rope.length());
			} else {
				pstmt.setObject(i + mediateLoc, obj);
			}
		}
	}

	private static boolean isNull(Object obj) {
		return obj == null;
	}

	private static boolean isClobType(int type) {
		return Types.CLOB == type;
	}

	private static boolean isBlobType(int type) {
		return Types.BLOB == type;
	}

}
