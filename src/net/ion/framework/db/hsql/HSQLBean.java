package net.ion.framework.db.hsql;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.ion.framework.db.procedure.ProcedureBean;
import net.ion.framework.util.ListUtil;
import net.ion.framework.util.MapUtil;

public class HSQLBean {
	private String address = "127.0.0.1";
	private String name = "";
	private String userId = "sa";
	private String userPwd = "";

	private Map<String, String> procedures = MapUtil.newMap() ;

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
		procedures.put(proc.getId(), proc.getCmd());
	}

	public ProcedureBean[] getProcedures() {
		List<ProcedureBean> result = ListUtil.newList() ; 
		for (Entry<String, String> entry: procedures.entrySet()) {
			result.add(ProcedureBean.create(entry.getKey(), entry.getValue())) ;
		}
		return result.toArray(new ProcedureBean[0]);
	}

	public ProcedureBean getProcedure(String id) {
		if (id == null || (! procedures.containsKey(id))) throw new IllegalArgumentException("not found procedure id");
		return  ProcedureBean.create(id, procedures.get(id));
	}
}
