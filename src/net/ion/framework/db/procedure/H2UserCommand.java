package net.ion.framework.db.procedure;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.ion.framework.db.IDBController;

public class H2UserCommand extends UserCommand {

	protected H2UserCommand(IDBController dc, String sql) {
		super(dc, sql);
	}

	public int myUpdate(Connection conn) throws SQLException {
		int updateCount;
		try {
			pstmt = conn.prepareStatement(getProcSQL());
			MSSQLParamUtils.setCommandParam(pstmt, getParams(), getTypes());
			updateCount = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
		} finally {
			closeSilence(pstmt);
		}
		return updateCount;
	}

	public Statement getStatement() {
		return pstmt;
	}

}
