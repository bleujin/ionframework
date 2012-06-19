package net.ion.framework.db.procedure;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.mongo.MongoParamUtils;

public class MongoUserCommand extends UserCommand {

	private static final int NOT_IMPL = 0 ;
	
	protected MongoUserCommand(IDBController dc, String strSQL) {
		super(dc, strSQL);
	}

	@Override
	public int myUpdate(Connection conn) throws SQLException {
		int updateCount;
		try {
			conn.setAutoCommit(true) ;
			pstmt = conn.prepareStatement(getProcSQL());
			
			MongoParamUtils.setParam(conn, pstmt, getParams(), getTypes(), MongoParamUtils.COMMAND_LOC);
			updateCount = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
		} catch (IOException ex) {
			throw new SQLException(ex.getMessage());
		} finally {
			if (pstmt != null)
				pstmt.close();
		}
		return updateCount;
	}
	
	public Rows myQuery(Connection conn) throws SQLException {
        ResultSet rs = null;
        try {
            makeStatement(conn);

            rs = pstmt.executeQuery();
            // rs.setFetchSize(DEFAULT_FETCHSIZE);

            Rows result = populate(rs) ;

            return result;
        } catch (SQLException ex) {
            throw new SQLException(getExceptionMessage(ex, this), ex.getSQLState(), ex.getErrorCode());
        } finally {
        	closeSilence(rs, pstmt, conn) ;
        }

    }


	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException{
        ResultSet rs = null;
        try {
            makeStatement(conn);
            rs = pstmt.executeQuery();
            rs.setFetchSize(DEFAULT_FETCHSIZE);
            Object result = handler.handle(rs) ;

            return result;
        } catch (SQLException ex) {
            throw new SQLException(getExceptionMessage(ex, this), ex.getSQLState(), ex.getErrorCode());
        } finally {
        	closeSilence(rs, pstmt, conn) ;
        }
	
    }
	
	private void makeStatement(Connection conn) throws SQLException {
		pstmt = conn.prepareStatement(getProcSQL(), NOT_IMPL, NOT_IMPL);
		setMaxRows(pstmt, getMaxRows()) ;
		pstmt.setFetchSize(getFetchSize()) ;

		// result.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
		MongoParamUtils.setCommandParam(pstmt, getParams(), getTypes()) ;
	}
}
