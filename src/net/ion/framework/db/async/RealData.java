package net.ion.framework.db.async;

import net.ion.framework.db.Rows;
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
 * @author bleujin
 * @version 1.0
 */

// Future Pattern.RealData

public class RealData implements Data {
	final Rows rows;
	final IQueryable query;

	public RealData(IQueryable query, Rows rows) {
		this.query = query;
		this.rows = rows;
	}

	public Rows getRows() {
		return rows;
	}

	public IQueryable getQuery() {
		return query;
	}

	public Throwable getException() {
		return null;
	}

}
