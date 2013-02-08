package net.ion.framework.session;

import java.util.Enumeration;

/**
 * Session Interface
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public interface Session {
	/**
	 * @return session이 생성된 시각
	 */
	public long getCreationTime();

	/**
	 * @return session의 id
	 */
	public String getId();

	/**
	 * @return 최근 session attribute에 대한 접근 시각
	 */
	public long getLastAccessedTime();

	/**
	 * @return session이 사용하는 context
	 */
	public SessionContext getSessionContext();

	/**
	 * @return session이 inactive 상태로 valid한 시간(초)
	 */
	public int getMaxInactiveInterval();

	/**
	 * session에서 attribute를 가져온다. 없을 경우 null이다.
	 * 
	 * @param name
	 *            가져올 attribute의 key
	 * @return
	 */
	public Object getAttribute(String name);

	/**
	 * session에 attribute를 저장한다.
	 * 
	 * @param name
	 *            저장할 attribute의 key
	 * @param value
	 *            저장할 attribute
	 */
	public void setAttribute(String name, Object value);

	/**
	 * session에서 attribute를 지운다.
	 * 
	 * @param name
	 *            지우고자 하는 attribute의 key
	 */
	public void removeAttribute(String name);

	/**
	 * session에서 모든 attribute를 지운다.
	 */
	public void removeAttributeAll();

	/**
	 * session의 모든 attribute를 가져온다.
	 * 
	 * @return
	 */
	public Enumeration<?> getAttributeNames();

	/**
	 * session의 attribute 값을 모두 invalidate한다.(지운다.)
	 */
	public void invalidate();

	/**
	 * 새로 생성된 session인지 여부 (set,get이 한번도 일어 나지 않으면 true)
	 * 
	 * @return
	 */
	public boolean isNew();

	/**
	 * session 정보
	 * 
	 * @return
	 */
	public String getInfo();
}
