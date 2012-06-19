package net.ion.framework.db.procedure;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.ion.framework.db.IDBController;
import net.ion.framework.util.ListUtil; 
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

public class OracleUserCommandBatch extends UserCommandBatch {

	private OraclePreparedStatement pstmt = null;

	OracleUserCommandBatch(IDBController dc, String strSQL) {
		super(dc, strSQL);
	}

	public int myUpdate(Connection conn) throws SQLException {
		int updateCount;
		// oracle 9.2 ���� ���� ���� ��������� �ӽ�LOB�� ������ �ʿ䰡 ����.
		List<CLOB> clobList = ListUtil.newList() ;
		List<BLOB> blobList = ListUtil.newList() ;
		try {
			pstmt = (OraclePreparedStatement) conn.prepareStatement(getProcSQL());
			pstmt.clearBatch();

			pstmt.setExecuteBatch(100);
			setParams(conn, clobList, blobList);
			updateCount = pstmt.sendBatch();

		} catch (IOException ex) {
			throw new SQLException(ex.getMessage());
		} catch (SQLException ex) {
			throw ex;
		} finally {
			OracleParamUtils.freeLOB(clobList, blobList);
			closeSilence(pstmt);

			// oracle 9.2 ���� ���� ���� ��������� �ӽ�LOB�� ������ �ʿ䰡 ����.(���� ������ �߿���)
		}
		return updateCount;
	}

	private void setParams(Connection conn, List<CLOB> clobList, List<BLOB> blobList) throws IOException, SQLException {
		Object[][] values = getParamsAsArray(); // [a][b] a : rows, b : column
		for (int row = 0, rlast = values.length > 0 ? values[0].length : 0; row < rlast; row++) {
			List<Object> list = new ArrayList<Object>();
			for (int col = 0, clast = values.length; col < clast; col++) {
				Object obj = values[col][row];
				list.add(obj);
			}
			OracleParamUtils.setParam(conn, pstmt, list, getTypes(), OracleParamUtils.COMMAND_LOC, clobList, blobList);
			pstmt.executeUpdate();
		}

	}

}
