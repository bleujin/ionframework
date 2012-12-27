package net.ion.framework.db.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.procedure.RepositoryService;

/**
 * <p>
 * Title: Oracle Connection �� ����ϴ� Oracle�� �⺻ DBManager
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public class OracleDBManager extends DBManager {
	private final static RepositoryService service = RepositoryService.ORACLE;
	private static final String driverName = "oracle.jdbc.driver.OracleDriver";

	public OracleDBManager(String jdbcURL, String userId, String userPwd) {
		super(driverName, jdbcURL, userId, userPwd);
	}
	
	public final static OracleDBManager test(){
		// return new OracleDBManager("jdbc:oracle:thin:@dev-test.i-on.net:1521:devTest", "bleu", "redf") 
		return new OracleDBManager("jdbc:oracle:thin:@61.250.201.76:1521:TOONTALK", "toontalk", "toon0711") ;
	}

	public OracleDBManager(String jdbcURL, String user, String passwd, int connectLimit) {
		this(jdbcURL, user, passwd);
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(getJdbcURL(), getUserId(), getUserPwd());
	}

	public int getDBManagerType() {
		return DBType.OracleDBManager;
	}

	public String getDBType() {
		return DBType.OracleDBName;
	}

	protected void myDestroyPool() throws SQLException {
	}

	protected void myInitPool() throws SQLException {
		try {
			// Class.forName(this.driverName);
			Class.forName(driverName);
		} catch (ClassNotFoundException ex) {
			throw new SQLException("Can't Init Pool : " + ex.getMessage());
		} finally {
			// to resolve a rowset bug;
			// http://developer.java.sun.com/developer/bugParade/bugs/4625851.html
			// NumberFormatException
			System.setProperty("oracledatabasemetadata.get_lob_precision", "false");
		}
	}

	public RepositoryService getRepositoryService() {
		return service;
	}

	protected void heartbeatQuery(IDBController dc) throws SQLException {
		getRepositoryService().createUserCommand(dc, "select /* heartbeat query */ 1 from dual").execQuery();
	}
}

// Lob Insert or Update�ϴ� Function �ۼ� ����
// CREATE OR REPLACE FUNCTION SCOTT.Insert_Test_Clob5(
// clob_Id in out Number,
// clob_text1 in out CLOB,
// blob_img1 in out BLOB,
// clob_text2 in out CLOB,
// blob_img2 in out BLOB
// )return NUMBER
// IS
// v_rowid varchar(40) ;
// BEGIN
// Insert into clobtest(a, b, c, d, e) values(clob_Id, empty_clob(),
// empty_blob(), empty_clob(), empty_blob()) returning rowid into v_rowid ;
//
// SELECT B, C, D, E INTO clob_text1, blob_img1, clob_text2, blob_img2
// FROM clobtest
// WHERE rowid = v_rowid ;
//
// return 1;
// END;

// Code Sample
// UserProcedure upt = new UserProcedure( "Insert_Test_Clob5(?,?,?,?,?)" );
// upt.addParam( 0, 999);
// upt.addClob( 1, clob.toString() );
// upt.addBlob( 2, "c:\\odbcconf.log" );
// upt.addClob( 3, clob.toString() );
// upt.addBlob( 4, "c:\\odbcconf.log" );
// dc.execProcedureUpdate( upt )

// Blob blob = null ;
// if(rs.next()){
// blob = rs.getBlob(3) ;
// }
// InputStream from = blob.getBinaryStream();
// FileOutputStream to = new FileOutputStream("c:\\temp2.log");
//
// byte[] buffer = new byte[4096]; // buffer holding bytes to be transferred
// int nbytes = 0; // Number of bytes read
// while( (nbytes = from.read(buffer)) != -1 ) // Read from Blob stream
// to.write(buffer, 0, nbytes); // Write to file stream
// to.flush();
// to.close();
// from.close();

// } catch ( IOException ex ) {
// throw new SQLException(
// "Exception thrown in execProcedureQuery() method of OracleDBManager Class\n"
// + ex.toString());
