package net.ion.framework.db.mongo;

import org.bson.types.ObjectId;

import net.ion.framework.db.DBController;
import net.ion.framework.db.Page;
import net.ion.framework.db.mongo.MongoDBManager;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.util.Debug;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

public class TestMongoDBManager extends MongoTestBase{

	private static DBController dc = null ;
	public void setUp() throws Exception{
		if (dc == null){
			dc = new DBController(MongoDBManager.create("localhost", 27017, "test")) ;
			dc.initSelf() ;
		}
	}

	
	public void testFirst() throws Exception {
		
		new ObjectId();
		
		IUserCommand cmd = dc.createUserCommand("select * from bulletin.bleujin");
		
//		for (int i = 0; i < 5; i++) {
//			long start = System.currentTimeMillis() ;
//			cmd.execQuery()  ;
//			Debug.debug(System.currentTimeMillis()- start) ;
//		}
		Debug.debug(cmd.execQuery()) ;
	}
	

	public void testNotExistTable() throws Exception {
		IUserCommand cmd = dc.createUserCommand("select * from dual");
		Debug.debug(cmd.execQuery()) ;
	}

	public void testInsert() throws Exception {
		IUserCommand cmd = dc.createUserCommand("insert into people (name, address) values('bleujin', 'seoul')");
		cmd.execUpdate() ;
	}
	
	
	public void testSelect() throws Exception {
		
		IUserCommand cmd = dc.createUserCommand("select * from people");
		Debug.debug(cmd.execQuery()) ;
	}
	
	
	public void testSampleArticle() throws Exception {
		IUserCommand cmd = dc.createUserCommand("select * from article_sample where catid = 'c_tp_m_g_mus'");
		cmd.setPage(Page.DEFAULT) ;
		long start = System.currentTimeMillis() ;
		Debug.debug(System.currentTimeMillis()- start, cmd.execQuery() ) ;
	}

	public void testSampleArticleGrater() throws Exception {
		IUserCommand cmd = dc.createUserCommand("select * from article_sample where artid >= 2271824");
		cmd.setPage(Page.DEFAULT) ;
		long start = System.currentTimeMillis() ;
		Debug.debug(System.currentTimeMillis()- start, cmd.execQuery() ) ;
	}
	

	public void testMongo() throws Exception {
		Mongo m = new Mongo("localhost", 27017) ;
		DB db = m.getDB("test") ;
		DBCollection col = db.getCollection("article_sample") ;
		
		BasicDBObject query = new BasicDBObject() ;
		query.append("CATID", "c_tp_m_g_mus") ;
		
		DBCursor cursor = col.find(query) ;
		Debug.debug("count", col.getCount()) ;
		int limit = 100 ;
		while(cursor.hasNext()){
			if (limit-- < 0) break ;
			Debug.debug(cursor.next()) ;
		}
	}
	
	

}
