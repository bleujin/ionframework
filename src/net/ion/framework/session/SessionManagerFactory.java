package net.ion.framework.session;

import net.ion.framework.configuration.Configuration;

/**
 * SessionManager�� �����Ѵ�.
 * 
 * <pre>
 * SessionManagerFactory factory = SessionManagerFactory.createFactory();
 * SessionManager manager = factory.createManager(config);
 * </pre>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public abstract class SessionManagerFactory {
	private static SessionManagerFactoryType type = SessionManagerFactoryType.SIMPLE_SESSION_MANAGER_FACTORY;

	/**
	 * SessionManager�� �����ϴµ� ����� Factory Type�� �����Ѵ�.
	 * 
	 * @param type
	 *            SessionManagerFactoryType factory type
	 */
	public static void setFactoryClass(SessionManagerFactoryType type) {
		SessionManagerFactory.type = type;
	}

	/**
	 * ���� ������ factory type
	 * 
	 * @return SessionManagerFactoryType
	 */
	public static SessionManagerFactoryType getFactoryType() {
		return type;
	}

	/**
	 * session manager factory �� �����Ѵ�.
	 * 
	 * @return SessionManagerFactory
	 */
	public static SessionManagerFactory createFactory() {
		try {
			String factoryClass = type.getTypeClassName();
			return (SessionManagerFactory) Class.forName(factoryClass).newInstance();

		} catch (Throwable t) {
			// t.printStackTrace();
			return null;
		}
	}

	/**
	 * session manager�� �����Ѵ�.
	 * 
	 * @param config
	 *            Configuration
	 * @return SessionManager
	 */
	public abstract SessionManager createManager(Configuration config);

	/**
	 * session manager�� �����Ѵ�.
	 * 
	 * @param name
	 *            String
	 * @return SessionManager
	 */
	public abstract SessionManager createManager(String name);
}
