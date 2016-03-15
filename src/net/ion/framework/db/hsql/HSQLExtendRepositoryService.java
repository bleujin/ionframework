package net.ion.framework.db.hsql;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.procedure.HSQLRepositoryService;
import net.ion.framework.db.procedure.IUserProcedureBatch;
import net.ion.framework.db.procedure.ProcedureBean;
import net.ion.framework.db.procedure.UserProcedure;
import net.ion.framework.util.StringUtil;

public class HSQLExtendRepositoryService extends HSQLRepositoryService {

	private HSQLBean bean;

	public HSQLExtendRepositoryService(HSQLBean bean) {
		this.bean = bean;
	}

	public UserProcedure createUserProcedure(IDBController dc, String procName) {
		String stmtSQL = getProcedureCmd(procName);
		if (StringUtil.contains(stmtSQL, ";")) {
			return new HSQLChainUserProcedure(dc, procName, stmtSQL);
		} else
			return new HSQLUserProcedure(dc, procName, stmtSQL);
	}

	public IUserProcedureBatch createUserProcedureBatch(IDBController dc, String procName) {
		return new HSQLUserProcedureBatch(dc, procName, getProcedureCmd(procName));
	}

	private String getProcedureCmd(String procName) {
		ProcedureBean proc = bean.getProcedure(procName);
		if (proc == null) {
			throw RepositoryException.throwIt("Not found Procedure : " + procName);
		}
		return proc.getCmd();
	}

}
