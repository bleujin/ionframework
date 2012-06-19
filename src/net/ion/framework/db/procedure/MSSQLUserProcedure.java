package net.ion.framework.db.procedure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
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
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class MSSQLUserProcedure extends UserProcedure {

	private PreparedStatement pstmt = null;

	MSSQLUserProcedure(IDBController dc, String procSQL) {
		super(dc, procSQL);
	}

	public Statement getStatement() {
		return pstmt;
	}

	@Override
	public Rows myQuery(Connection conn) throws SQLException {

		Rows result = null;
		ResultSet rs = null;

		try {
			makeStatement(conn);
			rs = pstmt.executeQuery();
			result = super.populate(rs);
		} catch (IOException ex) {
			throw new SQLException(getExceptionMessage(ex, this));
		} catch (SQLException ex) {
			throw new SQLException(getExceptionMessage(ex, this), ex.getSQLState(), ex.getErrorCode());
		} finally {
			closeSilence(rs, pstmt, conn);
		}
		return result;
	}

	private void makeStatement(Connection conn) throws SQLException, IOException, FileNotFoundException {
		String procSQL = (getProcSQL().replace('(', ' ')).replace(')', ' ');
		conn.setAutoCommit(false);
		pstmt = conn.prepareStatement("Exec " + procSQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		setMaxRows(pstmt, getMaxRows());
		pstmt.setFetchSize(getFetchSize());

		MSSQLParamUtils.setCommandParam(pstmt, getParams(), getTypes());
	}

	@Override
	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException {
		ResultSet rs = null;
		try {
			makeStatement(conn);
			rs = pstmt.executeQuery();
			return handler.handle(rs);
		} catch (FileNotFoundException e) {
			throw new SQLException(e.getMessage());
		} catch (IOException e) {
			throw new SQLException(e.getMessage());
		} finally {
			closeSilence(rs, pstmt, conn);
		}
	}

	public int myUpdate(Connection conn) throws SQLException {
		int result = 0;
		try {
			pstmt = (CallableStatement) conn.prepareCall("{? = call " + getProcSQL() + "}");
			CallableStatement cstmt = (CallableStatement) pstmt;
			cstmt.registerOutParameter(1, Types.INTEGER);

			MSSQLParamUtils.setProcedureParam(cstmt, getParams(), getTypes());

			cstmt.executeUpdate();
			result = cstmt.getInt(1);

			cstmt.close();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			closeSilence(pstmt);
		}

		return result;
	}

}
