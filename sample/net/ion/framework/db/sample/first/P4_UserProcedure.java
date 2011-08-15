package net.ion.framework.db.sample.first;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.sample.SampleTestBase;

public class P4_UserProcedure extends SampleTestBase {

	/*
	 * Procedure는 퍼포먼스 라든가 보안성이라든가 하는 장점이 있는데 그중의 가장 큰 장점은 프로그램의 DB의존도를 줄인다는 것이다. 이게 먼 소리냐면 - 반대로 알고 있는 사람이 있는데 select a, b from framework_update_sample 이라는 쿼리를 실행하는 프로그램이 있을때. 해당 프로그램은 framework_update_sample테이블에 대해서 너무 상세히 알고 있어야 하기 때문에 DB쪽에서 변동이
	 * 일어날때 그 데미지가 심각하다. 특히나 SQL은 스트링 변수로 저장되기 때문에 그 많은 프로그램을 다 살펴보아야 하는 수고를 해야 한다.
	 * 
	 * 이는 DB 디자인의 변경에 있을경우 심각한 위험요소와 막대한 작업피로도를 발생시키는걸로 작용한다.
	 * 
	 * 여기서 Procedure는 오라클 혹은 MSSQL에서 벤더 디펜턴트 하게 실행하는 procedure를 뜻하는 것이 아니다. 일종의 message key로 DB를 억세스 하는 추상적인 방법을 뜻하며. 실제 procedure를 어떻게 해석하고 적용하는가는 procedure를 구현하는 사람에게 전적으로 달려있다.
	 * 
	 * 이를테면 OracleUserProcedure는 A@B라는 Procedure를 A라는 Package에 B라는 Procedure 혹은 function이라고 해석하며 MSSQLProcedure와 MySQLProcedure는 A@B라는 Procedure를 A@B라는 Procedure 혹은 function이라고 해석하며(MSSQL에는 Package가 없다. ) H2Procedure는 A@B라는 Procedure를 특정파일에 key value
	 * 형태로 저장된 SQL의 name을 가리키는 것이라고 해석한다.
	 * 
	 * 어떤 Procedure를 선택하는 것은 전적으로 DBManager가 사용하는 RepositoryService에 달려있으며.. Select 혹은 MDL을 하는 Procedure와 Function을 어떻게 만들어야 하는 것은 UserProcedure의 구현에 달려있다. 만약 다른 해석방식을 적용하자면 새로운 DBManager와 RepositoryService를 구현하면 된다.
	 */

	public void setUp() throws Exception {
		super.setUp();
		IUserCommand cmd = dc.createUserCommand("delete from update_sample");
		cmd.execUpdate();
	}

	public void testProcedureUpdate() throws Exception {
		IUserProcedure upt = dc.createUserProcedure("Sample@insertWith(?,?)");
		upt.addParam(1).addParam("abc");

		upt.execUpdate();
	}

	public void testProcedureQuery() throws Exception {
		testProcedureUpdate();

		IUserProcedure upt = dc.createUserProcedure("Sample@selectBy(?)");
		upt.addParam(1);

		Rows rows = upt.execQuery();

		assertEquals(1, rows.getRowCount());
		assertEquals("abc", rows.firstRow().getString("b"));
	}

	/*
	 * MySQL의 경우 아래와 같이 작성한다.
	 * 
	 * 
	 * delimiter //
	 * 
	 * drop function if exists sample_insertWith //
	 * 
	 * CREATE Function sample_insertWith(v_a int, v_b varchar(20)) returns int BEGIN INSERT INTO update_sample values(v_a, v_b); return 1 ; END//
	 * 
	 * 
	 * drop procedure if exists sample_selectBy // CREATE PROCEDURE sample_selectBy(v_a int) BEGIN SELECT * FROM update_sample WHERE a = v_a ; END//
	 * 
	 * commit ;
	 */

	/*
	 * Oracle의 경우
	 * 
	 * 
	 * // 먼저 공용할 return cursor 타입을 정의한다. CREATE OR REPLACE PACKAGE Types as type cursorType is ref cursor ; FUNCTION dummy return number ; END ;
	 * 
	 * CREATE OR REPLACE PACKAGE BODY Types as
	 * 
	 * FUNCTION dummy return Number is BEGIN return 1 ; End dummy ; END ;
	 * 
	 * 
	 * // 아래와 같이 적당한 Package로 묶어서 사용한다. // 아래의 testSelectBy의 Procedure키는 Sample@selectBy() 이다.
	 * 
	 * CREATE OR REPLACE PACKAGE Sample is function selectBy(v_a number) return Types.cursorType ; function insertWith(v_a number, v_b varchar2) return number ; END Sample; /
	 * 
	 * 
	 * CREATE OR REPLACE PACKAGE BODY Sample is
	 * 
	 * function selectBy (v_a number) return Types.cursorType is rtn_cursor Types.cursorType ; begin open rtn_cursor For select * from update_sample where a = v_a ;
	 * 
	 * return rtn_cursor ; end ;
	 * 
	 * function insertWith(v_a number, v_b varchar2) return number is begin insert into update_sample values(v_a, v_b) ; return SQL%ROWCOUNT ; end ;
	 * 
	 * End Sample ;
	 */

}
