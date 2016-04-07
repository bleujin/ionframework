package net.ion.framework.db.postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;

import net.ion.framework.util.RandomUtil;

public class TestStd extends TestBasePG {

	public void testBatch() throws Exception {
		Connection conn = dc.getDBManager().getConnection();
		PreparedStatement insert = conn.prepareStatement("insert into dummy values (?,?)");

		for (int i = 0; i < 10; i++) {
			insert.setLong(1, RandomUtil.nextInt());
			insert.setString(2, RandomUtil.nextRandomString(10));
			insert.addBatch();
		}
		insert.executeBatch();
		insert.clearBatch(); 
		insert.close(); 
		conn.close();
	}
}
