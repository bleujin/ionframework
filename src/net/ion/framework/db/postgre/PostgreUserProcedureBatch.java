package net.ion.framework.db.postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.procedure.UserProcedureBatch;

public class PostgreUserProcedureBatch extends UserProcedureBatch {
	private PreparedStatement pstmt;

	private String stmtSQL;

	public PostgreUserProcedureBatch(IDBController dc, String procName) {
		super(dc, procName);
		this.stmtSQL = procName;
	}

	public int myUpdate(Connection conn) throws SQLException {
		int updateCount;
		try {
			pstmt = conn.prepareStatement(getStmtSQL());
			pstmt.clearBatch();
			
			Object[][] values = getParamsAsArray(); // [a][b] a : rows, b : column
			for (int row = 0, rlast = values.length > 0 ? values[0].length : 0; row < rlast; row++) {
				for (int col = 0, clast = values.length; col < clast; col++) {
					Object obj = values[col][row];
					setParam(pstmt, col, obj);
				}
				pstmt.addBatch();
			}
			int[] count = pstmt.executeBatch();
			updateCount = count.length;

			pstmt.close();
			pstmt = null;

		} catch (SQLException ex) {
			throw ex;
		} finally {
			closeSilence(pstmt);
		}
		return updateCount;
	}

	protected void setParam(PreparedStatement pstmt, int i, Object obj) throws SQLException {
		if (isNull(i)) {
			pstmt.setNull(i + 1, getType(i));
		} else {
			pstmt.setObject(i + 1, obj);
		}
	}

	protected String getStmtSQL() {
		return transNamedSQL(this.stmtSQL);
	}

	public Statement getStatement() {
		return pstmt;
	}

}
