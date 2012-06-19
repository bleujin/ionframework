package net.ion.framework.db.procedure;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ion.framework.db.AradonConnection;
import net.ion.framework.db.IDBController;
import net.ion.framework.util.StringUtil;

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

public abstract class RepositoryService {

	public final static RepositoryService ORACLE = new OracleRepositoryService();
	public final static RepositoryService MSSQL = new MSSQLRepositoryService();
	public final static RepositoryService HSQL = new HSQLRepositoryService();
	public final static RepositoryService MYSQL = new MySqlRepositoryService();

	final static String PROCEDURE_PATTERN = ".+\\@.+\\(.*[\\?\\,]*\\)";

	protected RepositoryService() {
	}

	public abstract IUserProcedureBatch createUserProcedureBatch(IDBController dc, String proc);

	public abstract IUserCommand createUserCommand(IDBController dc, String sql);

	public abstract IUserCommandBatch createUserCommandBatch(IDBController dc, String sql);

	public abstract IUserProcedure createUserProcedure(IDBController dc, String proc);

	public IUserProcedures createUserProcedures(IDBController dc, String name) {
		return new UserProcedures(dc, name);
	}

	public ICombinedUserProcedures createCombinedUserProcedures(IDBController dc, String name) {
		return new CombinedUserProcedures(dc, name);
	}

	public IParameterQueryable createParameterQuery(IDBController dc, String strSQL) {
		if (isProcedureType(strSQL))
			return createUserProcedure(dc, strSQL);
		else
			return createUserCommand(dc, strSQL);
	}

	public IBatchQueryable createBatchParameterQuery(IDBController dc, String strSQL) {
		if (isProcedureType(strSQL))
			return createUserProcedureBatch(dc, strSQL);
		else
			return createUserCommandBatch(dc, strSQL);
	}

	boolean isProcedureType(String strSQL) {
		Pattern pattern = Pattern.compile(PROCEDURE_PATTERN);
		Matcher matcher = pattern.matcher(StringUtil.deleteWhitespace(strSQL));

		return matcher.find(0);
	}

	public static RepositoryService makeService(Connection conn) throws SQLException {
		if ("ORACLE".equals(conn.getMetaData().getDatabaseProductName().toUpperCase())) {
			return ORACLE;
		} else {
			return MSSQL;
		}
	}

}
