package net.ion.framework.db.procedure;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;

public class StdOracleUserCommand extends UserCommand {

	StdOracleUserCommand(IDBController dc, String strSQL) {
		this(dc, strSQL, QueryType.USER_COMMAND);
	}

	StdOracleUserCommand(IDBController dc, String strSQL, int queryType) {
		super(dc, strSQL, queryType);
	}

	public int myUpdate(Connection conn) throws SQLException {
		int updateCount;
		try {
			pstmt = conn.prepareStatement(getProcSQL());
			StdOracleParamUtils.setParam(conn, pstmt, getParams(), getTypes(), OracleParamUtils.COMMAND_LOC);
			updateCount = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
		} catch (IOException ex) {
			throw new SQLException(ex.getMessage());
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return updateCount;
	}

}
