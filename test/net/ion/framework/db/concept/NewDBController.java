package net.ion.framework.db.concept;

import java.sql.SQLException;

import net.ion.framework.configuration.Configuration;
import net.ion.framework.db.DBController;
import net.ion.framework.db.DBControllerInstantiationException;
import net.ion.framework.db.Rows;
import net.ion.framework.db.manager.DBManager;

public class NewDBController extends DBController {

	public NewDBController(DBManager dbm) {
		super(dbm);
	}

	public <T> T tran(TransactionJob<T> tjob) throws SQLException {
		WriteSession wsession = new WriteSession(this) ;
		try {
			beginTran() ;
			T result = tjob.handle(wsession) ;
			return result ;
		} catch (SQLException ex) {
			failTran(ex) ;
			throw ex ;
		} finally {
			endTran() ;
		}
		
	}

	private void failTran(SQLException ex) {
		
	}

	private void beginTran() {
		
	}
	
	private void endTran(){
		
	}
}
