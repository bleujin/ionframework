package net.ion.framework.db.procedure;

public class ProcedureBean {
	private String id;
	private String cmd;

	public ProcedureBean() {
	}

	public String getCmd() {
		return cmd;
	}

	public String getId() {
		return id;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public void setId(String id) {
		this.id = id;
	}

}