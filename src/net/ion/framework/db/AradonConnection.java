package net.ion.framework.db;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class AradonConnection implements Connection {

	public static final Connection Fake = new AradonConnection();

	public void clearWarnings() throws SQLException {

	}

	public void close() throws SQLException {

	}

	public void commit() throws SQLException {

	}

	public Statement createStatement() throws SQLException {
		return null;
	}

	public Statement createStatement(int i, int j) throws SQLException {
		return null;
	}

	public Statement createStatement(int i, int j, int k) throws SQLException {
		return null;
	}

	public boolean getAutoCommit() throws SQLException {
		return false;
	}

	public String getCatalog() throws SQLException {
		return null;
	}

	public int getHoldability() throws SQLException {
		return 0;
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return null;
	}

	public int getTransactionIsolation() throws SQLException {
		return 0;
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return null;
	}

	public SQLWarning getWarnings() throws SQLException {
		return null;
	}

	public boolean isClosed() throws SQLException {
		return false;
	}

	public boolean isReadOnly() throws SQLException {
		return false;
	}

	public String nativeSQL(String s) throws SQLException {
		return null;
	}

	public CallableStatement prepareCall(String s) throws SQLException {
		return null;
	}

	public CallableStatement prepareCall(String s, int i, int j) throws SQLException {
		return null;
	}

	public CallableStatement prepareCall(String s, int i, int j, int k) throws SQLException {
		return null;
	}

	public PreparedStatement prepareStatement(String s) throws SQLException {
		return null;
	}

	public PreparedStatement prepareStatement(String s, int i) throws SQLException {
		return null;
	}

	public PreparedStatement prepareStatement(String s, int[] ai) throws SQLException {
		return null;
	}

	public PreparedStatement prepareStatement(String s, String[] as) throws SQLException {
		return null;
	}

	public PreparedStatement prepareStatement(String s, int i, int j) throws SQLException {
		return null;
	}

	public PreparedStatement prepareStatement(String s, int i, int j, int k) throws SQLException {
		return null;
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {

	}

	public void rollback() throws SQLException {

	}

	public void rollback(Savepoint savepoint) throws SQLException {

	}

	public void setAutoCommit(boolean flag) throws SQLException {

	}

	public void setCatalog(String s) throws SQLException {

	}

	public void setHoldability(int i) throws SQLException {

	}

	public void setReadOnly(boolean flag) throws SQLException {

	}

	public Savepoint setSavepoint() throws SQLException {
		return null;
	}

	public Savepoint setSavepoint(String s) throws SQLException {
		return null;
	}

	public void setTransactionIsolation(int i) throws SQLException {

	}

	public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {

	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (iface.isInstance(this)) {
			return iface.cast(this);
		}
		throw new SQLException("not supported operation");
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return iface.isInstance(this);
	}

	public Clob createClob() throws SQLException {

		return null;
	}

	public Blob createBlob() throws SQLException {

		return null;
	}

	public NClob createNClob() throws SQLException {

		return null;
	}

	public SQLXML createSQLXML() throws SQLException {

		return null;
	}

	public boolean isValid(int timeout) throws SQLException {

		return false;
	}

	public void setClientInfo(String name, String value) throws SQLClientInfoException {

	}

	public void setClientInfo(Properties properties) throws SQLClientInfoException {

	}

	public String getClientInfo(String name) throws SQLException {

		return null;
	}

	public Properties getClientInfo() throws SQLException {

		return null;
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {

		return null;
	}

	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {

		return null;
	}

	public void setSchema(String schema) throws SQLException {

	}

	public String getSchema() throws SQLException {

		return null;
	}

	public void abort(Executor executor) throws SQLException {

	}

	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

	}

	public int getNetworkTimeout() throws SQLException {

		return 0;
	}

}
