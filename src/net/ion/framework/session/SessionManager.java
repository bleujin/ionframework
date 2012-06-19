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
	 * timeout�� session���� �����ϴ� thread
	 */
	private Thread managerThread;

	/**
	 * timeout�� session ���� ���� �ϴ� �ֱ� (��)
	 */
	private int checkInterval = 60; // default 1 min

	/**
	 * session�� default timeout(��)
	 */
	private int defaultMaxInactiveInterval = 60 * 30; // default 30 min

	/**
	 * session�� ����� context
	 */
	private SessionContext context;

	/**
	 * session manager ���� (manager�ۼ��ÿ� ����)
	 * 
	 * @return
	 */
	public abstract String getInfo();

	/**
	 * session manager �̸� (�����ÿ� ����)
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * session�� �����Ͽ� �����Ѵ�.
	 * 
	 * @param sessionId
	 *            ������ session�� id
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
		// ������ ���ŵǱ� ������ �ؾ��� ���� ������.. ��ӹ޾Ƽ� �˾Ƽ� ����
	}

	protected void removeBeforeJob(String id) {
		// ������ ���ŵǰ� �� �Ŀ� �ؾ��� ���� ������.. ��ӹ޾Ƽ� �˾Ƽ� ����
	}

	/**
	 * session manager�� �ʱ�ȭ �� ���� manager thread �� start�Ǳ� �� �ҷ�����.
	 */
	protected abstract void load();

	/**
	 * session manager�� �ʱ�ȭ �� ���� manager thread �� stop�Ǳ� �� �ҷ�����.
	 */
	protected abstract void unload();

	/**
	 * @return session manager�� context
	 */
	final public SessionContext getSessionContext() {
		return this.context;
	}

	/**
	 * @param context
	 *            session manager�� context�� ������ ��
	 */
	final protected void setSessionContext(SessionContext context) {
		this.context = context;
	}

	/**
	 * @return session�� default timeout (��)
	 */
	final public int getMaxInactiveInterval() {
		return this.defaultMaxInactiveInterval;
	}

	/**
	 * @param interval
	 *            session�� default timeout (��)
	 */
	final public void setMaxInactiveInterval(int intervalSec) {
		this.defaultMaxInactiveInterval = intervalSec;
	}

	/**
	 * @param checkInterval
	 *            timeout�� session���� �����ϴ� thread�� check �ֱ� (��)
	 */
	final public void setCheckInterval(int checkIntervalSec) {
		this.checkInterval = checkIntervalSec;
	}

	/**
	 * @return timeout�� session���� �����ϴ� thread�� check �ֱ� (��)
	 */
	final public int getCheckInterval() {
		return this.checkInterval;
	}

	/**
	 * sessionId�� �ش��ϴ� session�� �����´�. ���� ��� �����Ѵ�.
	 * 
	 * @param sessionId
	 *            ���������� �ϴ� sessionId
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
	 * sessionId�� �ش��ϴ� session�� �����´�.
	 * 
	 * @param sessionId
	 *            ���������� �ϴ� sessionId
	 * @param create
	 *            sessionId�� �ش��ϴ� session�� ���� ��� ���� ����
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
	 * context�� �ش� session�� �߰��Ѵ�.
	 * 
	 * @param session
	 *            �߰��ϰ��� �ϴ� session
	 */
	final protected void add(AbstractSession session) {
		this.context.add(session);
	}

	/**
	 * context���� �ش� session�� �����Ѵ�.
	 * 
	 * @param session
	 *            ���� �ϰ��� �ϴ� session
	 */
	final protected void remove(AbstractSession session) {
		session.removeAttributeAll();
		this.context.remove(session.getId());
	}

	/**
	 * context���� sessionId�� �ش��ϴ� session�� ã�´�. ������ null�̴�.
	 * 
	 * @param id
	 *            ã�� sessionId
	 * @return
	 */
	final public Session findSession(String id) {
		return this.context.find(id);
	}

	/**
	 * context�� ��� session�� �����´�.
	 * 
	 * @return
	 */
	final public Session[] findSessions() {
		return this.context.findSessions();
	}

	/**
	 * Session Manager�� ������ �� �ݵ�� �� �޼ҵ带 �ҷ���� session timeout�� �����Ѵ�.!!
	 */
	final public void begin() {
		this.load();

		this.managerThread = new ManagerThread();
		this.managerThread.setName(getName());
		this.managerThread.setDaemon(true);
		this.managerThread.start();
	}

	/**
	 * Session Manager�� ����� ���� �� ȣ��. session timeout thread �� �����Ѵ�.!!
	 * 
	 * �ѹ� end()�� ���� �ٽ� begin()�ϸ� �ȵȴ�.
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
	 * ������ session id�� �������ش�.
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
