package net.ion.framework.db.procedure;

// Oracle extensions
// Java Utility Classes
// Distributed transaction support
import java.sql.SQLException;

import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

import net.ion.framework.db.DBController;
import net.ion.framework.db.manager.OracleDBManager;
import oracle.jdbc.xa.OracleXid;
import oracle.jdbc.xa.client.OracleXADataSource;

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

public class OracleTxTransaction extends TxTransaction {

	// Only For Test
	OracleTxTransaction() {
		this(new OracleDBManager("jdbc:oracle:thin:@novision:1525:DB9I", "odin", "odin"), "test");
	};

	public OracleTxTransaction(OracleDBManager dbm, String name) {
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
		Xid xid = new OracleXid(0x1234, // Format identifier
				globalTranId, // Global transaction identifier
				branchQualifier); // Branch qualifier

		return xid;
	}

	public void init(int branchId) throws XAException {
		/*
		 * This code is required only when using Oracle9i JDBC driver v9.0.1. If the JDBC driver version is 9.0.2 or higher, this code can be removed.
		 */
		/*
		 * java.sql.DriverManager.registerDriver(new oracle.jdbc.OracleDriver()); Connection conn = java.sql.DriverManager.getConnection(getJdbcURL(),getUser(),getPasswd()); conn.close();
		 */
		/* End of code */

		try {
			// Initialize the Datasource
			OracleXADataSource oracleXADataSource = new OracleXADataSource();
			oracleXADataSource.setURL(((OracleDBManager) dbm).getJdbcURL());
			oracleXADataSource.setUser(((OracleDBManager) dbm).getUserId());
			oracleXADataSource.setPassword(((OracleDBManager) dbm).getUserPwd());
			// oracleXADataSource.setNativeXA(true); // Set the NativeXA property true, to support distributed transactions in Oracle8i releases prior to 8.1.6.

			xaDataSource = oracleXADataSource;

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
