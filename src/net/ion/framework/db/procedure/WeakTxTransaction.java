package net.ion.framework.db.procedure;

import java.sql.Connection;
import java.sql.SQLException;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.xa.Transactionable;

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
 * Company: I-ON Communications
 * </p>
 * 
 * @author wschoi
 * @version 1.0
 */
public class WeakTxTransaction implements Transactionable, WeakTxTransactionUpdater {
	private IUserProcedures upts;

	protected boolean dbmDestroyed;
	protected DBManager dbm = null;
	protected Connection connection = null;
	protected boolean commitFinished;
	protected int transStatus;

	public WeakTxTransaction(IDBController dc, String name, boolean dbmDestroyed) {
		this.dbm = dc.getDBManager();
		this.upts = new DBController(this.dbm).createUserProcedures(name);
		this.dbmDestroyed = dbmDestroyed;
	}

	public void add(Queryable obj) {
		upts.add(obj);
	}

	protected IUserProcedures getUserProcedures() {
		return upts;
	}

	public void init(int branchId) throws XAException {
		try {
			connection = dbm.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new XAException(e.getMessage());
		}
	}

	public void start() throws XAException {
		transStatus = XAResource.XA_RDONLY; // ������ �ȵ� ���� rollback�� �ȵǰ� �ϱ� ���ؼ�
	}

	public int perform() throws XAException {
		int proc = 0;
		try {
			proc = execUpdate(getUserProcedures());
			transStatus = XAResource.XA_OK;
		} catch (SQLException ex) {
			transStatus = XAResource.TMFAIL;
			throw new XAException(ex.getMessage());
		}

		return proc;
	}

	public void end() throws XAException {
	}

	public int prepare() throws XAException {
		return transStatus;
	}

	public void commit() throws XAException {
		try {
			connection.commit();
			commitFinished = true;
		} catch (SQLException e) {
			throw new XAException(e.getMessage());
		}
	}

	public void rollback() throws XAException {
		try {
			if (!commitFinished) {
				connection.rollback();
			}
		} catch (SQLException e) {
			throw new XAException(e.getMessage());
		}
	}

	public void close() throws XAException {
		try {
			dbm.freeConnection(connection);
			connection = null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new XAException(e.getMessage());
		}
	}

	// -----------------------------------------------------------------------------------
	public int execUpdate(Queryable queryable) throws SQLException {
		return queryable.myUpdate(connection);
	}

}
