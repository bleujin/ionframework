package net.ion.framework.db.servant;

import java.util.Calendar;

import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.QueryType;

public class PrintOutServant implements IExtraServant {
	private int showLevel;
	public final static int ONLY_USERPROCEDURE = 1;
	public final static int ONLY_USERCOMMAND = 2;
	public final static int ONLY_USERPROCEDUREBATCH = 4;
	public final static int ONLY_USERPROCEDURES = 8;
	public final static int ONLY_USERCOMMANDBATCH = 16;
	public final static int ONLY_CUSTOMQUERY = 32;
	public final static int All = 63;

	public PrintOutServant() {
		this.showLevel = All;
	}

	public PrintOutServant(int showLevel) {
		this.showLevel = showLevel;
	}

	public void support(AfterTask atask) {
		if (isDealWith(atask)) {
			println(atask.getStart(), atask.getEnd(), atask.getQueryable());
		}
	}

	private void println(long start, long end, IQueryable query) {

		String queryStr = query.toString();

		StringBuilder msg = new StringBuilder("[" + getTime() + "]\n");
		msg.append(queryStr);
		msg.append("\n");
		long actual_time = (end - start);
		if (actual_time > 1000000) {
			msg.append("actual time(ms) : " + (int) (actual_time / 1000000) + "\n");
		} else {
			// System.out.println("actual time(ns) : " + actual_time + "\n");
			msg.append("actual time(ms) : 0\n");
		}
		System.out.println(msg);
	}

	private String getTime() {
		return Calendar.getInstance().getTime().toString();
	}

	private boolean isDealWith(AfterTask atask) {
		IQueryable query = atask.getQueryable();
		return (this.showLevel == StdOutServant.All)
				|| (((showLevel / 32) % 2 == 1 && query.getQueryType() == QueryType.CUSTOM_QUERY)
						|| ((showLevel / 16) % 2 == 1 && query.getQueryType() == QueryType.USER_COMMAND_BATCH)
						|| ((showLevel / 8) % 2 == 1 && query.getQueryType() == QueryType.USER_PROCEDURES)
						|| ((showLevel / 4) % 2 == 1 && query.getQueryType() == QueryType.USER_PROCEDURE_BATCH)
						|| ((showLevel / 2) % 2 == 1 && query.getQueryType() == QueryType.USER_COMMAND) || ((showLevel / 1) % 2 == 1 && query
						.getQueryType() == QueryType.USER_PROCEDURE));
	}

}
