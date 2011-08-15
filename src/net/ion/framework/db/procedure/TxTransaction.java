package net.ion.framework.db.procedure;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

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
 * Company:
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public abstract class TxTransaction implements Transactionable {

	protected XADataSource xaDataSource = null;
	protected XAConnection xaConnection = null;
	protected XAResource xaResource = null;
	protected Connection connection = null;
	protected Xid xId = null;
	protected DBManager dbm = null;
	private IUserProcedures upts;

	TxTransaction(IDBController dc, String name) {
		this.dbm = dc.getDBManager();
		this.upts = new DBController(this.dbm).createUserProcedures(name);
	}

	protected DBManager getDBManager() {
		return this.dbm;
	}

	public abstract void init(int branchId) throws XAException;

	public void end() throws XAException {
		// End(xid,flags) disassociates the transaction branch from the
		// distributed
		// transaction. The flags can be either
		// XAResource.TMSUCCESS --> Transaction branch has succeeded
		// XAResource.TMFAIL --> Transaction branch has failed
		// XAResource.TMSUSPEND --> Suspend the transaction branch specified by
		// xid
		xaResource.end(xId, XAResource.TMSUCCESS);
	}

	public int perform() throws XAException {
		int proc = 0;
		try {
			proc = upts.myUpdate(connection);
		} catch (SQLException ex) {
			throw new XAException(ex.getMessage());
		}

		return proc;
	}

	public void start() throws XAException {
		// Start(xid,flags) associates a transaction branch with a distributed
		// transaction. The flags can be either
		// XAResource.TMNOFLAGS --> Start of new transaction branch
		// XAResource.TMJOIN --> Joins a transaction brach specified by xid
		// XAResource.TMRESUME --> Resumes a transaction branch specified by xid
		//
		// note: The commit() or rollback() or setAutoCommit() methods of the
		// connection instance should not be invoked until the distributed
		// transaction is either committed or rolled back by the resource
		// managers.
		xaResource.start(xId, XAResource.TMNOFLAGS);
	}

	public void commit() throws XAException {
		xaResource.commit(xId, false);
	}

	public void rollback() throws XAException {
		xaResource.rollback(xId);

	}

	public int prepare() throws XAException {
		return xaResource.prepare(xId);
	}

	public void close() throws XAException {
		try {
			if (connection != null)
				connection.close();
			if (xaConnection != null)
				xaConnection.close();
			connection = null;
			xaConnection = null;
		} catch (SQLException ex) {
			throw new XAException(ex.getMessage());
		}
	}

	public void add(Queryable obj) {
		upts.add(obj);
	}

	protected IUserProcedures getUserProcedures() {
		return upts;
	}

}
