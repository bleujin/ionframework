package net.ion.framework.db.concept;

import net.ion.framework.db.IDBController;

public class WriteSession {

	private IDBController dc;

	WriteSession(IDBController dc){
		this.dc = dc ;
	}
	
	public WProcedure createProcedure(String psql) {
		return new WProcedure(dc, psql) ;
	}

	public WBatchProcedure createBatchProcedure(String psql) {
		return new WBatchProcedure(dc, psql);
	}
}
