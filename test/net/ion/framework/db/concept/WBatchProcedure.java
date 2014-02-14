package net.ion.framework.db.concept;

import java.sql.Types;

import net.ion.framework.db.IDBController;

public class WBatchProcedure {

	private IDBController dc;
	private String psql;

	public WBatchProcedure(IDBController dc, String psql) {
		this.dc = dc ;
		this.psql = psql ;
	}

	public WBatchProcedure param(String name, String value) {
		return parameter(name, value, Types.VARCHAR) ;
	}

	private WBatchProcedure parameter(String name, Object value, int type) {
		
		return this;
	}

	public int execUpdate() {
		return 0 ;
	}
	
	

}
