package net.ion.framework.db.procedure;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;

public class MSSQLUserCommandPlan implements PlanPrintable {

	private DBController dc;
	private IUserCommand cmd;

	public MSSQLUserCommandPlan(IDBController dc, IUserCommand cmd) {
		this.dc = new DBController(dc.getDBManager()); // none servant
		this.cmd = cmd;
	}

	public void printPlan(OutputStream output) throws SQLException {
		Rows rows = execQuery();

		PrintWriter writer = new PrintWriter(new OutputStreamWriter(output));
		while (rows.next()) {
			writer.printf("%s", rows.getString("query_plan"));
			writer.println();
		}
		writer.flush();
		writer.close();
	}

	public Rows execQuery() throws SQLException {

		IUserProcedure planQuery = dc.createUserProcedure("framework@viewPlan(?)");
		planQuery.addParam(cmd.getProcFullSQL());

		ICombinedUserProcedures upts = dc.createCombinedUserProcedures("plan_view");
		upts.add((IQueryable) cmd, "about", IQueryable.QUERY_COMMAND).add((Queryable) planQuery, "plan", IQueryable.QUERY_COMMAND);

		upts.execUpdate();
		Map result = upts.getResultMap();
		return (Rows) result.get("plan");
	}

	// create Procedure framework@viewPlan
	// (@_startQuery varchar(1000))
	// as
	// begin
	// select top 1 cp.objtype,
	// object_name(st.objectid) as ObjectName,
	// cp.usecounts as executionCount,
	// st.text as QueryText,
	// qp.query_plan
	// from sys.dm_exec_cached_plans as cp
	// cross apply sys.dm_exec_query_plan(cp.plan_handle) as qp
	// cross apply sys.dm_exec_sql_text(cp.plan_handle) as st
	// where st.text = @_startQuery
	// end

	// sysadmin..

}
