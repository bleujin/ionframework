package net.ion.framework.db.procedure;

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

public class QueryType {

	public final static int EMPTY = 0; // None Action

	public final static int USER_COMMAND = 1;
	public final static int USER_PROCEDURE = 2;
	public final static int USER_PROCEDURES = 3;
	public final static int USER_PROCEDURE_BATCH = 4;
	public final static int USER_COMMAND_BATCH = 5;

	public final static int CUSTOM_QUERY = 6;

	public QueryType() {
	}

}
