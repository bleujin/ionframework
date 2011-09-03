package net.ion.framework.db.sample.first;

import net.ion.framework.db.Rows;
import net.ion.framework.db.procedure.IUserCommand;
import net.ion.framework.db.procedure.IUserProcedure;
import net.ion.framework.db.sample.TestBaseDB;

public class P4_UserProcedure extends TestBaseDB {

	/*
	 * Procedure�� �����ս� ��簡 ���ȼ��̶�簡 �ϴ� ������ �ִµ� ������ ���� ū ������ ���α׷��� DB�������� ���δٴ� ���̴�. �̰� �� �Ҹ��ĸ� - �ݴ�� �˰� �ִ� ����� �ִµ� select a, b from framework_update_sample �̶�� ������ �����ϴ� ���α׷��� ������. �ش� ���α׷��� framework_update_sample���̺? ���ؼ� �ʹ� ���� �˰� �־�� �ϱ� ������ DB�ʿ��� ������
	 * �Ͼ�� �� �������� �ɰ��ϴ�. Ư���� SQL�� ��Ʈ�� ������ ����Ǳ� ������ �� ���� ���α׷��� �� ���캸�ƾ� �ϴ� ���? �ؾ� �Ѵ�.
	 * 
	 * �̴� DB �������� ���濡 ������� �ɰ��� �����ҿ� ������ �۾��Ƿε��� �߻��Ű�°ɷ� �ۿ��Ѵ�.
	 * 
	 * ���⼭ Procedure�� ����Ŭ Ȥ�� MSSQL���� ���� ������Ʈ �ϰ� �����ϴ� procedure�� ���ϴ� ���� �ƴϴ�. ������ message key�� DB�� �＼�� �ϴ� �߻����� ����� ���ϸ�. ���� procedure�� ��� �ؼ��ϰ� ����ϴ°��� procedure�� �����ϴ� ������� �������� �޷��ִ�.
	 * 
	 * �̸��׸� OracleUserProcedure�� A@B��� Procedure�� A��� Package�� B��� Procedure Ȥ�� function�̶�� �ؼ��ϸ� MSSQLProcedure�� MySQLProcedure�� A@B��� Procedure�� A@B��� Procedure Ȥ�� function�̶�� �ؼ��ϸ�(MSSQL���� Package�� ���. ) H2Procedure�� A@B��� Procedure�� Ư�����Ͽ� key value
	 * ���·� ����� SQL�� name�� ����Ű�� ���̶�� �ؼ��Ѵ�.
	 * 
	 * � Procedure�� �����ϴ� ���� �������� DBManager�� ����ϴ� RepositoryService�� �޷�������.. Select Ȥ�� MDL�� �ϴ� Procedure�� Function�� ��� ������ �ϴ� ���� UserProcedure�� ������ �޷��ִ�. ���� �ٸ� �ؼ������ ������ڸ� ���ο� DBManager�� RepositoryService�� �����ϸ� �ȴ�.
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
	 * MySQL�� ��� �Ʒ��� ���� �ۼ��Ѵ�.
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
	 * Oracle�� ���
	 * 
	 * 
	 * // ���� ����� return cursor Ÿ���� �����Ѵ�. CREATE OR REPLACE PACKAGE Types as type cursorType is ref cursor ; FUNCTION dummy return number ; END ;
	 * 
	 * CREATE OR REPLACE PACKAGE BODY Types as
	 * 
	 * FUNCTION dummy return Number is BEGIN return 1 ; End dummy ; END ;
	 * 
	 * 
	 * // �Ʒ��� ���� ����� Package�� ��� ����Ѵ�. // �Ʒ��� testSelectBy�� ProcedureŰ�� Sample@selectBy() �̴�.
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
