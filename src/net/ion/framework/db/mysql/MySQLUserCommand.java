package net.ion.framework.db.mysql;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.MSSQLParamUtils;
import net.ion.framework.db.procedure.UserCommand;

public class MySQLUserCommand extends UserCommand {

	public MySQLUserCommand(IDBController dc, String sql) {
		super(dc, sql);
	}

	public void printPlan(OutputStream output) throws SQLException {
		MySQLUserCommandPlan plan = new MySQLUserCommandPlan(getDBController(), this);
		plan.printPlan(output);
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

	public Rows myQuery(Connection conn) throws SQLException {
		ResultSet rs = null;
		try {
			// in mssql.. access of row level cann't.. current use setMaxRows
			// String limit = "" ;
			// if (StringUtil.indexOf(getProcSQL().toLowerCase(), " limit ") < 0){
			// // limit = " limit " + getPage().getStartLoc() + ", " + getPage().getListNum() ;
			// }
			// pstmt = conn.prepareStatement(getProcSQL() + limit, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

			pstmt = conn.prepareStatement(getProcSQL(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			setMaxRows(pstmt, getMaxRows());
			// pstmt.setFetchSize(Integer.MIN_VALUE) ;

			// result.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
			MSSQLParamUtils.setCommandParam(pstmt, getParams(), getTypes());

			rs = pstmt.executeQuery();
			// rs.setFetchSize(DEFAULT_FETCHSIZE);

			Rows result = populate(rs);

			return result;
		} catch (SQLException ex) {
			throw new SQLException(getExceptionMessage(ex, this), ex.getSQLState(), ex.getErrorCode());
		} finally {
			closeSilence(rs, pstmt, conn);
		}
	}
}
