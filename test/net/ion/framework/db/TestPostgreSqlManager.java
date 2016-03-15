package net.ion.framework.db;


import junit.framework.TestCase;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.PostSqlDBManager;
import net.ion.framework.db.servant.StdOutServant;

public class TestPostgreSqlManager extends TestCase {

    protected static DBController dc = null;

    public TestPostgreSqlManager(){
    }

    public TestPostgreSqlManager(String name){
        super(name) ;
    }

    protected void setUp() throws java.lang.Exception {
        super.setUp();
        DBManager dbm = new PostSqlDBManager("jdbc:postgresql://social.i-on.net:54321/postgres", "postgres", "") ;
        dc = new DBController("Default", dbm) ;
        dc.addServant(new StdOutServant()) ;
        dc.initSelf() ;
    }

    protected  void tearDown() throws java.lang.Exception {
        super.tearDown();
        dc.destroySelf() ;
    }


    public void testDBManager() throws Exception{
    	Rows rows = dc.getRows("select * from sample_tbl ");
    	while(rows.next()) {
    		System.out.println(rows.getString("seq"));
    		System.out.println(rows.getString("subject"));
    		System.out.println(rows.getString("regdate"));
    	}
        
    }



    protected void start(){
    }
    protected void end(String s){
    }

}