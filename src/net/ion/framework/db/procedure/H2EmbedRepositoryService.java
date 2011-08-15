package net.ion.framework.db.procedure;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.RepositoryException;
import net.ion.framework.util.StringUtil;

public class H2EmbedRepositoryService extends HSQLExtendRepositoryService {

	private HSQLBean bean;

	public H2EmbedRepositoryService(HSQLBean bean) {
		super(bean);
		this.bean = bean;
	}

	public UserCommand createUserCommand(IDBController dc, String sql) {
		return new H2UserCommand(dc, sql);
	}

	public UserProcedure createUserProcedure(IDBController dc, String procName) {
		String stmtSQL = getProcedureCmd(procName);
		if (StringUtil.contains(stmtSQL, ";")) {
			return new H2ChainUserProcedure(dc, procName, stmtSQL);
		} else
			return new H2UserProcedure(dc, procName, stmtSQL);
	}

	private String getProcedureCmd(String procName) {
		ProcedureBean proc = bean.getProcedure(procName);
		if (proc == null)
			throw RepositoryException.throwIt((new StringBuffer("Not found Procedure : ")).append(procName).toString());
		else
			return proc.getCmd();
	}

}
