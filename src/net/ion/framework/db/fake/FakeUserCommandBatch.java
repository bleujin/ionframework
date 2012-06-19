package net.ion.framework.db.fake;

import java.sql.Connection;

import net.ion.framework.db.procedure.UserCommandBatch;

public class FakeUserCommandBatch extends UserCommandBatch {

	/*
	 * 이 클래스의 존재 이유는.. 가르켜 줄건 connection 밖에 없지만... 즉 이건 기존 레거시 사용방법을 크게 흐트리지 않으면서 해당 Framework의 기능 - 트랜잭션, 클라이언트 커서 등등- 은 모두 사용하고 싶을때이다.
	 * 
	 * connection을 닫을 책임은 기존 connection을 생성시키는 레거시 코드에 있으며 (connection이 풀로 관리되는지 알수 없기 때문이다.) Fake는 Connection을 닫지 않는다.
	 */

	public FakeUserCommandBatch(Connection conn, String stmtSQL) {
		super(new FakeDBController(conn), stmtSQL);
	}
}
