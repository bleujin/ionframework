package net.ion.framework.session;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 간단한 session 구현
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
	 * session을 생성한다.
	 * 
	 * @param context
	 *            SessionContext session context
	 * @param id
	 *            String session id
	 * @param maxInactiveInterval
	 *            int 세션 만료 체크 시간 (ms)
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
	 * 세션 생성 시각
	 * 
	 * @return long he difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.
	 */
	public long getCreationTime() {
		return this.creationTime;
	}

	/**
	 * 세션 생성 시각
	 * 
	 * @param time
	 *            long long he difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.
	 */
	public void setCreationTime(long time) {
		this.creationTime = time;
		this.lastAccessedTime = time;
	}

	/**
	 * @return String 세션 id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            String 세센 id
	 */
	public void setId(String id) {
		if (id == null) {
			throw new IllegalStateException("Null Session ID.");
		}
		this.id = id;
	}

	/**
	 * 마지막 세션 접근 시각
	 * 
	 * @return long
	 */
	public long getLastAccessedTime() {
		return this.lastAccessedTime;
	}

	/**
	 * 마지막 세션 접근 시각
	 * 
	 * @param time
	 *            long
	 */
	public void setLastAccessedTime(long time) {
		this.lastAccessedTime = time;
	}

	/**
	 * 사용중인 session context
	 * 
	 * @return SessionContext
	 */
	public SessionContext getSessionContext() {
		return this.context;
	}

	/**
	 * 사용할 session context를 지정
	 * 
	 * @param context
	 *            SessionContext
	 */
	public void setSessionContext(SessionContext context) {
		this.context = context;
	}

	/**
	 * 만료 여부를 체크할 최소 시간을 리턴한다.
	 * 
	 * @return int
	 */
	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	/**
	 * 만료 여부를 체크할 최소 시간을 설정한다.
	 * 
	 * @param interval
	 *            int
	 */
	public void setMaxInactiveInterval(int interval) {
		this.maxInactiveInterval = interval;
	}

	/**
	 * 세션에 저장된 속성 값을 리턴한다.
	 * 
	 * @param name
	 *            String 속성키
	 * @return Object 속성값
	 */
	public Object getAttribute(String name) {
		refreshAccessedTime();
		return repository.get(name);
	}

	public Object getAttributeNotRefreshAccessedTime(String name) {
		return repository.get(name);
	}

	/**
	 * 세션에 저장된 속성 값을 지정한다.
	 * 
	 * @param name
	 *            String 속성키
	 * @param value
	 *            Object 속성값
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
	 * 저장된 속성을 제거한다.
	 * 
	 * @param name
	 *            String 제거할 속성의 키
	 */
	public void removeAttribute(String name) {
		refreshAccessedTime();
		this.repository.remove(name);
	}

	/**
	 * 세선에 저장된 모든 속성을 제거한다.
	 */
	public void removeAttributeAll() {
		refreshAccessedTime();
		this.repository.clear();
	}

	/**
	 * 저장된 모든 속성의 키를 카져온다.
	 * 
	 * @return Enumeration java.lang.String type
	 */
	public Enumeration<Object> getAttributeNames() {
		refreshAccessedTime();
		return this.repository.elements();
	}

	/**
	 * 세션을 만료시킨다. (무효화시킴)
	 */
	public void invalidate() {
		refreshAccessedTime();
		this.repository.clear();
	}

	/**
	 * 세로 생성한 세션인지 여부
	 * 
	 * @return boolean
	 */
	public boolean isNew() {
		return this.isNew;
	}

	/**
	 * 새로 생성 여부 결정
	 * 
	 * @param isNew
	 *            boolean
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * session의 class name을 가져온다.
	 * 
	 * @return String
	 */
	public String getInfo() {
		return this.getClass().getName();
	}

	/**
	 * 마지막 세션 접근 시각을 새로고친다.
	 */
	private void refreshAccessedTime() {
		this.setLastAccessedTime(System.currentTimeMillis());
	}
}
