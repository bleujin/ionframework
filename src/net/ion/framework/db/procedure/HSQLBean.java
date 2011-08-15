package net.ion.framework.db.procedure;

import java.util.HashMap;

public class HSQLBean {
	private String address = "127.0.0.1";
	private String name = "";
	private String userId = "sa";
	private String userPwd = "";

	private HashMap<String, ProcedureBean> procedures = new HashMap<String, ProcedureBean>();

	public HSQLBean() {
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public void addProcedure(ProcedureBean proc) {
		if (proc.getId() == null) throw new IllegalArgumentException("hsql procedure id is not null");
		procedures.put(proc.getId().toUpperCase(), proc);
	}

	public ProcedureBean[] getProcedures() {
		return procedures.values().toArray(new ProcedureBean[0]);
	}

	public ProcedureBean getProcedure(String id) {
		if (id == null) throw new IllegalArgumentException("hsql procedure id is not null");
		return procedures.get(id.toUpperCase());
	}
}
