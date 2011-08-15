package net.ion.framework.session;

import java.io.Serializable;

/**
 * Session Interface는 session을 쓰기 위한 것만 노출 되어 있는데 그것을 확장하여 session을 setting 하는 것을 추가
 * 
 * SessionManager,SessionContext에서는 AbstractSession을 이용한다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public abstract class AbstractSession implements Session, Serializable {
	/**
	 * @param time
	 *            session이 생성된 시각
	 */
	public abstract void setCreationTime(long time);

	/**
	 * @param id
	 *            session의 id
	 */
	public abstract void setId(String id);

	/**
	 * 마지막으로 접근한 시각. '접근' attribute에 대한 모든 get,set...을 의미한다.
	 * 
	 * @param time
	 */
	public abstract void setLastAccessedTime(long time);

	/**
	 * @param context
	 *            session이 사용하는 context
	 */
	public abstract void setSessionContext(SessionContext context);

	/**
	 * @param interval
	 *            session이 inactive 상태로 valid한 시간(초)
	 */
	public abstract void setMaxInactiveInterval(int interval);

	/**
	 * 새로 생성된 session인지 여부
	 * 
	 * @param isNew
	 */
	public abstract void setNew(boolean isNew);
}
