package net.ion.framework.db;

import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.IUserProcedures;
import net.ion.framework.db.servant.AfterTask;
import net.ion.framework.db.servant.IExtraServant;
import net.ion.framework.db.servant.ServantChain;



public class BatchDBController extends DBController { // implements
														// Configurable

	private ServantChain schain = null;

	public BatchDBController(String name, DBManager dbm) {
		this(name, dbm, IExtraServant.BLANK);
	}

	public BatchDBController(String name, DBManager dataBaseManager, IExtraServant servant) {
		super(name, dataBaseManager);
		this.schain = new ServantChain().addServant(servant);
	}

    protected void handleServant(long start, long end, IUserProcedures query, int execType) {
    	schain.support(new AfterTask(start, end, this.getDBManager(), query, execType));
    }

    public void handleServant(long start, long end, IQueryable query, int execType) {
    	schain.support(new AfterTask(start, end, this.getDBManager(), query, execType));
    }

}
