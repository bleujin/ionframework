package net.ion.framework.session;

import net.ion.framework.configuration.Configuration;

/**
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see net.ion.framework.session.SessionManagerFactory
 */

public class SimpleSessionManagerFactory extends SessionManagerFactory {
	/**
	 * config�� ���� ���� manager�� �����ϹǷ� null�� �Է��ϸ�ȴ�.-_-;;
	 * 
	 * @param config
	 * @return
	 */
	public SessionManager createManager(Configuration config) {
		return new SimpleSessionManager("Simple Session Manager");
	}

	public SessionManager createManager(String name) {
		return new SimpleSessionManager(name);
	}
}
