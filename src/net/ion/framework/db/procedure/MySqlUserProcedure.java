package net.ion.framework.db.procedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;

public class MySqlUserProcedure extends UserProcedure {

	private CallableStatement cstmt;

	public MySqlUserProcedure(IDBController dc, String procSQL) {
		super(dc, procSQL);
	}

	public Statement getStatement() {
		return cstmt;
	}

	@Override
	public Rows myQuery(Connection conn) throws SQLException {
		Rows result = null;
		ResultSet rs = null;

		try {
			makeStatement(conn);
			rs = cstmt.executeQuery();
			result = super.populate(rs);
		} catch (SQLException ex) {
			throw new SQLException(getExceptionMessage(ex, this), ex.getSQLState(), ex.getErrorCode());
		} finally {
			closeSilence(rs, cstmt, conn);
		}

		return result;
	}

	@Override
	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException {
		ResultSet rs = null;
		try {
			makeStatement(conn);
			cstmt.executeQuery();
			rs = (ResultSet) cstmt.getObject(1);
			return handler.handle(rs);
		} finally {
			closeSilence(rs, cstmt, conn);
		}
	}

	private void makeStatement(Connection conn) throws SQLException {
		cstmt = conn.prepareCall("{ call " + getProcedureSQL() + "}", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		setMaxRows(cstmt, getMaxRows());
		cstmt.setFetchSize(getFetchSize());

		for (int i = 0; i < getParams().size(); i++) {
			setParam(i, getParams().get(i));
		}
	}

	protected String getProcedureSQL() {
		String procSQL = getProcSQL().replace('@', '_');
		return procSQL;
	}

	public int myUpdate(Connection conn) throws SQLException {
		int updateRow = 0;
		try {
			cstmt = conn.prepareCall("{? = call " + getProcedureSQL() + "}");

			for (int i = 0; i < getParams().size(); i++) {
				int mediateLoc = i + 1; // return + start zero...
				Object obj = getParams().get(i);

				if (isNull(i)) {
					cstmt.setNull(mediateLoc + 1, getType(i));
				} else {
					cstmt.setObject(mediateLoc + 1, obj);
				}
			}
			cstmt.registerOutParameter(1, Types.INTEGER);
			updateRow = cstmt.executeUpdate();
			updateRow = cstmt.getInt(1);
		} catch (SQLException ex) {
			throw new SQLException(getExceptionMessage(ex, this), ex.getSQLState(), ex.getErrorCode());
		} finally {
			if (cstmt != null)
				cstmt.close();
			cstmt = null;
		}
		return updateRow;
	}

	protected void setParam(int i, Object obj) throws SQLException {
		int mediateLoc = i + 1; // return + start zero...

		if (isNull(i)) {
			cstmt.setNull(mediateLoc, getType(i));
		} else {
			cstmt.setObject(mediateLoc, obj);
		}
	}
}
