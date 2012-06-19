package net.ion.framework.db.procedure;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.fake.BlankConnection;
import net.ion.framework.util.Debug;
import net.ion.framework.util.StringUtil;

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

public abstract class UserCommand extends ParameterQueryable implements IUserCommand {

	protected PreparedStatement pstmt = null;

	protected UserCommand(IDBController dc, String strSQL) {
		this(dc, strSQL, QueryType.USER_COMMAND);
	}

	UserCommand(IDBController dc, String strSQL, int queryType) {
		super(dc, strSQL, queryType);
	}

	public Statement getStatement() {
		return pstmt;
	}

	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException {
		ResultSet rs = null;
		try {
			makeStatement(conn);
			rs = pstmt.executeQuery();
			rs.setFetchSize(DEFAULT_FETCHSIZE);
			Object result = handler.handle(rs);

			return result;
		} catch (SQLException ex) {
			throw new SQLException(getExceptionMessage(ex, this), ex.getSQLState(), ex.getErrorCode());
		} finally {
			closeSilence(rs, pstmt, conn);
		}

	}

	private void makeStatement(Connection conn) throws SQLException {
		
		pstmt = conn.prepareStatement(getProcSQL(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		setMaxRows(pstmt, getMaxRows());
		pstmt.setFetchSize(getFetchSize());
		
		// result.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
		MSSQLParamUtils.setCommandParam(pstmt, getParams(), getTypes());
	}

	public Rows myQuery(Connection conn) throws SQLException {
		ResultSet rs = null;
		try {
			makeStatement(conn);

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

	public void printPlan(OutputStream output) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public String[] getParamsAsString(int index) {
		Object obj = params.get(index);
		if (obj == null)
			return new String[] { "" };
		else
			return new String[] { String.valueOf(obj) };
	}

	public String getProcFullSQL() {
		String result = getProcSQL();
		for (int i = 0, last = params.size(); i < last; i++) {
			String value = params.get(i) == null ? "null" : "'" + params.get(i).toString().replaceAll("'", "''") + "'";
			result = StringUtil.replaceOnce(result, "?", value);
		}
		return result;
	}

}
