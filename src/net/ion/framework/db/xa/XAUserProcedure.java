package net.ion.framework.db.xa;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

import net.ion.framework.db.Page;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.QueryType;

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

public class XAUserProcedure implements IQueryable {

	Transactionable[] transactionable = null;
	int[] transStatus = null;

	public XAUserProcedure(Transactionable[] transactionable) {
		this("XAUserProcedure", transactionable);
	}

	public XAUserProcedure(String name, Transactionable[] transactionable) {
		this.transactionable = transactionable;
		this.transStatus = new int[transactionable.length];
	}

	private int getTransSize() {
		return transStatus.length;
	}

	protected Statement getStatement() {
		throw new UnsupportedOperationException("Not Supported Operation");
	}

	public Object execHandlerQuery(ResultSetHandler rsh) {
		throw new java.lang.UnsupportedOperationException("This Method not yet implemented.");
	}

	public Rows execPageQuery() throws SQLException {
		throw new java.lang.UnsupportedOperationException("This Method not yet implemented.");
	}

	public int execUpdate() throws SQLException {
		try {
			init();
			start();
			perform();
			end();
			boolean doCommit = prepare();
			if (doCommit) {
				commit();
			} else {
				rollback();
			}
			close();
		} catch (SQLException ex) {
			rollback();
			throw ex;
		} catch (NullPointerException ex) {
			throw ex;
		} catch (Exception ex) {
			rollback();
			throw new SQLException(ex.getMessage());
		} finally {
			close();
		}
		return 1;
	}

	private void close() throws SQLException {
		for (int i = 0, last = getTransSize(); i < last; i++) {
			try {
				transactionable[i].close();
			} catch (XAException ex) {
				ex.printStackTrace();
			}
		}
	}

	private boolean prepare() throws XAException, SQLException {

		for (int i = 0, last = getTransSize(); i < last; i++) {
			transStatus[i] = transactionable[i].prepare();
		}

		boolean doCommit = true;

		for (int i = 0, last = transStatus.length; i < last; i++) {
			int currentStatus = transStatus[i];
			if ((currentStatus != XAResource.XA_RDONLY) && (currentStatus != XAResource.XA_OK)) {
				doCommit = false;
			}
		}

		return doCommit;
	}

	private void commit() throws XAException, SQLException {
		for (int i = 0, last = getTransSize(); i < last; i++) {
			if (transStatus[i] == XAResource.XA_OK)
				transactionable[i].commit();
		}
	}

	private void rollback() throws SQLException {
		try {
			for (int i = 0, last = getTransSize(); i < last; i++) {
				if (transStatus[i] != XAResource.XA_RDONLY)
					transactionable[i].rollback();
			}
		} catch (XAException e) {
			throw new SQLException(e.getMessage());
		}
	}

	private void init() throws SQLException, XAException, SQLException {
		for (int i = 0, last = getTransSize(); i < last; i++) {
			transactionable[i].init(i + 1);
		}
	}

	private void end() throws XAException, SQLException {
		for (int i = 0, last = getTransSize(); i < last; i++) {
			transactionable[i].end();
		}
	}

	private void perform() throws XAException, SQLException, XAException, SQLException, IOException {
		for (int i = 0, last = getTransSize(); i < last; i++) {
			transactionable[i].perform();
		}
	}

	private void start() throws XAException, SQLException {
		for (int i = 0, last = getTransSize(); i < last; i++) {
			transactionable[i].start();
		}
	}

	public void cancel() throws SQLException, InterruptedException {
		rollback(); // TODO ���൵�߿� �ߴ��� �����.... =��=
	}

	public Rows execQuery() throws SQLException {
		throw new UnsupportedOperationException("this type is only permitted execUpdate");
	}

	public String getProcFullSQL() {
		return Arrays.deepToString(transactionable);
	}

	public String getProcSQL() {
		return Arrays.deepToString(transactionable);
	}

	public int getQueryType() {
		return QueryType.USER_PROCEDURE;
	}

	public IQueryable setPage(Page page) {
		throw new UnsupportedOperationException("this type is only permitted execUpdate");
	}

	public Page getPage() {
		throw new UnsupportedOperationException("this type is only permitted execUpdate");
	}

	public String getDBType() {
		throw new UnsupportedOperationException("this type use multiful db");
	}
}
