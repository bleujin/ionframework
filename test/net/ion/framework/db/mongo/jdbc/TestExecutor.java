package net.ion.framework.db.mongo.jdbc;

import net.ion.framework.db.mongo.MongoTestBase;
import net.ion.framework.db.mongo.jdbc.Executor;
import net.ion.framework.util.Debug;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class TestExecutor extends MongoTestBase {

	
	public void testEqualAnd() throws Exception {
		Executor ex = new Executor(mdb, "select * from article_sample where name = 'bleujin' and artid > 300") ;
		Statement stmt = ex.getStatement() ;
		
		Select select = (Select)stmt ;
		PlainSelect ps = (PlainSelect) select.getSelectBody() ;
		
		Debug.debug(ex.parseWhere(ps.getWhere())) ;
	}

	public void testEqualOr() throws Exception {
		Executor ex = new Executor(mdb, "select * from article_sample where name = 'bleujin' or artid > 300") ;
		Statement stmt = ex.getStatement() ;
		
		Select select = (Select)stmt ;
		PlainSelect ps = (PlainSelect) select.getSelectBody() ;
		
		Debug.debug(ex.parseWhere(ps.getWhere())) ;
	}

	public void testEqualEmbeding() throws Exception {
		Executor ex = new Executor(mdb, "select * from article_sample where name.prefix = 'bleujin' or artid > 300") ;
		Statement stmt = ex.getStatement() ;
		
		Select select = (Select)stmt ;
		PlainSelect ps = (PlainSelect) select.getSelectBody() ;
		
		Debug.debug(ex.parseWhere(ps.getWhere())) ;
	}

}
