package net.ion.framework.session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;

/**
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public abstract class SessionManager {

	public SessionManager() {
	}

	/**
	 * timeout된 session들을 정리하는 thread
	 */
	private Thread managerThread;

	/**
	 * timeout된 session 들을 정리 하는 주기 (초)
	 */
	private int checkInterval = 60; // default 1 min

	/**
	 * session의 default timeout(초)
	 */
	private int defaultMaxInactiveInterval = 60 * 30; // default 30 min

	/**
	 * session이 저장될 context
	 */
	private SessionContext context;

	/**
	 * session manager 정보 (manager작성시에 결정)
	 * 
	 * @return
	 */
	public abstract String getInfo();

	/**
	 * session manager 이름 (생성시에 결정)
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * session을 생성하여 리턴한다.
	 * 
	 * @param sessionId
	 *            생성할 session의 id
	 * @return
	 */
	protected abstract AbstractSession createSession(String sessionId);

	public void removeSession(String sessionId) {
		SimpleSession session = (SimpleSession) getSession(sessionId, false);
		if (session != null) {
			this.removeBeforeJob(sessionId);
			remove(session);
			this.removeAfterJob(sessionId);
		}
	}

	protected void removeAfterJob(String id) {
		// 세션이 제거되기 이전에 해야할 일이 있으면.. 상속받아서 알아서 구현
	}

	protected void removeBeforeJob(String id) {
		// 세션이 제거되고 난 후에 해야할 일이 있으면.. 상속받아서 알아서 구현
	}

	/**
	 * session manager가 초기화 된 이후 manager thread 가 start되기 전 불러진다.
	 */
	protected abstract void load();

	/**
	 * session manager가 초기화 된 이후 manager thread 가 stop되기 전 불러진다.
	 */
	protected abstract void unload();

	/**
	 * @return session manager의 context
	 */
	final public SessionContext getSessionContext() {
		return this.context;
	}

	/**
	 * @param context
	 *            session manager의 context로 지정할 것
	 */
	final protected void setSessionContext(SessionContext context) {
		this.context = context;
	}

	/**
	 * @return session의 default timeout (초)
	 */
	final public int getMaxInactiveInterval() {
		return this.defaultMaxInactiveInterval;
	}

	/**
	 * @param interval
	 *            session의 default timeout (초)
	 */
	final public void setMaxInactiveInterval(int intervalSec) {
		this.defaultMaxInactiveInterval = intervalSec;
	}

	/**
	 * @param checkInterval
	 *            timeout된 session들을 정리하는 thread의 check 주기 (초)
	 */
	final public void setCheckInterval(int checkIntervalSec) {
		this.checkInterval = checkIntervalSec;
	}

	/**
	 * @return timeout된 session들을 정리하는 thread의 check 주기 (초)
	 */
	final public int getCheckInterval() {
		return this.checkInterval;
	}

	/**
	 * sessionId에 해당하는 session을 가져온다. 없을 경우 생성한다.
	 * 
	 * @param sessionId
	 *            가져오고자 하는 sessionId
	 * @return
	 */
	final public Session getSession(String sessionId) {
		AbstractSession session = this.context.find(sessionId);
		if (session != null) {
			session.setNew(false);
			return session;
		} else {
			return createSession(sessionId);
		}
	}

	/**
	 * sessionId에 해당하는 session을 가져온다.
	 * 
	 * @param sessionId
	 *            가져오고자 하는 sessionId
	 * @param create
	 *            sessionId에 해당하는 session이 없을 경우 생성 여부
	 * @return
	 */
	final public Session getSession(String sessionId, boolean create) {
		if (create) {
			return getSession(sessionId);
		} else {
			return this.context.find(sessionId);
		}
	}

	/**
	 * context에 해당 session을 추가한다.
	 * 
	 * @param session
	 *            추가하고자 하는 session
	 */
	final protected void add(AbstractSession session) {
		this.context.add(session);
	}

	/**
	 * context에서 해당 session을 제거한다.
	 * 
	 * @param session
	 *            제거 하고자 하는 session
	 */
	final protected void remove(AbstractSession session) {
		session.removeAttributeAll();
		this.context.remove(session.getId());
	}

	/**
	 * context에서 sessionId에 해당하는 session을 찾는다. 없으면 null이다.
	 * 
	 * @param id
	 *            찾을 sessionId
	 * @return
	 */
	final public Session findSession(String id) {
		return this.context.find(id);
	}

	/**
	 * context의 모든 session을 가져온다.
	 * 
	 * @return
	 */
	final public Session[] findSessions() {
		return this.context.findSessions();
	}

	/**
	 * Session Manager를 생성한 후 반드시 이 메소드를 불러줘야 session timeout이 동작한다.!!
	 */
	final public void begin() {
		this.load();

		this.managerThread = new ManagerThread();
		this.managerThread.setName(getName());
		this.managerThread.setDaemon(true);
		this.managerThread.start();
	}

	/**
	 * Session Manager의 사용을 끝낼 때 호출. session timeout thread 를 종료한다.!!
	 * 
	 * 한번 end()한 이후 다시 begin()하면 안된다.
	 */
	final public void end() {
		this.unload();

		if (this.managerThread != null) {
			this.managerThread.interrupt();
		}
		this.managerThread = null;
		this.context = null;
	}

	/**
	 * 임의의 session id를 생성해준다.
	 * 
	 * @return
	 */
	final public static String generateSessionId() {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update((String.valueOf(System.currentTimeMillis())).getBytes());
			byte msg[] = digest.digest();

			StringBuffer buff = new StringBuffer();
			for (int i = 0; i < msg.length; ++i) {
				int v = msg[i] & 0x7F + ((msg[i] & 0x80) > 0 ? 0x80 : 0x00);
				String t = Integer.toHexString(v);
				if (t.length() == 1) {
					buff.append("0");
				}
				buff.append(t);
			}

			return buff.toString();
		} catch (NoSuchAlgorithmException ex) {
			return String.valueOf(System.currentTimeMillis());
		}
	}

	/**
	 * time-out session management thread
	 */
	private class ManagerThread extends Thread {
		@SuppressWarnings("static-access")
		public void run() {
			Logger logger = LogBroker.getLogger(this);
			logger.info("Session manager started. :" + this.toString());

			try {
				while (!this.interrupted()) {
					this.sleep(checkInterval * 1000);

					synchronized (context) {
						Enumeration<?> sessions = context.getSessions();
						while (sessions.hasMoreElements()) {
							AbstractSession session = (AbstractSession) sessions.nextElement();

							long last = session.getLastAccessedTime();
							long maxInterval = session.getMaxInactiveInterval() * 1000;
							long interval = System.currentTimeMillis() - last;

							// System.out.println(session.getId()+"/System:" + System.currentTimeMillis() + "/last:" + last + "/maxInterval:" +
							// maxInterval + "/interval:" + interval); //debug

							if (interval >= maxInterval) {
								// System.out.println(session.getId() + ":time-out."); // debug
								// remove(session);
								removeSession(session.getId());
							}
						}
					}
				}
			} catch (InterruptedException ex) {
			}

			logger.info("Session manager ended. :" + this.toString());
		}
	}
}
