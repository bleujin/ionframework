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
 * configuration�� ���� logger�� �����ϰ� pattern�� mapping�Ǿ� �ִ� logger�� ��ȯ�Ѵ�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class LogBroker {
	private static LogBroker instance = null;

	// pattern to logger mapping

	// pattern�� logger name mapping
	// ������ pattern matching�� ���� ������ �ʿ��ϴ�.(������� ��) ���� ����� PatternComparator �� ������.
	private TreeMap<String, String> patternMapping = null;

	// name�� logger name mapping
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

		// handler�� �����ϰ� logger�� �����ϰ� mapping table�� �ִ´�.

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

					// ���̺� ��� ���ѵд�.
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
	 * name�� mapping�Ǵ� loggerName�� �����´�.
	 * 
	 * @param name
	 * @return
	 */
	private String resolve(String name) {
		// cachedMapping���� ���� �о� ����.
		String loggerName = (String) nameMapping.get(name);

		// mapping���� name�� ���� pattern�� resolve �Ѵ�.
		if (loggerName == null) {
			// pattern���� name�� ã�´�.
			String[] patterns = patternMapping.keySet().toArray(new String[0]);
			for (int i = 0; i < patterns.length; ++i) {
				if (isCorrespondingClass(patterns[i], name)) {
					loggerName = patternMapping.get(patterns[i]);

					// ���� ����� ���� loggerName�� cachedMapping�� �־� �д�.
					nameMapping.put(name, loggerName);

					break;
				}
			}
		}

		// System.out.println(patternMapping);
		// System.out.println(nameMapping);

		// mapping ������ ������ name �״�� return
		if (loggerName != null)
			return loggerName;
		else
			return name;
	}

	/**
	 * class�� package�� ���ϴ��� �׽�Ʈ
	 * 
	 * @param packageName
	 * @param className
	 * @return
	 */
	private boolean isCorrespondingClass(String packageName, String className) {
		// packageName�� ������ *�� ���ش�.
		packageName = packageName.substring(0, packageName.length() - 1);

		// packageName �̸��� className���� ũ�� �ݵ�� �ƴ���..
		if (packageName.length() > className.length())
			return false;
		className = className.substring(0, packageName.length());

		// packageName ��ŭ className�� ������ OK
		if (className.endsWith(packageName))
			return true;
		else
			return false;
	}

	/**
	 * ���ο� mapping�� �߰��Ѵ�.
	 * 
	 * keyName loggerName a.b.c.* -> logger1 a.b.c.A -> logger2 a.b.c.d.* -> logger3
	 * 
	 * @param keyName
	 *            pattern �Ǵ� name key
	 * @param loggerName
	 *            pattern�� mapping�� loggerName
	 */
	private void addMapping(String keyName, String loggerName) {
		if (keyName.equals("*") || keyName.endsWith(".*"))
			patternMapping.put(keyName, loggerName);
		else
			nameMapping.put(keyName, loggerName);
	}

	/**
	 * configuration ������ log handler,logger,mapping�� �ʱ�ȭ �Ѵ�.
	 * 
	 * ���ο� mapping���� ���� ��ü �ȴ�. (������ mapping ���� �������� ����)
	 * 
	 *<pre>
	 * * ���� ���� :
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
	 * &lt;configured-object/&gt; �κ��� net.ion.framework.util.InstanceCreator�� ������.
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
	 * mapping table���� name�� �ش�Ǵ� logger�� �����´�.
	 * 
	 * @param name
	 *            ������ logger
	 * @return
	 */
	public static Logger getLogger(String name) {
		// name�� mapping�Ǵ� logger�� ������
		// �������� Exception �� ���� ���� ����������
		// ������ logger�� �ٷ� �����ؼ� �����Ѵ�.

		// jdk Logger Ŭ������ getLogger(..)�� exception�� ���� �����Ƿ� �ϰ����� ����
		// �� �޼ҵ� ������ �ϴ� �� �޼ҵ�� exception�� ���� �ʵ��� �Ѵ�.

		if (instance != null)
			return Logger.getLogger(instance.resolve(name));
		else
			return Logger.getLogger(name);
	}

	/**
	 * class �̸��� name���� �ϴ� logger�� �����´�. (getLogger(String)�� wrapper)
	 * 
	 * @param cls
	 * @return
	 */
	public static Logger getLogger(Class<?> cls) {
		return getLogger(cls.getName());
	}

	/**
	 * object�� class name ���� name���� �ϴ� logger�� �����´�. (getLogger(String)�� wrapper)
	 * 
	 * @param obj
	 * @return
	 */
	public static Logger getLogger(Object obj) {
		return getLogger(obj.getClass().getName());
	}
}
