package net.ion.framework.db.procedure;

import java.io.OutputStream;
import java.sql.SQLException;

public interface PlanPrintable {
	public void printPlan(OutputStream output) throws SQLException;
}
