package net.ion.framework.db.sample.extend;

import java.util.Map;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.ICombinedUserProcedures;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.sample.TestBaseDB;

public class CombinedQuery extends TestBaseDB {

	public void setUp() throws Exception {
		super.setUp();
		IUserCommand cmd = dc.createUserCommand("delete from update_sample");
		cmd.execUpdate();
	}

	/**
	 * CombinedProcedure�� Update�� Query�� ���� �ϴ� ��ɹ� �����̴�. �ַ� ���̱׷��̼� �۾��̳� session scope temp table�� ������ �۾��Ҷ� ���δ�.
	 * 
	 * ù��° MDL : insert�� �ι�° Select : �տ� insert�Ѱ��� select�Ѵ�. ����° MDL�� : ù��° insert�Ѱ��� �����.
	 * 
	 * �� 3���� ������ ��� ����������.. ��������δ� �ƹ��͵� insert���� ������. 2��° select ���� ���� �˾Ƴ��� �ִ�. combinedProcedure�� �ȿ� MDL������ �ֵ� ����ֵ簣�� execUpdate���� ����Ѵ�.
	 * 
	 * �׸��� ���� �ϳ��� Transaction���� ó���ȴ�. �� IQueryable ��ü�� �ƴ� Queryable�� ��ӹ޾ƾ� �Ѵ�. �̰��� �� �̻��ϴٴ� ���� ������ �������� ������ �ϳ��� Ʈ��������� ó���ϱ����ؼ� Connection�� �����ؾ� �ϴµ� �� Connection�� �ܺο� �����Ű�� ���� �ʾҴ�.
	 * 
	 * ���� IQueryable�� ���������� Queryable�� ��ӹ��� ���� Ŭ������ EmptyQueryable, TimeOutQuery, XAUserProcedure�� �̰͵��� �ٸ� Queryable�� ���� �ϳ��� Ʈ��������� ó���ɼ� ��� ������
	 */

	public void testFirst() throws Exception {
		IUserCommand ins = dc.createUserCommand("insert into update_sample values(?,?)");
		ins.addParam(1).addParam("abc");

		IUserCommand sel = dc.createUserCommand("select * from update_sample");

		ICombinedUserProcedures upts = dc.createCombinedUserProcedures("combined");

		upts.add(ins, "ins", IQueryable.UPDATE_COMMAND).add(sel, "sel", IQueryable.QUERY_COMMAND);

		upts.execUpdate();

		Map result = upts.getResultMap(); // for access sel'result

		int rowcount = (Integer) result.get("ins");
		Rows rows = (Rows) result.get("sel");

		assertEquals(1, rowcount);
		assertEquals("abc", rows.firstRow().getString("b"));
	}

	public void testSecond() throws Exception {
		IUserCommand ins = dc.createUserCommand("insert into update_sample values(?,?)");
		ins.addParam(1).addParam("abc");

		IUserCommand sel = dc.createUserCommand("select * from update_sample");

		IUserCommand del = dc.createUserCommand("delete from update_sample where a = ?");
		del.addParam(1);

		ICombinedUserProcedures upts = dc.createCombinedUserProcedures("combined");

		upts.add(ins, "ins", IQueryable.UPDATE_COMMAND).add(sel, "sel", IQueryable.QUERY_COMMAND).add(del, "del", IQueryable.UPDATE_COMMAND);

		upts.execUpdate();

		Map result = upts.getResultMap(); // for access sel'result

		int inscount = (Integer) result.get("ins");
		Rows rows = (Rows) result.get("sel");
		int delcount = (Integer) result.get("del");

		assertEquals(1, inscount);
		assertEquals("abc", rows.firstRow().getString("b"));
		assertEquals(1, delcount);

		Rows crows = dc.createUserCommand("select * from update_sample where a = ?").addParam(1).execQuery();
		assertEquals(false, crows.next());

	}

}
