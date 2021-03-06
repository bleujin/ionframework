package net.ion.framework.db;

import junit.framework.TestCase;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.MSSQLDBManager;
import net.ion.framework.db.manager.OracleDBManager;
import net.ion.framework.db.servant.PrintOutServant;
import net.ion.framework.db.servant.StdOutServant;

public class DBTestCase extends TestCase {

    protected static DBController dc = null;

    public DBTestCase(){
    }

    public DBTestCase(String name){
        super(name) ;
    }

    protected void setUp() throws java.lang.Exception {
        super.setUp();
        DBManager dbm = OracleDBManager.test();
        //DBManager dbm = MSSQLDBManager.test() ;
        dc = new DBController("Default", dbm) ;
        dc.addServant(new PrintOutServant()) ;
        dc.initSelf() ;
    }

    protected  void tearDown() throws java.lang.Exception {
        super.tearDown();
        dc.destroySelf() ;
    }


    public void xtestDBManager() throws Exception{
        System.out.println(dc.getRows("select * from tabs where rownum < 2"));
    }



    protected void start(){
    }
    protected void end(String s){
    }

//    public void xtestMock() throws Exception {
//        HttpServletRequest request = getMockHttpRequest() ;
//        System.out.println("pathInfo="+request.getPathInfo());
//        System.out.println("protocol="+request.getServerPort());
//    }
}
