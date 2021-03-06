package net.ion.framework.db.procedure;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.util.ListUtil;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

public class OracleUserProcedure extends UserProcedure {

	protected OracleCallableStatement cstmt;

	OracleUserProcedure(IDBController dc, String procSQL) {
		super(dc, procSQL);
	}

	public Statement getStatement() {
		return this.cstmt;
	}

	public Rows myQuery(Connection conn) throws SQLException {
		Rows result = null;
		ResultSet rs = null;

		try {
			makeQueryStatement(conn);
			cstmt.executeQuery();
			rs = (ResultSet) cstmt.getObject(1);
			result = super.populate(rs);
		} catch (SQLException ex) {
			throw new SQLException(getExceptionMessage(ex, this), ex.getSQLState(), ex.getErrorCode());
		} finally {
			closeSilence(rs, cstmt, conn);
		}

		return result;
	}

	private void makeQueryStatement(Connection conn) throws SQLException {
		cstmt = (OracleCallableStatement) conn.prepareCall("{? = call " + getProcedureSQL() + "}", ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		setMaxRows(cstmt, getMaxRows());
		cstmt.setFetchSize(getFetchSize());

		cstmt.setRowPrefetch(getFetchSize());

		cstmt.registerOutParameter(1, OracleTypes.CURSOR);
		for (int i = 0; i < getParams().size(); i++) {
			setQueryParam(i, getParams().get(i));
		}
	}

	protected String getProcedureSQL() {
		String procSQL = getProcSQL().replace('@', '.');
		return procSQL;
	}

	public int myUpdate(Connection conn) throws SQLException {
		int updateRow = 0;
		// oracle 9.2 ���� ���� ���� ��������� �ӽ�LOB�� ������ �ʿ䰡 ����.
		List<CLOB> clobList = ListUtil.newList();
		List<BLOB> blobList = ListUtil.newList();
		try {

			cstmt = (OracleCallableStatement) conn.prepareCall("{? = call " + getProcedureSQL() + "}");
			cstmt.registerOutParameter(1, OracleTypes.NUMBER);
			OracleParamUtils.setParam(conn, cstmt, getParams(), getTypes(), OracleParamUtils.PROCEDURE_LOC, clobList, blobList);
			cstmt.executeUpdate();
			updateRow = cstmt.getInt(1);
		} catch (IOException ex) {
			throw new SQLException(ex.getMessage());
		} catch (SQLException ex) {
			throw new SQLException(getExceptionMessage(ex, this), ex.getSQLState(), ex.getErrorCode());
		} finally {
			OracleParamUtils.freeLOB(clobList, blobList);
			closeSilence(cstmt);
		}
		return updateRow;
	}

	@Override
	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException {
		ResultSet rs = null;
		try {
			makeQueryStatement(conn);
			cstmt.executeQuery();
			rs = (ResultSet) cstmt.getObject(1);
			return handler.handle(rs);
		} finally {
			closeSilence(rs, cstmt, conn);
		}
	}

	private void setQueryParam(int i, Object obj) throws SQLException {
		int mediateLoc = i + 2; // return + start zero...

		if (isNull(i)) {
			cstmt.setNull(mediateLoc, getType(i));
			// modifyps.setNull();
		} else {
			cstmt.setObject(mediateLoc, obj);
		}
	}

}
