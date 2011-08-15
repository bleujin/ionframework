package net.ion.framework.message;

import java.sql.SQLException;

/**
 * DBMessageInfo를 감싼 Wrapper클래스. handle() 메소드는 메세지가 포함하고 있는 DB작업을 실행시킨다.
 * 
 * @author bleujin
 * @version 1.0
 */

public class DBMessage extends Message {

	DBMessageInfo messageInfo;

	public DBMessage(String name, DBMessageInfo messageInfo) {
		super(name);
		this.messageInfo = messageInfo;
	}

	/**
	 * DBMessageInfo를 수행한다.
	 * 
	 * @throws SQLException
	 */
	public void handle() throws SQLException {
		messageInfo.sendDB();
	}

	public String toString() {
		return getName() + "[ DB Message from Trace\n" + messageInfo + " ]";
	}
}
