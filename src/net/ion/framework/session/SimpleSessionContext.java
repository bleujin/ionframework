package net.ion.framework.session;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 개별 session이 저장될 공간
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see SessionContext
 */
public class SimpleSessionContext implements SessionContext, Serializable {
	Hashtable<String, AbstractSession> sessions;

	public SimpleSessionContext() {
		this.sessions = new Hashtable<String, AbstractSession>();
	}

	public Enumeration<AbstractSession> getSessions() {
		return this.sessions.elements();
	}

	public void add(AbstractSession session) {
		this.sessions.put(session.getId(), session);
	}

	public void remove(String sessionId) {
		this.sessions.remove(sessionId);
	}

	public AbstractSession find(String sessionId) {
		return (AbstractSession) this.sessions.get(sessionId);
	}

	public AbstractSession[] findSessions() {
		return this.sessions.values().toArray(new AbstractSession[0]);
	}

	public String getInfo() {
		return this.getClass().getName();
	}

	public int size() {
		return this.sessions.size();
	}
}
