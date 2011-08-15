package net.ion.framework.db.procedure;

import net.ion.framework.db.IDBController;
import net.ion.framework.util.StringUtil;

/**
 * <p>
 * Title: Procedure Query
 * </p>
 * <p>
 * Description: execProcedureQuery, Update를 실행하기 위한 Procedure Query를 만든다.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: I-ON Communications
 * </p>
 * 
 * @author bleujin
 * @version 1.0
 */

public abstract class UserProcedure extends ParameterQueryable implements IUserProcedure {
	protected UserProcedure(IDBController dc, String procSQL) {
		super(dc, procSQL, QueryType.USER_PROCEDURE);
	}

	UserProcedure(IDBController dc, String procSQL, int queryType) {
		super(dc, procSQL, queryType);
	}

	public String getProcName() {
		return StringUtil.deleteWhitespace(StringUtil.substringBefore(this.getProcSQL(), "("));
	}

	public String toString() {
		return getProcFullSQL();
	}

}
