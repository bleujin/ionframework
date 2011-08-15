package net.ion.framework.pool;

import java.util.Enumeration;

import net.ion.framework.session.Session;
import net.ion.framework.session.SessionManager;
import net.ion.framework.session.SessionManagerFactory;
import net.ion.framework.session.SimpleSessionManagerFactory;

/**
 * time-out object pool - 일정 시간동안 사용하지 않으면 pool에서 object가 없어진다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
@SuppressWarnings("unused")
public final class TimeOutObjectPool implements ObjectPool {
	private final static int MAX_OBJECT = 16;
	private final static int EMPTY = 0;

	// object별로 사용에 따른 time-out을 주기위해 session을 기반으로 코딩한다.
	private SessionManager manager = null;
	private int maxObject = MAX_OBJECT;
	private int objectIndex = EMPTY;
	private int inactiveIntervalSecond = 60;

	/**
	 * time-out 60초. maxObject 16개인 pool을 만든다.
	 */
	public TimeOutObjectPool() {
		this(60, MAX_OBJECT); // 60초
	}

	/**
	 * @param inactiveIntervalSecond
	 *            억세스 하지 않을 경우 object를 pool에서 삭제해 버릴 time-out(초) 시간
	 * @param maxObject
	 *            pool에 수용할 최대 object 수
	 */
	public TimeOutObjectPool(int inactiveIntervalSecond, int maxObject) {
		SessionManagerFactory factory = SimpleSessionManagerFactory.createFactory();
		this.manager = factory.createManager("");
		this.manager.setMaxInactiveInterval(inactiveIntervalSecond);
		this.maxObject = maxObject;
		this.inactiveIntervalSecond = inactiveIntervalSecond;
		this.manager.begin();
	}

	private synchronized String resolveGetKey() {
		Enumeration<?> e = this.manager.getSessionContext().getSessions();

		if (e.hasMoreElements()) {
			return ((Session) e.nextElement()).getId();
		} else {
			return null;
		}
	}

	private synchronized String resolveAddKey() {
		String key;
		do {
			key = String.valueOf(objectIndex++);
		} while (this.manager.getSession(key, false) != null);

		return key;
	}

	/**
	 * pool에서 object class name 에 해당하는 object 를 가져온다. 여유분이 없을 경우 null;
	 * 
	 * @param tagName
	 * @return
	 */
	public synchronized Object getObject() {
		String key = resolveGetKey();
		if (key == null)
			return null;

		Session s = this.manager.getSession(key);
		Object o = s.getAttribute(key);
		// s.removeAttribute(key);
		s.getSessionContext().remove(s.getId());

		return o;
	}

	/**
	 * 사용한 object를 pool에 환원한다.
	 * 
	 * @param className
	 * @param object
	 */
	public void releaseObject(Object object) {
		addObject(object);
	}

	/**
	 * pool에 object를 className에 맞게 추가한다. pool에 존재하는 object 수가 maxObject 보다 많으면 무시한다.
	 * 
	 * @param tagName
	 * @param tag
	 */
	public synchronized void addObject(Object object) {
		if (this.manager.getSessionContext().size() > maxObject)
			return;

		String key = resolveAddKey();
		this.manager.getSession(key).setAttribute(key, object);
	}

	/**
	 * pool을 소멸시킨다.
	 */
	public synchronized void destroy() {
		if (this.manager != null)
			this.manager.end();
		this.manager = null;
	}

	public synchronized void clear() {
		Session[] sss = this.manager.getSessionContext().findSessions();
		for (int i = 0; i < sss.length; ++i)
			this.manager.getSessionContext().remove(sss[i].getId());
	}

	public String toString() {
		Session[] ss = this.manager.findSessions();

		StringBuffer buff = new StringBuffer();
		buff.append("{");

		for (int i = 0; i < ss.length; ++i) {
			Session s = ss[i];

			if (s.getAttribute(s.getId()) == null)
				continue;

			buff.append(s.getId());
			buff.append("=");
			buff.append(s.getAttribute(s.getId()));
			if (i < ss.length - 1)
				buff.append(",");
		}

		buff.append("}");

		return buff.toString();
	}

	public int getMaxObject() {
		return this.maxObject;
	}

	// test
	public static void main(String[] args) {
		TimeOutObjectPool pool = new TimeOutObjectPool();

		Object o = TimeOutObjectPool.class;
		int i = 1000000;

		long begin = System.currentTimeMillis();
		pool.addObject(o);
		while (--i > 0) {
			pool.releaseObject(pool.getObject());

		}
		System.out.println(System.currentTimeMillis() - begin);

		pool.destroy();
	}
}
