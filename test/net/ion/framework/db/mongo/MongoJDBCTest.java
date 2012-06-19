package net.ion.framework.db.mongo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;
import net.ion.framework.util.Debug;

public class MongoJDBCTest extends TestCase{

	void print(String name, ResultSet res) throws SQLException {
		System.out.println(name);
		while (res.next()) {
			System.out.println("\t" + res.getString("name") + "\t" + res.getInt("age") + "\t" + res.getObject(0));
		}
	}

	public void testFirst() throws Exception {

		Class.forName("net.ion.framework.db.mongo.jdbc.MongoDriver");

		Connection c = DriverManager.getConnection("mongodb://localhost:27017/test");

		Statement stmt = c.createStatement();

		// drop old table
		stmt.executeUpdate("drop table people");

		long start = System.currentTimeMillis() ;
		// insert some data
		stmt.executeUpdate("insert into people ( name , age ) values ( 'eliot' , 30 )");
		stmt.executeUpdate("insert into people ( name , age ) values ( 'sara' , 21 )");
		stmt.executeUpdate("insert into people ( name , age ) values ( 'jaime' , 28 )");

		// print
		print("not sorted", stmt.executeQuery("select name, age from people "));
		print("sorted by age", stmt.executeQuery("select name , age from people order by age "));
		print("sorted by age desc", stmt.executeQuery("select name , age from people order by age desc "));

		// update
		stmt.executeUpdate("update people set age=32 where name='jaime'");
		print("sorted by age desc", stmt.executeQuery("select name , age from people order by age desc "));
		Debug.line(System.currentTimeMillis() - start) ;
	}

}
