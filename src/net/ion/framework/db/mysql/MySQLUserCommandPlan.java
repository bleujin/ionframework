package net.ion.framework.db.mysql;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.PlanPrintable;

public class MySQLUserCommandPlan implements PlanPrintable {

	private DBController dc;
	private IUserCommand cmd;

	public MySQLUserCommandPlan(IDBController dc, IUserCommand cmd) {
		this.dc = new DBController(dc.getDBManager()); // none servant
		this.cmd = cmd;
	}

	public void printPlan(OutputStream output) throws SQLException {
		Rows rows = execQuery();

		PrintWriter writer = new PrintWriter(new OutputStreamWriter(output));
		ResultSetMetaData meta = rows.getMetaData();
		int cols = meta.getColumnCount();

		for (int i = 1; i <= cols; i++) {
			writer.printf("%s\t\t", meta.getColumnName(i));
		}
		writer.println();

		while (rows.next()) {
			for (int i = 1; i <= cols; i++) {
				writer.printf("%s\t\t", rows.getString(i));
			}
			writer.println();
		}
		writer.flush();
		writer.close();
	}

	public Rows execQuery() throws SQLException {
		String sql = "explain " + cmd.getProcFullSQL();
		return dc.createUserCommand(sql).execQuery();
	}
}
