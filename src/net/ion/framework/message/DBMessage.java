package net.ion.framework.message;

import java.sql.SQLException;

/**
 * DBMessageInfo�� ���� WrapperŬ����. handle() �޼ҵ�� �޼����� �����ϰ� �ִ� DB�۾��� �����Ų��.
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
	 * DBMessageInfo�� �����Ѵ�.
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
