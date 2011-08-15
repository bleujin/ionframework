package net.ion.framework.db.procedure;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import net.ion.framework.db.IDBController;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class MSSQLUserCommand extends UserCommand {

	private PreparedStatement pstmt = null;

	MSSQLUserCommand(IDBController dc, String strSQL) {
		this(dc, strSQL, QueryType.USER_COMMAND);
	}

	MSSQLUserCommand(IDBController dc, String strSQL, int queryType) {
		super(dc, strSQL, queryType);
	}

	public Statement getStatement() {
		return pstmt;
	}

	public void printPlan(OutputStream output) throws SQLException {
		MSSQLUserCommandPlan plan = new MSSQLUserCommandPlan(getDBController(), this);
		plan.printPlan(output);
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
			if (pstmt != null)
				pstmt.close();
		}
		return updateCount;
	}
}
