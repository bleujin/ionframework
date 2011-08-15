package net.ion.framework.db.servant;

import java.util.Calendar;
import java.util.List;

import net.ion.framework.db.procedure.IParameterQueryable;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.QueryType;

public class StdOutServant extends ExtraServant {
	private int showLevel;
	public final static int ONLY_USERPROCEDURE = 1;
	public final static int ONLY_USERCOMMAND = 2;
	public final static int ONLY_USERPROCEDUREBATCH = 4;
	public final static int ONLY_USERPROCEDURES = 8;
	public final static int ONLY_USERCOMMANDBATCH = 16;
	public final static int ONLY_CUSTOMQUERY = 32;
	public final static int All = 63;

	public StdOutServant() {
		this.showLevel = All;
	}

	public StdOutServant(int showLevel) {
		this.showLevel = showLevel;
	}

	protected void handle(AfterTask atask) {
		println(atask.getStart(), atask.getEnd(), atask.getQueryable());
	}

	private void println(long start, long end, IQueryable query) {

		String queryStr = query.toString();

		// ibr_12077
		if (queryStr.startsWith("CMSUSER@")) { // 비밀번호 노출을 방지하기 위해서..
			List<?> params = ((IParameterQueryable) query).getParams();
			if (params.size() > 0) {
				if (queryStr.startsWith("CMSUSER@logInBy")) {
					String userId = (String) params.get(0);
					queryStr = "CMSUSER@logInBy(" + userId + ")....\n\n";
				} else if (queryStr.startsWith("CMSUSER@updateWith")) {
					String userId = (String) params.get(0);
					queryStr = "CMSUSER@updateWith(" + userId + ")....\n\n";
				} else if (queryStr.startsWith("CMSUSER@createWith")) {
					String userId = (String) params.get(0);
					queryStr = "CMSUSER@createWith(" + userId + ")....\n\n";
				} else if (queryStr.startsWith("CMSUSER@registerWith")) {
					String userId = (String) params.get(0);
					queryStr = "CMSUSER@registerWith(" + userId + ")....\n\n";
				}
			}
		} else if (queryStr.startsWith("TRACE@usrLogin")) {
			List<?> params = ((IParameterQueryable) query).getParams();
			if (params.size() > 0) {
				String userId = (String) params.get(0);
				queryStr = "TRACE@usrLogin(" + userId + ")....\n\n";
			}
		}

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
		// Debug.debug(msg);
	}

	private String getTime() {
		return Calendar.getInstance().getTime().toString();
	}

	protected boolean isDealWith(AfterTask atask) {
		IQueryable query = atask.getQueryable();
		return (this.showLevel == StdOutServant.All)
				|| (((showLevel / 32) % 2 == 1 && query.getQueryType() == QueryType.CUSTOM_QUERY)
						|| ((showLevel / 16) % 2 == 1 && query.getQueryType() == QueryType.USER_COMMAND_BATCH)
						|| ((showLevel / 8) % 2 == 1 && query.getQueryType() == QueryType.USER_PROCEDURES)
						|| ((showLevel / 4) % 2 == 1 && query.getQueryType() == QueryType.USER_PROCEDURE_BATCH)
						|| ((showLevel / 2) % 2 == 1 && query.getQueryType() == QueryType.USER_COMMAND) || ((showLevel / 1) % 2 == 1 && query.getQueryType() == QueryType.USER_PROCEDURE));
	}

	public ExtraServant newCloneInstance() {
		return new StdOutServant(this.showLevel);
	}
}
