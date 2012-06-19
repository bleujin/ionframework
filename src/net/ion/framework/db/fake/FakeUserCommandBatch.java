package net.ion.framework.db.fake;

import java.sql.Connection;

import net.ion.framework.db.procedure.UserCommandBatch;

public class FakeUserCommandBatch extends UserCommandBatch {

	/*
	 * �� Ŭ������ ���� ������.. ������ �ٰ� connection �ۿ� ������... �� �̰� ���� ���Ž� ������� ũ�� ��Ʈ���� �����鼭 �ش� Framework�� ��� - Ʈ�����, Ŭ���̾�Ʈ Ŀ�� ���- �� ��� ����ϰ� �������̴�.
	 * 
	 * connection�� ���� å���� ���� connection�� ������Ű�� ���Ž� �ڵ忡 ������ (connection�� Ǯ�� �����Ǵ��� �˼� ���� �����̴�.) Fake�� Connection�� ���� �ʴ´�.
	 */

	public FakeUserCommandBatch(Connection conn, String stmtSQL) {
		super(new FakeDBController(conn), stmtSQL);
	}
}
