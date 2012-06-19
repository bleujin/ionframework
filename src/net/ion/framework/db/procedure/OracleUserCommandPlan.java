package net.ion.framework.db.procedure;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;

public class OracleUserCommandPlan implements PlanPrintable {

	private DBController dc;
	private IUserCommand cmd;

	public OracleUserCommandPlan(IDBController dc, IUserCommand cmd) {
		this.dc = new DBController(dc.getDBManager()); // none servant
		this.cmd = cmd;
	}

	public void printPlan(OutputStream output) throws SQLException {
		Rows rows = execQuery();

		PrintWriter writer = new PrintWriter(new OutputStreamWriter(output));
		while (rows.next()) {
			writer.printf("%s\t\t", rows.getString(1));
			writer.printf("%s", rows.getString(2));
			writer.println();
		}
		writer.flush();
		writer.close();
	}

	public Rows execQuery() throws SQLException {
		IQueryable cleanQuery = (IQueryable) dc.createParameterQuery("delete from plan_table");
		IQueryable aboutQuery = (IQueryable) dc.createParameterQuery("explain plan for " + cmd.getProcFullSQL());
		IQueryable planQuery = (IQueryable) dc
				.createUserCommand("select substr (lpad(' ', level-1) || operation || ' (' || options || ')',1,30 ) Operation, object_name Object "
						+ " from plan_table start with id = 0 connect by prior id=parent_id");

		ICombinedUserProcedures upts = dc.createCombinedUserProcedures("plan_view");
		upts.add(cleanQuery, "clean", IQueryable.UPDATE_COMMAND).add(aboutQuery, "about", IQueryable.UPDATE_COMMAND).add(planQuery, "plan",
				IQueryable.QUERY_COMMAND);

		upts.execUpdate();
		Map result = upts.getResultMap();
		return (Rows) result.get("plan");
	}

}
