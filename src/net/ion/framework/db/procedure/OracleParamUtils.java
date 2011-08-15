package net.ion.framework.db.procedure;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;

@SuppressWarnings("deprecation")
public class OracleParamUtils {
	public static final int COMMAND_LOC = 1;
	public static final int PROCEDURE_LOC = 2;

	OracleParamUtils() {
	}

	static void setParam(Connection conn, PreparedStatement pstmt, List<Object> _param, List<Integer> _types, int mediateLoc, ArrayList<CLOB> clobList,
			ArrayList<BLOB> blobList) throws SQLException, IOException {
		Object[] params = _param.toArray(new Object[0]);
		int[] types = ArrayUtils.toPrimitive((Integer[]) _types.toArray(new Integer[0]));

		for (int i = 0, last = params.length; i < last; i++) {
			Object obj = params[i];
			int type = types[i];
			if (isNull(obj)) {
				pstmt.setNull(i + mediateLoc, types[i]);
			} else if (isClobType(type)) {
				Reader reader = (obj instanceof CharSequence) ? new StringReader(obj.toString()) : (Reader) obj;
				CLOB clob = CLOB.createTemporary(conn, true, CLOB.DURATION_SESSION);
				pstmt.setClob(i + mediateLoc, clob);
				Writer writer = clob.getCharacterOutputStream();
				IOUtils.copy(reader, writer);
				writer.flush();
				IOUtils.closeQuietly(writer);
				IOUtils.closeQuietly(reader);
				if (clobList != null) {
					clobList.add(clob);
				}
			} else if (isBlobType(type)) {
				InputStream is = (InputStream) obj;
				BLOB blob = BLOB.createTemporary(conn, true, BLOB.DURATION_SESSION);
				pstmt.setBlob(i + mediateLoc, blob);
				OutputStream os = new BufferedOutputStream(blob.getBinaryOutputStream());
				IOUtils.copy(is, os);
				os.flush();
				os.close();
				if (blobList != null) {
					blobList.add(blob);
				}
			} else {
				pstmt.setObject(i + mediateLoc, obj);
			}
		}
	}

	static void freeLOB(ArrayList<CLOB> clobList, ArrayList<BLOB> blobList) throws SQLException {
		if (clobList != null) {
			for (int i = 0; i < clobList.size(); i++) {
				CLOB clob = clobList.get(i);
				clob.freeTemporary();
			}
		}

		if (blobList != null) {
			for (int i = 0; i < blobList.size(); i++) {
				BLOB blob = blobList.get(i);
				blob.freeTemporary();
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

// 9204 이전 Lob Insert or Update하는 Function 작성 예제
// CREATE OR REPLACE FUNCTION SCOTT.Insert_Test_Clob5(
// clob_Id in out Number,
// clob_text1 in out CLOB,
// blob_img1 in out BLOB,
// clob_text2 in out CLOB,
// blob_img2 in out BLOB
// )return NUMBER
// IS
// v_rowid varchar(40) ;
// BEGIN
// Insert into clobtest(a, b, c, d, e) values(clob_Id, empty_clob(), empty_blob(), empty_clob(), empty_blob()) returning rowid into v_rowid ;
//
// SELECT B, C, D, E INTO clob_text1, blob_img1, clob_text2, blob_img2
// FROM clobtest
// WHERE rowid = v_rowid ;
//
// return 1;
// END;
// 단 Lob가 들어가는 배치는 할수가 없음...

// Code Sample
// UserProcedure upt = new UserProcedure( "Insert_Test_Clob5(?,?,?,?,?)" );
// upt.addParam( 0, 999);
// upt.addClob( 1, clob.toString() );
// upt.addBlob( 2, "c:\\odbcconf.log" );
// upt.addClob( 3, clob.toString() );
// upt.addBlob( 4, "c:\\odbcconf.log" );
// dc.execProcedureUpdate( upt )

