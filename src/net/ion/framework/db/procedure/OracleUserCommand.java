package net.ion.framework.db.procedure;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import net.ion.framework.db.IDBController;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

public class OracleUserCommand extends UserCommand {

	OracleUserCommand(IDBController dc, String strSQL) {
		this(dc, strSQL, QueryType.USER_COMMAND);
	}

	OracleUserCommand(IDBController dc, String strSQL, int queryType) {
		super(dc, strSQL, queryType);
	}

	public void printPlan(OutputStream output) throws SQLException {
		OracleUserCommandPlan plan = new OracleUserCommandPlan(getDBController(), this);
		plan.printPlan(output);
	}

	public int myUpdate(Connection conn) throws SQLException {
		int updateCount;
		// oracle 9.2 이후 버전 부터 명시적으로 임시LOB를 해제할 필요가 있음.
		ArrayList<CLOB> clobList = new ArrayList<CLOB>();
		ArrayList<BLOB> blobList = new ArrayList<BLOB>();
		try {
			pstmt = conn.prepareStatement(getProcSQL());
			OracleParamUtils.setParam(conn, pstmt, getParams(), getTypes(), OracleParamUtils.COMMAND_LOC, clobList, blobList);
			updateCount = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
		} catch (IOException ex) {
			throw new SQLException(ex.getMessage());
		} finally {
			if (pstmt != null)
				pstmt.close();
			// oracle 9.2 이후 버전 부터 명시적으로 임시LOB를 해제할 필요가 있음.(해제 시점이 중요함)
			OracleParamUtils.freeLOB(clobList, blobList);
		}
		return updateCount;
	}

}
