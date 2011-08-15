package net.ion.framework.db.procedure;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.ion.framework.db.IDBController;

public class StdOracleUserCommandBatch extends UserCommandBatch {

	StdOracleUserCommandBatch(IDBController dc, String strSQL) {
		super(dc, strSQL);
	}

	protected int execUpdate(Connection conn) throws SQLException {
		int updateCount;
		try {
			pstmt = conn.prepareStatement(getProcSQL());
			pstmt.clearBatch();

			setParams(conn);
			updateCount = pstmt.executeBatch().length;

			pstmt.close();
			pstmt = null;

		} catch (IOException ex) {
			throw new SQLException(ex.getMessage());
		} catch (SQLException ex) {
			throw ex;
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return updateCount;
	}

	private void setParams(Connection conn) throws IOException, SQLException {
		Object[][] values = getParamsAsArray(); // [a][b] a : rows, b : column
		for (int row = 0, rlast = values.length > 0 ? values[0].length : 0; row < rlast; row++) {
			List<Object> list = new ArrayList<Object>();
			for (int col = 0, clast = values.length; col < clast; col++) {
				Object obj = values[col][row];
				list.add(obj);
			}
			StdOracleParamUtils.setParam(conn, pstmt, list, getTypes(), OracleParamUtils.COMMAND_LOC);
			pstmt.executeUpdate();
		}

	}

}
