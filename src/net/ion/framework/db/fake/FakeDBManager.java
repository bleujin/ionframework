package net.ion.framework.db.fake;

import java.sql.Connection;
import java.sql.SQLException;

import net.ion.framework.db.manager.DBManager;
import net.ion.framework.db.manager.DBType;
import net.ion.framework.db.procedure.RepositoryService;

public class FakeDBManager extends DBManager {

	private Connection conn;

	public FakeDBManager(Connection conn) {
		this.conn = conn;
	}

	public int getDBManagerType() {
		return DBType.UNKNOWN;
	}

	public String getDBType() {
		return DBType.UnknownDBName;
	}

	public RepositoryService getRepositoryService() {
		try {
			return RepositoryService.makeService(this.conn);
		} catch (SQLException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	public Connection getConnection() {
		return this.conn;
	}

	public void freeConnection(Connection conn) throws SQLException {
		// FakeDBController�� connection�� ���� å���� connectio�� �������� �Ѱ��� Ŭ������ �ִ�.
		// conn.close() ;
	}

	protected void myInitPool() {
	}

	protected void myDestroyPool() throws SQLException {
	}

}
