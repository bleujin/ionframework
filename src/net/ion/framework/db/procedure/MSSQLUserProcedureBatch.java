package net.ion.framework.db.procedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.ion.framework.db.IDBController;

public class MSSQLUserProcedureBatch extends UserProcedureBatch {

	private CallableStatement cstmt = null;

	MSSQLUserProcedureBatch(IDBController dc, String procSQL) {
		super(dc, procSQL);
	}

	public Statement getStatement() {
		return cstmt;
	}

	public int myUpdate(Connection conn) throws SQLException {
		int[] updateRow;
		try {
			String procSQL = (getProcSQL().replace('(', ' ')).replace(')', ' ');

			cstmt = conn.prepareCall("Exec " + procSQL);
			Object[][] values = getParamsAsArray(); // [a][b] a : rows, b : column

			for (int row = 0, rlast = values.length > 0 ? values[0].length : 0; row < rlast; row++) {
				List<Object> paramList = new ArrayList<Object>();
				for (int col = 0, clast = values.length; col < clast; col++) {
					Object obj = values[col][row];
					paramList.add(obj);
				}
				MSSQLParamUtils.setCommandParam(cstmt, paramList, getTypes());
				cstmt.addBatch();
			}

			// for (int row = 0; row < rowCount; row++) {
			// Object[] rows = (Object[]) values.get(row);
			// for (int col = 0; col < colCount; col++) {
			// Object val =rows[col] ;
			// if (val == null) {
			// cstmt.setNull(col+1, getType(col));
			// } else {
			// cstmt.setObject(col+1, val);
			// }
			// }
			// cstmt.addBatch();
			// }
			updateRow = cstmt.executeBatch();
			cstmt.close();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			if (cstmt != null)
				cstmt.close();
		}
		return updateRow.length;
	}

}
