package net.ion.framework.db.procedure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.Rows;
import net.ion.framework.db.bean.ResultSetHandler;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.StringUtil;

public class H2ChainUserProcedure extends UserProcedure {

	private String procName;
	private String chainedSQL;
	private IUserProcedures chain;
	private List<Queryable> querys = ListUtil.newList() ;

	protected H2ChainUserProcedure(IDBController dc, String procName, String stmtSQL) {
		super(dc, procName);
		this.procName = procName;
		this.chainedSQL = stmtSQL;
		this.chain = makeChainProcedure(stmtSQL);

	}

	private IUserProcedures makeChainProcedure(String stmtSQL) {
		IUserProcedures chain = getDBController().createUserProcedures(procName);
		String[] queryStr = StringUtil.split(stmtSQL, ";");
		for (int i = 0; i < queryStr.length; i++) {
			if (StringUtil.isBlank(queryStr[i]))
				continue;
			H2UserProcedure cmd = (H2UserProcedure) getDBController().createParameterQuery(queryStr[i]);
			cmd.addNamedParam(this.getNamedParam());

			chain.add(cmd);
		}
		return chain;
	}

	@Override
	public Rows myQuery(Connection conn) throws SQLException {
		return chain.myQuery(conn);
	}

	@Override
	public Object myHandlerQuery(Connection conn, ResultSetHandler handler) throws SQLException {
		return chain.myHandlerQuery(conn, handler);
	}

	final String ParamPattern = "\\@\\{(\\w+)\\}";

	protected String getStmtSQL() {
		return transNamedSQL(this.chainedSQL);
	}

	public int myUpdate(Connection conn) throws SQLException {
		return chain.execUpdate();
	}

	public Queryable getQuery(int i) {
		return querys.get(i);
	}

	private void setParam(PreparedStatement modifyps, int i) throws SQLException {
		int mediateLoc = i + 1; // return + start zero...

		if (isNull(i)) {
			if (((Integer) getTypes().get(i)).intValue() == Types.INTEGER) {
				modifyps.setNull(mediateLoc, Types.INTEGER);
			} else if (((Integer) getTypes().get(i)).intValue() == Types.FLOAT) {
				modifyps.setNull(mediateLoc, Types.FLOAT);
			} else if (((Integer) getTypes().get(i)).intValue() == Types.DATE) {
				modifyps.setNull(mediateLoc, Types.DATE);
			} else {
				modifyps.setNull(mediateLoc, Types.VARCHAR);
			}
			// modifyps.setNull();
		} else {
			modifyps.setObject(mediateLoc, getParams().get(i));
		}
	}

	public Statement getStatement() {
		throw RepositoryException.throwIt(new UnsupportedOperationException());
	}

}
