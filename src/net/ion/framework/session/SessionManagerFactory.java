package net.ion.framework.session;

import net.ion.framework.configuration.Configuration;

/**
 * SessionManager를 생성한다.
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
	 * SessionManager를 생성하는데 사용할 Factory Type을 설정한다.
	 * 
	 * @param type
	 *            SessionManagerFactoryType factory type
	 */
	public static void setFactoryClass(SessionManagerFactoryType type) {
		SessionManagerFactory.type = type;
	}

	/**
	 * 현재 설정된 factory type
	 * 
	 * @return SessionManagerFactoryType
	 */
	public static SessionManagerFactoryType getFactoryType() {
		return type;
	}

	/**
	 * session manager factory 를 생성한다.
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
	 * session manager를 생성한다.
	 * 
	 * @param config
	 *            Configuration
	 * @return SessionManager
	 */
	public abstract SessionManager createManager(Configuration config);

	/**
	 * session manager를 생성한다.
	 * 
	 * @param name
	 *            String
	 * @return SessionManager
	 */
	public abstract SessionManager createManager(String name);
}
