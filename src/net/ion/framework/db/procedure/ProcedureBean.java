package net.ion.framework.db.procedure;

import net.ion.framework.util.StringUtil;

import org.apache.commons.lang.builder.ToStringBuilder;


public class ProcedureBean {
	private String id;
	private String cmd;

	public ProcedureBean() {
	}
	
	public static ProcedureBean create(String id, String cmd){
		if (StringUtil.isBlank(id)) throw new IllegalArgumentException("hsql procedure id is not null");

		ProcedureBean result = new ProcedureBean();
		result.setId(id) ;
		result.setCmd(cmd) ;
		return result ;
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

	
	public String toString(){
		return ToStringBuilder.reflectionToString(this) ;
	}
}