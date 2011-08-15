package net.ion.framework.db.servant;

import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.IQueryable;

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
 * @author not attributable
 * @version 1.0
 */

public class AfterTask {
	private long start;
	private long end;
	private DBManager dbm;
	private IQueryable query;
	private int execType;

	public AfterTask(long start, long end, DBManager dbm, IQueryable query, int execType) {
		this.start = start;
		this.end = end;
		this.dbm = dbm;
		this.query = query;
		this.execType = execType;
	}

	public DBManager getDBManager() {
		return dbm;
	}

	public long getEnd() {
		return end;
	}

	public IQueryable getQueryable() {
		return query;
	}

	public long getStart() {
		return start;
	}

	public int execType() {
		return execType;
	}

	public String toString() {
		return query.toString();
	}
}
