package net.ion.framework.db.sample.first;

import java.sql.SQLException;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserCommandBatch;
import net.ion.framework.db.sample.SampleTestBase;

public class P3_BatchCommandQuery extends SampleTestBase {

	public void setUp() throws Exception {
		super.setUp();
		IUserCommand cmd = dc.createUserCommand("delete from update_sample");
		cmd.execUpdate();
	}

	/**
	 * 물론 Batch를 쓰지않고 P4_UserProcedures를 쓸수도 있다. 그러나 만약 MDL문이 동일하고 Param값만 다르다면.. Batch를 사용하는게 더 좋다. 트랜잭션을 지원하는 것은 물론이고 훨씬 더 빠르다. 평균적으로 10만건단위의 배치 처리가 좋으며 약 10만건 insert시 1-3분 정도가 소모된다. P4_UserProcedurs에서 설명하는 UserProcedures는 기본적으로 MDL문이 다른 것들을 한번에
	 * 실행할때 사용한다.
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
		cmd.clearParam(); // param ojbect resource 반환
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
