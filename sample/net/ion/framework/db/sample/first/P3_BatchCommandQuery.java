package net.ion.framework.db.sample.first;

import java.sql.SQLException;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.db.sample.TestBaseDB;

public class P3_BatchCommandQuery extends TestBaseDB {

	public void setUp() throws Exception {
		super.setUp();
		IUserCommand cmd = dc.createUserCommand("delete from update_sample");
		cmd.execUpdate();
	}

	/**
	 * ���� Batch�� �����ʰ� P4_UserProcedures�� ������ �ִ�. �׷��� ���� MDL���� �����ϰ� Param���� �ٸ��ٸ�.. Batch�� ����ϴ°� �� ����. Ʈ������� �����ϴ� ���� �����̰� �ξ� �� ���. ��������� 10���Ǵ����� ��ġ ó���� ������ �� 10���� insert�� 1-3�� ������ �Ҹ�ȴ�. P4_UserProcedurs���� �����ϴ� UserProcedures�� �⺻������ MDL���� �ٸ� �͵��� �ѹ�
	 * �����Ҷ� ����Ѵ�.
	 */

	public void testBatchDefault() throws Exception {
		IUserCommandBatch cmd = dc.createUserCommandBatch("insert into update_sample values(?, ?)");

		int max = 10;
		for (int i = 0; i < max; i++) {
			cmd.addBatchParam(0, i);
			cmd.addBatchParam(1, i + "th ..");
		}

		int count = cmd.execUpdate();
		assertEquals(max, count);
		cmd.clearParam(); // param ojbect resource ��ȯ
	}

	public void testAddParamCase1() throws Exception {
		IUserCommandBatch cmd = dc.createUserCommandBatch("insert into update_sample values(?, ?)");

		int max = 10;
		int[] a = new int[max];
		String[] b = new String[max];
		for (int i = 0; i < max; i++) {
			a[i] = i;
			b[i] = i + "th ..";
		}
		cmd.addParam(a).addParam(b);
		int count = cmd.execUpdate();
		assertEquals(max, count);
	}

	public void testAddParamCase2() throws Exception {
		IUserCommandBatch cmd = dc.createUserCommandBatch("insert into update_sample values(?, ?)");

		int max = 10;
		int[] a = new int[max];
		String[] b = new String[max];
		for (int i = 0; i < max; i++) {
			a[i] = i;
			b[i] = i + "th ..";
		}

		cmd.addParam(0, a);
		cmd.addParam(1, b);
		int count = cmd.execUpdate();
		assertEquals(max, count);
	}

	public void testAddParamCase3() throws Exception {
		IUserCommandBatch cmd = dc.createUserCommandBatch("insert into update_sample values(?, ?)");

		int max = 10;
		int[] a = new int[max];
		for (int i = 0; i < max; i++) {
			a[i] = i;
		}

		cmd.addParam(0, a);
		cmd.addParamToArray(1, "SameValue", max); // SameValue * max
		int count = cmd.execUpdate();
		assertEquals(max, count);
	}

	public void testTransaction() throws Exception {
		IUserCommandBatch cmd = dc.createUserCommandBatch("insert into update_sample values(?, ?)");

		int max = 10;
		int[] a = new int[max];
		String[] b = new String[max];
		for (int i = 0; i < max; i++) {
			a[i] = i;
			b[i] = i + "th ..";
		}
		b[max - 1] = "01234567890123546789"; // over maxLength

		cmd.addParam(a).addParam(b);

		try {
			cmd.execUpdate();
			fail();
		} catch (SQLException ignore) {

		}

		Rows rows = dc.getRows("select count(*) from update_sample");
		assertEquals(0, rows.firstRow().getInt(1));
	}

}
