package net.ion.framework.db.procedure;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.ion.framework.db.IDBController;
import oracle.jdbc.driver.OracleCallableStatement;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

public class OracleUserProcedureBatch extends UserProcedureBatch {

	protected OracleCallableStatement cstmt = null;

	OracleUserProcedureBatch(IDBController dc, String procSQL) {
		super(dc, procSQL);
	}

	public Statement getStatement() {
		return cstmt;
	}

	public int myUpdate(Connection conn) throws SQLException {
		int[] updateRow;
		// oracle 9.2 이후 버전 부터 명시적으로 임시LOB를 해제할 필요가 있음.
		ArrayList<CLOB> clobList = new ArrayList<CLOB>();
		ArrayList<BLOB> blobList = new ArrayList<BLOB>();
		try {
			String procSQL = getProcSQL().replace('@', '.');
			cstmt = (OracleCallableStatement) conn.prepareCall("{call " + procSQL + "}");
			Object[][] values = getParamsAsArray(); // [a][b] a : rows, b : column

			for (int row = 0, rlast = values.length > 0 ? values[0].length : 0; row < rlast; row++) {
				List<Object> list = new ArrayList<Object>();
				for (int col = 0, clast = values.length; col < clast; col++) {
					Object obj = values[col][row];
					list.add(obj);
				}
				OracleParamUtils.setParam(conn, cstmt, list, getTypes(), OracleParamUtils.COMMAND_LOC, clobList, blobList);

				cstmt.addBatch();
			}

			updateRow = cstmt.executeBatch();
			cstmt.close();
		} catch (IOException ex) {
			throw new SQLException(ex.getMessage());
		} catch (SQLException ex) {
			throw ex;
		} finally {
			closeSilence(cstmt);
			// oracle 9.2 이후 버전 부터 명시적으로 임시LOB를 해제할 필요가 있음.(해제 시점이 중요함)
			OracleParamUtils.freeLOB(clobList, blobList);
		}

		return updateRow.length;
	}

}
