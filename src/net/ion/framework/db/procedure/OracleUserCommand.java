package net.ion.framework.db.procedure;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import net.ion.framework.db.IDBController;
import net.ion.framework.util.ListUtil;
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
		// oracle 9.2 ���� ���� ���� ��������� �ӽ�LOB�� ������ �ʿ䰡 ����.
		List<CLOB> clobList = ListUtil.newList();
		List<BLOB> blobList = ListUtil.newList();
		try {
			pstmt = conn.prepareStatement(getProcSQL());
			OracleParamUtils.setParam(conn, pstmt, getParams(), getTypes(), OracleParamUtils.COMMAND_LOC, clobList, blobList);
			updateCount = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
		} catch (IOException ex) {
			throw new SQLException(ex.getMessage());
		} finally {
			OracleParamUtils.freeLOB(clobList, blobList);
			closeSilence(pstmt) ;
			// oracle 9.2 ���� ���� ���� ��������� �ӽ�LOB�� ������ �ʿ䰡 ����.(���� ������ �߿���)
		}
		return updateCount;
	}

}
