package net.ion.framework.db.mongo;

import junit.framework.TestCase;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class MongoTestBase extends TestCase {

	protected static Mongo mongo = null;
	protected static DB mdb = null;

	public void setUp() throws Exception {
		super.setUp();
		if (mongo == null) {
			mongo = new Mongo("localhost", 27017);
			mdb = mongo.getDB("test");
		}

	}
	
}
