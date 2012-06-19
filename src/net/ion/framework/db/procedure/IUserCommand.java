package net.ion.framework.db.procedure;

import java.io.OutputStream;
import java.sql.SQLException;

public interface IUserCommand extends IParameterQueryable {

	public void printPlan(OutputStream output) throws SQLException;

}
