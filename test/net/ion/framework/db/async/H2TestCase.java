package net.ion.framework.db.async;

import java.sql.SQLException;

import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.procedure.H2EmbedPoolDBManager;
import net.ion.framework.db.servant.PrintOutServant;
import junit.framework.TestCase;

public class H2TestCase extends TestCase{
	
	protected IDBController dc ;
    protected AsyncDBController adc;
    
	protected void setUp() throws java.lang.Exception {
        super.setUp();
        this.dc = createDBController();
		this.adc = new AsyncDBController(dc) ;
    }

	private DBController createDBController() throws SQLException {
		final H2EmbedPoolDBManager dbm = H2EmbedPoolDBManager.test();
		
		final DBController dc = new DBController("testH2", dbm, new PrintOutServant());
		dc.initSelf() ;
		return dc ;
	}

    protected  void tearDown() throws java.lang.Exception {
        super.tearDown();
        adc.destroySelf() ;
    }

}
