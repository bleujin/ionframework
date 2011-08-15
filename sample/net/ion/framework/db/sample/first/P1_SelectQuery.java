package net.ion.framework.db.sample.first;

import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.sample.SampleTestBase;

public class P1_SelectQuery extends SampleTestBase {

	private String query1 = "select * from copy_sample";
	private String query2 = "select * from copy_sample no union all Select * from copy_sample";

	/**
	 * 가장 기본적인 쿼리 사용방법은 dc.create.. 메소드로 IQuery 객체를 만들어서 execQuery를 실행하거나.. 단순한 쿠리는 dc.getRows를 실행하면 된다. 반환값인 Rows는 이후에 좀더 자세히 설명되겠지만.. ResultSet Interface를 구현한 단순한 ValueObject 객체이다. 따라서 java.sql.PrepareStatement에서 반환하는 ResultSet과는 달리 close를 해주지 않아도
	 * 상관없고 Rows를 반환받는 순간 Connection은 Pool에 이미 반환되어 있기 때문에 다른 객체에 파라미터로 사용해도 상관없다.
	 */

	public void testDefault() throws Exception {
		Rows rows = dc.getRows(query1);
		assertEquals(31, rows.getRowCount());
	}

	public void testGetRows() throws Exception {
		Rows rows = dc.getRows(query2);
		assertEquals(62, rows.getRowCount());
	}

	public void testParameter() throws Exception {
		IUserCommand cmd = dc.createUserCommand("select ? from copy_sample");
		cmd.addParam(0, "abc"); // start index of param is 0 (zero)
		Rows rows = cmd.execQuery();

		rows.next();
		assertEquals("abc", rows.getString(1));
	}

	public void testFirstRow() throws Exception {
		Rows rows = dc.getRows(query1);

		assertEquals(1, rows.firstRow().getInt("no1"));

		try {
			rows = dc.getRows("select * from copy_sample where 1 = 2");
			rows.firstRow();
			fail();
		} catch (RepositoryException ignore) {

		}

	}
}
