package net.ion.framework.session;

public class SimpleSessionManager extends SessionManager {
	private String name;

	protected SimpleSessionManager(String name) {
		this.name = name;
		this.setSessionContext(new SimpleSessionContext());
	}

	protected AbstractSession createSession(String sessionId) {
		SimpleSession session = new SimpleSession(getSessionContext(), sessionId, getMaxInactiveInterval());

		getSessionContext().add(session);
		return session;
	}

	public String getInfo() {
		return this.getClass().getName();
	}

	public String getName() {
		return this.name;
	}

	protected void load() {
	}

	protected void unload() {
	}

	public static void main(String[] args) {
		// SessionManager manager=new SimpleSessionManager(null);
		// manager.start();
		//
		// manager.stop();
		System.out.println(generateSessionId());
	}
}
