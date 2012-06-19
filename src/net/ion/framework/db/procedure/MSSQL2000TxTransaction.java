package net.ion.framework.db.procedure;

import java.sql.SQLException;

import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

import net.ion.framework.db.DBController;
import net.ion.framework.db.manager.MSSQLDBManager;
import net.ion.framework.util.StringUtil;

import com.microsoft.jdbcx.base.BaseXid;
import com.microsoft.jdbcx.sqlserver.SQLServerDataSource;

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

public class MSSQL2000TxTransaction extends TxTransaction {

	// Distributed transaction enabled data-sources

	// Only For Test
	MSSQL2000TxTransaction() {
		this(new MSSQLDBManager("jdbc:microsoft:sqlserver://db.i-on.net:1433;DatabaseName=pubs", "pubs", "pubs"), "test");
	};

	public MSSQL2000TxTransaction(MSSQLDBManager dbm, String name) {
		super(new DBController(dbm), name);
	}

	private Xid getXId(int branchId) throws XAException {
		// Global Transaction Id will be identical for all the branches under the
		// same distributed transaction. Here it is set to letter 'O'
		byte[] globalId = new byte[1];
		globalId[0] = (byte) 'O';

		// Transaction Branch Id is unique for each branch under the same
		// distributed transaction. Set the transaction id as the parameter passed.
		byte[] tempBranchId = new byte[1];
		tempBranchId[0] = (byte) branchId;

		byte[] globalTranId = new byte[64];
		byte[] branchQualifier = new byte[64];

		// Copy Global Transaction Id and Branch Id into a 64 byte array
		System.arraycopy(globalId, 0, globalTranId, 0, 1);
		System.arraycopy(tempBranchId, 0, branchQualifier, 0, 1);

		// Create the Transaction Id
		// Transaction Id has 3 components
		Xid xid = new BaseXid(0x1234, // Format identifier
				globalTranId, // Global transaction identifier
				branchQualifier); // Branch qualifier

		return xid;
	}

	public void init(int branchId) throws XAException {
		try {
			// Initialize the Datasource
			SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();

			// "jdbc:microsoft:sqlserver://bleujin:1433;DatabaseName=pubs;"
			String jdbcUrl = ((MSSQLDBManager) dbm).getJdbcURL().trim() + ";";
			String serverInfo = StringUtil.substring(jdbcUrl, "jdbc:microsoft:sqlserver://".length());

			String serverName = StringUtil.substringBefore(serverInfo, ":");
			int portNum = Integer.parseInt(StringUtil.substringBetween(serverInfo, ":", ";"));
			String dbName = StringUtil.substringBetween(serverInfo, ";DatabaseName=", ";");

			sqlServerDataSource.setServerName(serverName);
			sqlServerDataSource.setDatabaseName(dbName);
			sqlServerDataSource.setPortNumber(portNum);

			sqlServerDataSource.setUser(((MSSQLDBManager) dbm).getUserId());
			sqlServerDataSource.setPassword(((MSSQLDBManager) dbm).getUserPwd());
			sqlServerDataSource.setSelectMethod("cursor");

			xaDataSource = sqlServerDataSource;
			xaConnection = xaDataSource.getXAConnection();

			xaResource = xaConnection.getXAResource();
			connection = xaConnection.getConnection();
			connection.setAutoCommit(false);
			xId = getXId(branchId);
		} catch (XAException ex) {
			throw ex;
		} catch (SQLException ex) {
			throw new XAException(ex.getMessage());
		}
	}

}
