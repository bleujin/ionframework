package net.ion.framework.session;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * ������ session ����
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class SimpleSession extends AbstractSession {
	SessionContext context;

	Hashtable<String, Object> repository;
	String id;
	long creationTime;
	long lastAccessedTime;

	int maxInactiveInterval;
	boolean isNew;

	/**
	 * session�� �����Ѵ�.
	 * 
	 * @param context
	 *            SessionContext session context
	 * @param id
	 *            String session id
	 * @param maxInactiveInterval
	 *            int ���� ���� üũ �ð� (ms)
	 */
	public SimpleSession(SessionContext context, String id, int maxInactiveInterval) {
		this.repository = new Hashtable<String, Object>();

		this.setSessionContext(context);
		this.setId(id);
		this.setCreationTime(System.currentTimeMillis());
		this.setMaxInactiveInterval(maxInactiveInterval);

		this.isNew = true;
	}

	/**
	 * ���� ���� �ð�
	 * 
	 * @return long he difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.
	 */
	public long getCreationTime() {
		return this.creationTime;
	}

	/**
	 * ���� ���� �ð�
	 * 
	 * @param time
	 *            long long he difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.
	 */
	public void setCreationTime(long time) {
		this.creationTime = time;
		this.lastAccessedTime = time;
	}

	/**
	 * @return String ���� id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            String ���� id
	 */
	public void setId(String id) {
		if (id == null) {
			throw new IllegalStateException("Null Session ID.");
		}
		this.id = id;
	}

	/**
	 * ������ ���� ���� �ð�
	 * 
	 * @return long
	 */
	public long getLastAccessedTime() {
		return this.lastAccessedTime;
	}

	/**
	 * ������ ���� ���� �ð�
	 * 
	 * @param time
	 *            long
	 */
	public void setLastAccessedTime(long time) {
		this.lastAccessedTime = time;
	}

	/**
	 * ������� session context
	 * 
	 * @return SessionContext
	 */
	public SessionContext getSessionContext() {
		return this.context;
	}

	/**
	 * ����� session context�� ����
	 * 
	 * @param context
	 *            SessionContext
	 */
	public void setSessionContext(SessionContext context) {
		this.context = context;
	}

	/**
	 * ���� ���θ� üũ�� �ּ� �ð��� �����Ѵ�.
	 * 
	 * @return int
	 */
	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	/**
	 * ���� ���θ� üũ�� �ּ� �ð��� �����Ѵ�.
	 * 
	 * @param interval
	 *            int
	 */
	public void setMaxInactiveInterval(int interval) {
		this.maxInactiveInterval = interval;
	}

	/**
	 * ���ǿ� ����� �Ӽ� ���� �����Ѵ�.
	 * 
	 * @param name
	 *            String �Ӽ�Ű
	 * @return Object �Ӽ���
	 */
	public Object getAttribute(String name) {
		refreshAccessedTime();
		return repository.get(name);
	}

	public Object getAttributeNotRefreshAccessedTime(String name) {
		return repository.get(name);
	}

	/**
	 * ���ǿ� ����� �Ӽ� ���� �����Ѵ�.
	 * 
	 * @param name
	 *            String �Ӽ�Ű
	 * @param value
	 *            Object �Ӽ���
	 */
	public void setAttribute(String name, Object value) {
		refreshAccessedTime();
		if (name == null) {
			throw new NullPointerException("key must NOT be null.");
		}
		if (value == null) {
			this.removeAttribute(name);
		} else {
			this.repository.put(name, value);
		}
	}

	/**
	 * ����� �Ӽ��� �����Ѵ�.
	 * 
	 * @param name
	 *            String ������ �Ӽ��� Ű
	 */
	public void removeAttribute(String name) {
		refreshAccessedTime();
		this.repository.remove(name);
	}

	/**
	 * ������ ����� ��� �Ӽ��� �����Ѵ�.
	 */
	public void removeAttributeAll() {
		refreshAccessedTime();
		this.repository.clear();
	}

	/**
	 * ����� ��� �Ӽ��� Ű�� ī���´�.
	 * 
	 * @return Enumeration java.lang.String type
	 */
	public Enumeration<Object> getAttributeNames() {
		refreshAccessedTime();
		return this.repository.elements();
	}

	/**
	 * ������ �����Ų��. (��ȿȭ��Ŵ)
	 */
	public void invalidate() {
		refreshAccessedTime();
		this.repository.clear();
	}

	/**
	 * ���� ������ �������� ����
	 * 
	 * @return boolean
	 */
	public boolean isNew() {
		return this.isNew;
	}

	/**
	 * ���� ���� ���� ����
	 * 
	 * @param isNew
	 *            boolean
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * session�� class name�� �����´�.
	 * 
	 * @return String
	 */
	public String getInfo() {
		return this.getClass().getName();
	}

	/**
	 * ������ ���� ���� �ð��� ���ΰ�ģ��.
	 */
	private void refreshAccessedTime() {
		this.setLastAccessedTime(System.currentTimeMillis());
	}
}
