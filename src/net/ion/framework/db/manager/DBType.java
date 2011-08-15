package net.ion.framework.db.manager;

/**
 * <p>
 * Title: 사용하는 DB의 타입을 받는 Const Value
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
public interface DBType {

	public final static String OracleDBName = "Oracle";
	public final static String MSSQLDBName = "MSSQL";
	public final static String HSQLDBName = "HSQL";
	public final static String MySqlDBName = "MYSQL";
	public final static String UnknownDBName = "Unknown";

	final static int OracleDBManager = 1;
	final static int OraclePoolDBManager = 11;
	final static int OracleCacheDBManager = 12;
	final static int Oracle9iCacheDBManager = 13;
	final static int OracleXADBManager = 14;
	public static final int OracleCacheReleaseDBManager = 15;

	final static int MSSQLDBManager = 2;
	final static int MSSQLPoolDBManager = 21;
	final static int MSSQLCacheDBManager = 22;

	public final static int DB2DBManager = 3;

	public final static int WASDBManager = 4;

	public final static int DataDirectDBManager = 5;

	public final static int HSQLDBManager = 6;

	public final static int MySQLDBManager = 7;
	public final static int MySQLPoolDBManager = 71;

	public final static int UNKNOWN = 9;

}
