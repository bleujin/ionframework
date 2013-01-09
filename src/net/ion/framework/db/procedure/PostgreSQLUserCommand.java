package net.ion.framework.db.procedure;

import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;

public class PostgreSQLUserCommand extends UserCommand {

	public PostgreSQLUserCommand(IDBController dc, String sql) {
		super(dc, sql);
	}

	public int myUpdate(Connection conn) throws SQLException {
		int updateCount;
		try {
			pstmt = conn.prepareStatement(getProcSQL());
			MSSQLParamUtils.setCommandParam(pstmt, getParams(), getTypes());
			updateCount = pstmt.executeUpdate();
		} finally {
			closeSilence(pstmt);
		}
		return updateCount;
	}

}