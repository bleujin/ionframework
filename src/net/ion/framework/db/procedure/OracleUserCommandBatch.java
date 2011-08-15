package net.ion.framework.db.procedure;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.ion.framework.db.IDBController;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

public class OracleUserCommandBatch extends UserCommandBatch {

	private OraclePreparedStatement pstmt = null;

	OracleUserCommandBatch(IDBController dc, String strSQL) {
		super(dc, strSQL);
	}

	public int myUpdate(Connection conn) throws SQLException {
		int updateCount;
		// oracle 9.2 이후 버전 부터 명시적으로 임시LOB를 해제할 필요가 있음.
		ArrayList<CLOB> clobList = new ArrayList<CLOB>();
		ArrayList<BLOB> blobList = new ArrayList<BLOB>();
		try {
			pstmt = (OraclePreparedStatement) conn.prepareStatement(getProcSQL());
			pstmt.clearBatch();

			pstmt.setExecuteBatch(100);
			setParams(conn, clobList, blobList);
			updateCount = pstmt.sendBatch();

			pstmt.close();
			pstmt = null;

		} catch (IOException ex) {
			throw new SQLException(ex.getMessage());
		} catch (SQLException ex) {
			throw ex;
		} finally {
			closeSilence(pstmt);

			// oracle 9.2 이후 버전 부터 명시적으로 임시LOB를 해제할 필요가 있음.(해제 시점이 중요함)
			OracleParamUtils.freeLOB(clobList, blobList);
		}
		return updateCount;
	}

	private void setParams(Connection conn, ArrayList<CLOB> clobList, ArrayList<BLOB> blobList) throws IOException, SQLException {
		Object[][] values = getParamsAsArray(); // [a][b] a : rows, b : column
		for (int row = 0, rlast = values.length > 0 ? values[0].length : 0; row < rlast; row++) {
			List<Object> list = new ArrayList<Object>();
			for (int col = 0, clast = values.length; col < clast; col++) {
				Object obj = values[col][row];
				list.add(obj);
			}
			OracleParamUtils.setParam(conn, pstmt, list, getTypes(), OracleParamUtils.COMMAND_LOC, clobList, blobList);
			pstmt.executeUpdate();
		}

	}

}
