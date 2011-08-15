package net.ion.framework.db.sample.plan;

import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.sample.SampleTestBase;

public class ViewPlan extends SampleTestBase {

	private String query = "select * from emp_sample x1, dept_sample x2 where x1.deptno = x2.deptno order by x1.empno";

	public void testViewPlan() throws Exception {
		IUserCommand cmd = dc.createUserCommand(query);

		cmd.printPlan(System.out);
	}

}
