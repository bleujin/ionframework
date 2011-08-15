package net.ion.framework.message;

import java.sql.SQLException;

/**
 * DB로 어떠한일(insert,select,update 등)을 시키려할 때. 이 인터페이스를 구현한다. sendDB()는 DB로 보낼 commend를 작성한다.
 * 
 * @author bleujin
 * @version 1.0
 */

public interface DBMessageInfo {
	public void sendDB() throws SQLException;
}
