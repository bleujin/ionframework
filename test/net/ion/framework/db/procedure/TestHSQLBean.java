package net.ion.framework.db.procedure;

import java.sql.Connection;

import junit.framework.TestCase;
import net.ion.framework.db.DBController;
import net.ion.framework.db.IDBController;
import net.ion.framework.db.h2.H2EmbedPoolDBManager;
import net.ion.framework.db.hsql.HSQLBean;
import net.ion.framework.db.manager.DBManager;
import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.parse.gson.JsonParser;
import net.ion.framework.util.Debug;

public class TestHSQLBean extends TestCase{

	public void testJsonCreate() throws Exception {
		HSQLBean hb = new HSQLBean() ;
		hb.addProcedure(ProcedureBean.create("select", "select * from emp")) ;
		
		
		JsonObject jso = JsonParser.fromObject(hb).getAsJsonObject() ;
		Debug.line(jso) ;
		
		HSQLBean dhb = jso.getAsObject(HSQLBean.class) ;
		Debug.line(dhb.getProcedure("select")) ;
		
	}
	
	public void testParse() throws Exception {
		String config = "{address:'jdbc:h2:mem:test', name:'h2db', userId:'sa', userPwd:'sa', " +
				"procedures:{'emp@select':'select * from emp'} " +
				"}" ;
		HSQLBean hb = JsonParser.fromString(config).getAsJsonObject().getAsObject(HSQLBean.class) ;
		Debug.line(hb, hb.getProcedure("emp@select")) ;
	}
	
	public void testAction() throws Exception {
		String config = "{address:'jdbc:h2:mem:test', name:'h2db', userId:'sa', userPwd:'sa', " +
		"procedures:{'emp@select':'select * from emp', 'emp@createtable':'create table if not exists emp(empno int, ename varchar(40))', 'emp@insert(?,?)':'insert into emp values(:empno, :ename)'} " +
		"}" ;
		
		HSQLBean hb = JsonParser.fromString(config).getAsJsonObject().getAsObject(HSQLBean.class) ;
		DBManager dbm = new H2EmbedPoolDBManager(hb) ;
		
		IDBController dc = new DBController(dbm) ;
		dc.initSelf() ;
		
		//Connection c = dc.getDBManager().getConnection() ;
		
		int result = dc.createUserProcedure("emp@createtable").execUpdate() ;
		Debug.line(result) ;
		Debug.line(dc.createUserProcedure("emp@select").execQuery()) ;
		dc.createUserProcedure("emp@insert(?,?)").addParam("empno", 10).addParam("ename", "bleujin").execUpdate() ;
		Debug.line(dc.createUserProcedure("emp@select").execQuery()) ;
		
		dc.destroySelf() ;
		
	}
}
