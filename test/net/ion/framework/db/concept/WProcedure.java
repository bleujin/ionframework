package net.ion.framework.db.concept;

import java.sql.Types;

import net.ion.framework.db.IDBController;
import net.ion.framework.db.Rows;

public class WProcedure {

	private IDBController dc;
	private String psql;

	public WProcedure(IDBController dc, String psql) {
		this.dc = dc ;
		this.psql = psql ;
	}

	public WProcedure param(String name, String value) {
		parameter(name, value, Types.VARCHAR) ;
		return this;
	}

	public WProcedure param(String name, int value) {
		parameter(name, value, Types.INTEGER) ;
		return this;
	}

	public WProcedure param(String name, boolean value) {
		parameter(name, value, Types.BOOLEAN) ;
		return this ;
	}

	private WProcedure parameter(String name, Object value, int type) {
		return this ;
	}

	public Rows execQuery() {
		return null ;
	}

	public int execUpdate() {
		return 0 ;
	}


}
