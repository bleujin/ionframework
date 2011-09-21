package net.ion.framework.db.servant;

import java.sql.SQLException;

import net.ion.framework.db.DBController;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.IUserProcedure;

public class TraceOfLateProcServant implements IExtraServant {
	private int limitMilisecond;

	public TraceOfLateProcServant(int limitMilisecond) {
		this.limitMilisecond = limitMilisecond;
	}

	public void support(AfterTask atask) {
		if (isDealWith(atask)) {
			IQueryable query = atask.getQueryable();
			final String procName = query.getProcSQL();
			final String procFullName = query.getProcFullSQL();
			final int misecond = Integer.parseInt(String.valueOf(atask.getEnd()
					- atask.getStart()));
			final DBController dc = new DBController(atask.getDBManager());
			new Thread() {
				public void run() {
					try {
						IUserProcedure upt = dc
								.createUserProcedure("OnlyAdmin@addLongTimeProcedure(?,?,?)");
						upt.addParam(0, procName);
						upt.addParam(1,
								procFullName.length() > 1000 ? procFullName
										.substring(0, 1000) : procFullName);
						upt.addParam(2, misecond);
						upt.execUpdate();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			}.start();
		}
	}

	private boolean isDealWith(AfterTask atask) {
		return (atask.getEnd() - atask.getStart() > this.limitMilisecond);
	}
}
