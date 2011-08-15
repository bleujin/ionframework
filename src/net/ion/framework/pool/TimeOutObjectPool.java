package net.ion.framework.pool;

import java.util.Enumeration;

import net.ion.framework.session.Session;
import net.ion.framework.session.SessionManager;
import net.ion.framework.session.SessionManagerFactory;
import net.ion.framework.session.SimpleSessionManagerFactory;

/**
 * time-out object pool - ���� �ð����� ������� ������ pool���� object�� ��������.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
@SuppressWarnings("unused")
public final class TimeOutObjectPool implements ObjectPool {
	private final static int MAX_OBJECT = 16;
	private final static int EMPTY = 0;

	// object���� ��뿡 ���� time-out�� �ֱ����� session�� ������� �ڵ��Ѵ�.
	private SessionManager manager = null;
	private int maxObject = MAX_OBJECT;
	private int objectIndex = EMPTY;
	private int inactiveIntervalSecond = 60;

	/**
	 * time-out 60��. maxObject 16���� pool�� �����.
	 */
	public TimeOutObjectPool() {
		this(60, MAX_OBJECT); // 60��
	}

	/**
	 * @param inactiveIntervalSecond
	 *            �＼�� ���� ���� ��� object�� pool���� ������ ���� time-out(��) �ð�
	 * @param maxObject
	 *            pool�� ������ �ִ� object ��
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
	 * pool���� object class name �� �ش��ϴ� object �� �����´�. �������� ���� ��� null;
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
	 * ����� object�� pool�� ȯ���Ѵ�.
	 * 
	 * @param className
	 * @param object
	 */
	public void releaseObject(Object object) {
		addObject(object);
	}

	/**
	 * pool�� object�� className�� �°� �߰��Ѵ�. pool�� �����ϴ� object ���� maxObject ���� ������ �����Ѵ�.
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
	 * pool�� �Ҹ��Ų��.
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
