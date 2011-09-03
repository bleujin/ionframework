package net.ion.framework.db.sample.first;

import net.ion.framework.db.RepositoryException;
import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.sample.TestBaseDB;

public class P1_SelectQuery extends TestBaseDB {

	private String query1 = "select * from copy_sample";
	private String query2 = "select * from copy_sample no union all Select * from copy_sample";

	/**
	 * ���� �⺻���� ���� ������� dc.create.. �޼ҵ�� IQuery ��ü�� ���� execQuery�� �����ϰų�.. �ܼ��� �?�� dc.getRows�� �����ϸ� �ȴ�. ��ȯ���� Rows�� ���Ŀ� ���� �ڼ��� ����ǰ�����.. ResultSet Interface�� ������ �ܼ��� ValueObject ��ü�̴�. ��� java.sql.PrepareStatement���� ��ȯ�ϴ� ResultSet��� �޸� close�� ������ �ʾƵ�
	 * ����� Rows�� ��ȯ�޴� �� Connection�� Pool�� �̹� ��ȯ�Ǿ� �ֱ� ������ �ٸ� ��ü�� �Ķ���ͷ� ����ص� �����.
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
