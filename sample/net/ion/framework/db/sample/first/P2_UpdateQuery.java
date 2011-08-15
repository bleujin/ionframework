package net.ion.framework.db.sample.first;

import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.sample.SampleTestBase;

public class P2_UpdateQuery extends SampleTestBase {

	public void setUp() throws Exception {
		super.setUp();
		IUserCommand cmd = dc.createUserCommand("delete from update_sample");
		cmd.execUpdate();
	}

	public void testFirst() throws Exception {
		int count = dc.execUpdate("insert into update_sample values(1, 'ad')");
		assertEquals(1, count);
	}

	public void testParameter() throws Exception {
		IUserCommand cmd = dc.createUserCommand("insert into update_sample values(?, ?)");
		cmd.addParam(1).addParam("abc");

		int count = cmd.execUpdate();
		assertEquals(1, count);

		IUserCommand cmd2 = dc.createUserCommand("insert into update_sample values(?, ?)");
		cmd2.addParam(0, 2); // start index is 0...
		cmd2.addParam(1, "bcd");
		count = cmd2.execUpdate();
		assertEquals(1, count);
	}

}
