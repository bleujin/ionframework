package net.ion.framework.db.procedure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: i-on
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class HSQLUserProcedure extends UserProcedure {

	private PreparedStatement pstmt;

	private String stmtSQL;

	protected HSQLUserProcedure(IDBController dc, String procName, String stmtSQL) {
		super(dc, procName);
		this.stmtSQL = stmtSQL;
	}

	@Override
	public Rows myQuery(Connection conn) throws SQLException {
		Rows result = null;
		ResultSet rs = null;

		try {
			makeStatement(conn);
			rs = pstmt.executeQuery();
			result = super.populate(rs);
		} catch (Exception ex) {
			throw new SQLException(ex.getMessage());
		} finally {
			closeSilence(rs, pstmt, conn);
		}
		return result;
	}

	@Override
	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException {
		ResultSet rs = null;
		try {
			makeStatement(conn);
			rs = pstmt.executeQuery();
			return handler.handle(rs);
		} finally {
			closeSilence(rs, pstmt, conn);
		}
	}

	private void makeStatement(Connection conn) throws SQLException {
		pstmt = conn.prepareStatement(getStmtSQL(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		setMaxRows(pstmt, getMaxRows());
		pstmt.setFetchSize(getFetchSize());

		for (int i = 0; i < getParams().size(); i++) {
			setParam(pstmt, i);
		}
	}

	final String ParamPattern = "\\@\\{(\\w+)\\}";

	protected String getStmtSQL() {
		return transNamedSQL(this.stmtSQL);
	}

	public int myUpdate(Connection conn) throws SQLException {
		int result = 0;
		try {
			pstmt = conn.prepareCall(getStmtSQL());

			for (int i = 0; i < getParams().size(); i++) {
				setParam(pstmt, i);
			}

			result = pstmt.executeUpdate();

			pstmt.close();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			closeSilence(pstmt);
		}

		return result;
	}

	private void setParam(PreparedStatement modifyps, int i) throws SQLException {
		int mediateLoc = i + 1; // return + start zero...

		if (isNull(i)) {
			if (((Integer) getTypes().get(i)).intValue() == Types.INTEGER) {
				modifyps.setNull(mediateLoc, Types.INTEGER);
			} else if (((Integer) getTypes().get(i)).intValue() == Types.FLOAT) {
				modifyps.setNull(mediateLoc, Types.FLOAT);
			} else if (((Integer) getTypes().get(i)).intValue() == Types.DATE) {
				modifyps.setNull(mediateLoc, Types.DATE);
			} else {
				modifyps.setNull(mediateLoc, Types.VARCHAR);
			}
			// modifyps.setNull();
		} else {
			modifyps.setObject(mediateLoc, getParams().get(i));
		}
	}

	public Statement getStatement() {
		return pstmt;
	}

}
