package net.ion.framework.logging;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.ion.framework.configuration.Configuration;
import net.ion.framework.configuration.ConfigurationException;
import net.ion.framework.configuration.NotFoundXmlTagException;
import net.ion.framework.util.InstanceCreator;

/**
 * configuration에 따라 logger를 생성하고 pattern에 mapping되어 있는 logger를 반환한다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class LogBroker {
	private static LogBroker instance = null;

	// pattern to logger mapping

	// pattern과 logger name mapping
	// 적절한 pattern matching을 위해 정렬이 필요하다.(순서대로 비교) 정렬 방법은 PatternComparator 을 따른다.
	private TreeMap<String, String> patternMapping = null;

	// name과 logger name mapping
	private HashMap<String, String> nameMapping = null;

	// log handler
	private HashMap<String, Handler> handlers = null;

	// write by sehan
	// private Configuration config = null;

	/*
	 * <logs> <handler> <description>File handler.</description> <handler-name>fileHandler</handler-name> <configured-object> <class-name>java.util.logging.FileHandler</class-name> <constructor> <constructor-param> <description>pattern</description>
	 * <type>java.lang.String</type> <value>%h/java%u.log</value> </constructor-param> <constructor-param> <description>limit</description> <type>int</type> <value>50000</value> </constructor-param> <constructor-param>
	 * <description>count</description> <type>int</type> <value>1</value> </constructor-param> <constructor-param> <description>append</description> <type>boolean</type> <value>true</value> </constructor-param> </constructor> </configured-object>
	 * <level>INFO</level> <formatter>java.util.logging.XMLFormatter</formatter> <encoding>euc-kr</encoding> </handler> <handler> <description>Console handler.</description> <handler-name>consoleHandler</handler-name> <configured-object>
	 * <class-name>java.util.logging.ConsoleHandler</class-name> </configured-object> <level>INFO</level> <formatter>java.util.logging.SimpleFormatter</formatter> <encoding>euc-kr</encoding> </handler> <logger> <description>Framework default logger
	 * configuration.</description> <logger-name>frameworkLogger</logger-name> <handler-name>fileHandler</handler-name> <handler-name>consoleHandler</handler-name> </logger> <logger> <description>Default logger configuration.</description>
	 * <logger-name>defaultLogger</logger-name> <handler-name>consoleHandler</handler-name> </logger> <logger-mapping> <description>Mapping for framework classes</description> <pattern>net.ion.framework.*</pattern>
	 * <logger-name>frameworkLogger</logger-name> </logger-mapping> <logger-mapping> <description>Default mapping.</description> <pattern>*</pattern> <logger-name>defaultLogger</logger-name> </logger-mapping> </logs>
	 */

	private LogBroker(Configuration config) throws ConfigurationException {
		this.patternMapping = new TreeMap<String, String>(new PatternComparator());
		this.nameMapping = new HashMap<String, String>();
		this.handlers = new HashMap<String, Handler>();

		// handler를 생성하고 logger에 연결하고 mapping table을 넣는다.

		initLoggers(config);

		instance = this;
	}

	private void initLoggers(Configuration config) throws ConfigurationException {
		try {
			if (!config.getTagName().equals("logs"))
				throw new ConfigurationException("unknown configuration.");

			PrintStream out = System.out;

			out.println("initializing loggers.");
			// make handlers
			try {
				Configuration[] handlerConfigs = config.getChildren("handler");

				for (int i = 0; i < handlerConfigs.length; ++i) {
					Configuration handlerConfig = handlerConfigs[i];

					String handlerName = handlerConfig.getChild("handler-name").getValue();

					// create handler
					Handler handler = (Handler) InstanceCreator.createConfiguredInstance(handlerConfig.getChild("configured-object"));

					// set level if necessary
					try {
						String level = handlerConfig.getChild("level").getValue();
						handler.setLevel(Level.parse(level));
					} catch (NotFoundXmlTagException notFoundEx) {
						out.println("handler:" + handlerName + " :skipped setting level.");
					}

					// set formatter if necessary
					try {
						String formatterName = handlerConfig.getChild("formatter").getValue();
						java.util.logging.Formatter formatter = (java.util.logging.Formatter) Class.forName(formatterName).newInstance();
						handler.setFormatter(formatter);
					} catch (NotFoundXmlTagException notFoundEx) {
						out.println("handler:" + handlerName + " :skipped setting formatter.");
					}

					// set encoding if necessary
					try {
						String encoding = handlerConfig.getChild("encoding").getValue();
						handler.setEncoding(encoding);
					} catch (NotFoundXmlTagException notFoundEx) {
						out.println("handler:" + handlerName + ":skipped setting encoding charset.");
					}

					// 테이블에 등록 시켜둔다.
					handlers.put(handlerName, handler);
				}
			} catch (NotFoundXmlTagException noTag) {
				out.println("skipped making handlers.");
			}

			// loggers
			try {
				Configuration[] loggerConfigs = config.getChildren("logger");

				for (int i = 0; i < loggerConfigs.length; ++i) {
					Configuration loggerConfig = loggerConfigs[i];

					String loggerName = loggerConfig.getChild("logger-name").getValue();
					Logger logger = Logger.getLogger(loggerName);

					Configuration[] handlerNameConfigs = loggerConfig.getChildren("handler-name");
					for (int j = 0; j < handlerNameConfigs.length; ++j) {
						String handlerName = handlerNameConfigs[j].getValue();
						Handler handler = (Handler) handlers.get(handlerName);

						if (handler == null)
							throw new ConfigurationException("not found handler:" + handlerName);

						logger.addHandler(handler);
					}
				}
			} catch (NotFoundXmlTagException noTag) {
				out.println("skipped setting loggers.");
			}

			// mapping
			try {
				Configuration[] loggerMappingConfigs = config.getChildren("logger-mapping");
				for (int i = 0; i < loggerMappingConfigs.length; ++i) {
					Configuration loggerMappingConfig = loggerMappingConfigs[i];

					String pattern = loggerMappingConfig.getChild("pattern").getValue();
					String loggerName = loggerMappingConfig.getChild("logger-name").getValue();

					addMapping(pattern, loggerName);
				}
			} catch (NotFoundXmlTagException noTag) {
				out.println("skipped setting mapping.");
			}

			out.println("initialized.");

		} catch (Exception ex) {
			throw new ConfigurationException(ex);
		}
	}

	/**
	 * name에 mapping되는 loggerName을 가져온다.
	 * 
	 * @param name
	 * @return
	 */
	private String resolve(String name) {
		// cachedMapping에서 먼저 읽어 본다.
		String loggerName = (String) nameMapping.get(name);

		// mapping에서 name에 대한 pattern을 resolve 한다.
		if (loggerName == null) {
			// pattern에서 name을 찾는다.
			String[] patterns = patternMapping.keySet().toArray(new String[0]);
			for (int i = 0; i < patterns.length; ++i) {
				if (isCorrespondingClass(patterns[i], name)) {
					loggerName = patternMapping.get(patterns[i]);

					// 다음 사용을 위해 loggerName을 cachedMapping에 넣어 둔다.
					nameMapping.put(name, loggerName);

					break;
				}
			}
		}

		// System.out.println(patternMapping);
		// System.out.println(nameMapping);

		// mapping 정보가 없으면 name 그대로 return
		if (loggerName != null)
			return loggerName;
		else
			return name;
	}

	/**
	 * class가 package에 속하는지 테스트
	 * 
	 * @param packageName
	 * @param className
	 * @return
	 */
	private boolean isCorrespondingClass(String packageName, String className) {
		// packageName의 마지막 *을 없앤다.
		packageName = packageName.substring(0, packageName.length() - 1);

		// packageName 이름이 className보다 크면 반드시 아니지..
		if (packageName.length() > className.length())
			return false;
		className = className.substring(0, packageName.length());

		// packageName 만큼 className이 같으면 OK
		if (className.endsWith(packageName))
			return true;
		else
			return false;
	}

	/**
	 * 새로운 mapping을 추가한다.
	 * 
	 * keyName loggerName a.b.c.* -> logger1 a.b.c.A -> logger2 a.b.c.d.* -> logger3
	 * 
	 * @param keyName
	 *            pattern 또는 name key
	 * @param loggerName
	 *            pattern과 mapping할 loggerName
	 */
	private void addMapping(String keyName, String loggerName) {
		if (keyName.equals("*") || keyName.endsWith(".*"))
			patternMapping.put(keyName, loggerName);
		else
			nameMapping.put(keyName, loggerName);
	}

	/**
	 * configuration 값으로 log handler,logger,mapping을 초기화 한다.
	 * 
	 * 새로운 mapping으로 완전 대체 된다. (기존에 mapping 값은 유지되지 않음)
	 * 
	 *<pre>
	 * * 설정 예제 :
	 * 
	 *    &lt;logs&gt;
	 *        &lt;handler&gt;
	 *            &lt;description&gt;File handler.&lt;/description&gt;
	 *            &lt;handler-name&gt;fileHandler&lt;/handler-name&gt;
	 *            &lt;configured-object&gt;
	 *                &lt;class-name&gt;java.util.logging.FileHandler&lt;/class-name&gt;
	 *                &lt;constructor&gt;
	 *                    &lt;constructor-param&gt;
	 *                        &lt;description&gt;pattern&lt;/description&gt;
	 *                        &lt;type&gt;java.lang.String&lt;/type&gt;
	 *                        &lt;value&gt;%h/java%u.log&lt;/value&gt;
	 *                    &lt;/constructor-param&gt;
	 *                    &lt;constructor-param&gt;
	 *                        &lt;description&gt;limit&lt;/description&gt;
	 *                        &lt;type&gt;int&lt;/type&gt;
	 *                        &lt;value&gt;50000&lt;/value&gt;
	 *                    &lt;/constructor-param&gt;
	 *                    &lt;constructor-param&gt;
	 *                        &lt;description&gt;count&lt;/description&gt;
	 *                        &lt;type&gt;int&lt;/type&gt;
	 *                        &lt;value&gt;1&lt;/value&gt;
	 *                    &lt;/constructor-param&gt;
	 *                    &lt;constructor-param&gt;
	 *                        &lt;description&gt;append&lt;/description&gt;
	 *                        &lt;type&gt;boolean&lt;/type&gt;
	 *                        &lt;value&gt;true&lt;/value&gt;
	 *                    &lt;/constructor-param&gt;
	 *                &lt;/constructor&gt;
	 *            &lt;/configured-object&gt;
	 *            &lt;level&gt;INFO&lt;/level&gt;
	 *            &lt;formatter&gt;java.util.logging.XMLFormatter&lt;/formatter&gt;
	 *            &lt;encoding&gt;euc-kr&lt;/encoding&gt;
	 *        &lt;/handler&gt;
	 *        &lt;handler&gt;
	 *            &lt;description&gt;Console handler.&lt;/description&gt;
	 *            &lt;handler-name&gt;consoleHandler&lt;/handler-name&gt;
	 *            &lt;configured-object&gt;
	 *                &lt;class-name&gt;java.util.logging.ConsoleHandler&lt;/class-name&gt;
	 *            &lt;/configured-object&gt;
	 *            &lt;level&gt;INFO&lt;/level&gt;
	 *            &lt;formatter&gt;java.util.logging.SimpleFormatter&lt;/formatter&gt;
	 *            &lt;encoding&gt;euc-kr&lt;/encoding&gt;
	 *        &lt;/handler&gt;
	 *        &lt;logger&gt;
	 *            &lt;description&gt;Framework default logger configuration.&lt;/description&gt;
	 *            &lt;logger-name&gt;frameworkLogger&lt;/logger-name&gt;
	 *            &lt;handler-name&gt;fileHandler&lt;/handler-name&gt;
	 *            &lt;handler-name&gt;consoleHandler&lt;/handler-name&gt;
	 *        &lt;/logger&gt;
	 *        &lt;logger&gt;
	 *            &lt;description&gt;Default logger configuration.&lt;/description&gt;
	 *            &lt;logger-name&gt;defaultLogger&lt;/logger-name&gt;
	 *            &lt;handler-name&gt;consoleHandler&lt;/handler-name&gt;
	 *        &lt;/logger&gt;
	 *        &lt;logger-mapping&gt;
	 *            &lt;description&gt;Mapping for framework classes&lt;/description&gt;
	 *            &lt;pattern&gt;net.ion.framework.*&lt;/pattern&gt;
	 *            &lt;logger-name&gt;frameworkLogger&lt;/logger-name&gt;
	 *        &lt;/logger-mapping&gt;
	 *        &lt;logger-mapping&gt;
	 *            &lt;description&gt;Default mapping.&lt;/description&gt;
	 *            &lt;pattern&gt;*&lt;/pattern&gt;
	 *            &lt;logger-name&gt;defaultLogger&lt;/logger-name&gt;
	 *        &lt;/logger-mapping&gt;
	 *    &lt;/logs&gt;
	 * </pre>
	 * 
	 * &lt;configured-object/&gt; 부분은 net.ion.framework.util.InstanceCreator을 따른다.
	 * 
	 * @param config
	 * @throws InitializingException
	 * 
	 * @see net.ion.framework.util.InstanceCreator
	 */
	public static void initialize(Configuration config) throws ConfigurationException {
		LogBroker.instance = new LogBroker(config);
	}

	/**
	 * mapping table에서 name에 해당되는 logger를 가져온다.
	 * 
	 * @param name
	 *            가져올 logger
	 * @return
	 */
	public static Logger getLogger(String name) {
		// name에 mapping되는 logger가 없으면
		// 논리적으로 Exception 을 내는 것이 적당하지만
		// 적당한 logger를 바로 선택해서 리턴한다.

		// jdk Logger 클래스의 getLogger(..)는 exception을 내지 않으므로 일관성에 따라
		// 그 메소드 역할을 하는 이 메소드는 exception을 내지 않도록 한다.

		if (instance != null)
			return Logger.getLogger(instance.resolve(name));
		else
			return Logger.getLogger(name);
	}

	/**
	 * class 이름을 name으로 하는 logger를 가져온다. (getLogger(String)의 wrapper)
	 * 
	 * @param cls
	 * @return
	 */
	public static Logger getLogger(Class<?> cls) {
		return getLogger(cls.getName());
	}

	/**
	 * object의 class name 명을 name으로 하는 logger를 가져온다. (getLogger(String)의 wrapper)
	 * 
	 * @param obj
	 * @return
	 */
	public static Logger getLogger(Object obj) {
		return getLogger(obj.getClass().getName());
	}
}
