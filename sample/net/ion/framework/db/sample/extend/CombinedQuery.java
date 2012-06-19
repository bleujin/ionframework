package net.ion.framework.db.sample.extend;

import java.util.Map;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.ICombinedUserProcedures;
import net.ion.framework.db.procedure.IQueryable;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.sample.TestBaseSample;

public class CombinedQuery extends TestBaseSample {

	public void setUp() throws Exception {
		super.setUp();
		IUserCommand cmd = dc.createUserCommand("delete from update_sample");
		cmd.execUpdate();
	}

	/**
	 * CombinedProcedure는 Update와 Query를 같이 하는 명령문 집합이다. 주로 마이그레이션 작업이나 session scope temp table을 가지고 작업할때 쓰인다.
	 * 
	 * 첫번째 MDL : insert문 두번째 Select : 앞에 insert한것을 select한다. 세번째 MDL문 : 첫번째 insert한것을 지운다.
	 * 
	 * 의 3가지 문장을 담아 실행했을때.. 결과적으로는 아무것도 insert되지 않지만. 2번째 select 문의 결과는 알아낼수 있다. combinedProcedure는 안에 MDL문만을 넣든 섞어넣든간에 execUpdate만을 사용한다.
	 * 
	 * 그리고 역시 하나의 Transaction으로 처리된다. 단 IQueryable 객체가 아닌 Queryable을 상속받아야 한다. 이것이 좀 이상하다는 것은 알지만 여러개의 쿼리를 하나의 트랜잭션으로 처리하기위해선 Connection을 공유해야 하는데 이 Connection을 외부에 노출시키고 싶지 않았다.
	 * 
	 * 현재 IQueryable를 구현했지만 Queryable를 상속받지 않은 클래스는 EmptyQueryable, TimeOutQuery, XAUserProcedure로 이것들은 다른 Queryable과 같이 하나의 트랜잭션으로 처리될수 없기 때문에
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
