package net.ion.framework.message;

import java.sql.SQLException;

/**
 * DB�� �����(insert,select,update ��)�� ��Ű���� ��. �� �������̽��� �����Ѵ�. sendDB()�� DB�� ���� commend�� �ۼ��Ѵ�.
 * 
 * @author bleujin
 * @version 1.0
 */

public interface DBMessageInfo {
	public void sendDB() throws SQLException;
}
