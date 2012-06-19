package net.ion.framework.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Hashtable;

import org.xml.sax.InputSource;

/**
 * <pre>
 * Configuration �� �����Ѵ�.
 * 
 * ConfigurationFactory �� Configuration �ν��Ͻ��� �����Ѵ�.
 * ConfiguartionFactory �� newInstance( String instanceKeyName )�� ���� �����Ǹ� KeyName �� ������ �ְ� ������ factory ��
 * ���������� ����ȴ�. �ѹ������� factory �� �ٽ� �������� �ʰ� �̹̻����Ǿ��ִ� �ν��Ͻ��� �����ش�.
 * 
 * ConfigurationFactory �����ϱ�
 * 
 *      ConfigurationFactory factory = ConfigurationFactory.newInstance( keyname );
 *      factory.build( configurationFileName );  // xml ���� �о���̱�
 * 
 * 
 * 
 * </pre>
 * 
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 */

public abstract class ConfigurationFactory {
	// ������ ConfigurationFactory �ν��Ͻ��� �����ϴ� hashtable
	private static Hashtable<String, ConfigurationFactory> configurationFactoryCache = new Hashtable<String, ConfigurationFactory>();

	// factory�� �����̸�
	protected String instanceKeyName = null;

	// protected ConfigurationType configurationType = null;

	// ���� ���ǰ� �ִ� concrete ConfigurationFactory Ŭ���� �� ��Ű����
	private static ConfigurationFactoryType configurationFactoryType = ConfigurationFactoryType.DEFAULT;

	/**
	 * ConfigurationFactory �ν��Ͻ��� �����´�. Singleton ������� �ν��Ͻ��� �����´�.
	 * 
	 * @param instanceKeyName
	 *            String ������ �ν��Ͻ� �̸�
	 * @throws ConfigurationException
	 * @return ConfigurationFactory
	 */
	public static ConfigurationFactory getInstance(String instanceKeyName) throws ConfigurationException {
		// �̹� �����Ǿ��ִ� �ν��Ͻ��� �ִ��� ã�ƺ���.
		ConfigurationFactory instance = (ConfigurationFactory) configurationFactoryCache.get(instanceKeyName);

		if (instance == null) {
			// ã�� ���Ͽ����� ���� �����Ѵ�.
			try {
				Constructor<?> constructor = Class.forName(configurationFactoryType.getClassName()).getConstructor(new Class[] { String.class });
				instance = (ConfigurationFactory) (constructor.newInstance(new Object[] { instanceKeyName }));
			} catch (Exception ex) {
				throw new ConfigurationException("ConfigurationFactory instance not Create : " + ex.getLocalizedMessage(), ex);
			}

			// ���� ������ �ν��Ͻ��� �����Ѵ�.
			configurationFactoryCache.put(instanceKeyName, instance);
		}

		return instance;
	}

	/**
	 * filename �� �ش��ϴ� xml config ������ �о���δ�.
	 * 
	 * @param filename
	 *            String ȯ�漳���� ���� XML ���ϰ��
	 * @throws ConfigurationBuildException
	 */
	public void build(String filename) throws ConfigurationBuildException {
		build(new File(filename));
	}

	/**
	 * File Class �� ���ؼ� xml config ������ �о���δ�.
	 * 
	 * @param configFile
	 *            File ȯ�漳���� ���� FILE Class
	 * @throws ConfigurationBuildException
	 */
	public void build(File configFile) throws ConfigurationBuildException {
		try {
			File cFile = null;
			cFile = configFile.getCanonicalFile();
			FileInputStream fis = new FileInputStream(cFile);
			build(fis);
		} catch (IOException ex) {
			throw new ConfigurationBuildException("file not found : " + configFile.getName(), ex);
		}
	}

	/**
	 * InputStream �� ���ؼ� xml config ������ �е��δ�.
	 * 
	 * @param inputstream
	 *            InputStream ȯ�漳���� ���� InputStream Class
	 * @throws ConfigurationBuildException
	 */
	public void build(InputStream inputstream) throws ConfigurationBuildException {
		build(new InputSource(inputstream));
	}

	/**
	 * inputsource �� ���ؼ� xml config ������ �о���δ�.
	 * 
	 * @param inputsource
	 *            InputSource ȯ�漳���� ���� InputSource Class
	 * @throws ConfigurationBuildException
	 */
	public abstract void build(InputSource inputsource) throws ConfigurationBuildException;

	/**
	 * elementPathString�� �ش��ϴ� Configuration �ν��Ͻ��� �����´�.
	 * 
	 * @param elementPathString
	 *            String ������ Configuration �ν��Ͻ��� path ���ڿ�
	 * @throws NotFoundXmlTagException
	 * @throws NotBuildException
	 * @throws ConfigurationException
	 * @return Configuration
	 */
	public abstract Configuration getConfiguration(String elementPathString) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

	/**
	 * elementPathString�� �ش��ϴ� Configuration �ν��Ͻ����� �����´�.
	 * 
	 * @param elementPathString
	 *            String ������ Configuration �ν��Ͻ��� path ���ڿ�
	 * @throws NotFoundXmlTagException
	 * @throws NotBuildException
	 * @throws ConfigurationException
	 * @return Configuration[]
	 */
	public abstract Configuration[] getConfigurations(String elementPathString) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

	// /**
	// * ���� Factory �� Ŭ�����̸� �����ϱ�
	// *
	// * @param className - Factory Ŭ���� ��
	// */
	// public static void setClassName( String className )
	// {
	// ConfigurationFactory.className = className;
	// }
	//
	// /**
	// * ���� Factory �� Ŭ�����̸� ��������
	// *
	// * @return ���� Factory �� Ŭ�����̸�
	// */
	// public static String getClassName()
	// {
	// return className;
	// }

	/**
	 * ���� Factory���� �����ϴ� Configuration �� Type �����ϱ�
	 * 
	 * @param configType
	 */
	// public void setConfigurationType( ConfigurationType configType )
	// {
	// this.configurationType = configType;
	// }

	/**
	 * ���� Factory���� �����ϴ� Configuration �� Type ��������
	 * 
	 * @return ConfiguraitonType. �⺻���� Configuration.DEFAULT
	 */
	// public ConfigurationType getConfigurationType()
	// {
	// return configurationType;
	// }

	/**
	 * ConfigurationFactory �� InstanceKey �̸��� �����´�.
	 * 
	 * @return String
	 */
	public String getInstanceKeyName() {
		return instanceKeyName;
	}

	public ConfigurationFactoryType getConfigurationFactoryType() {
		return configurationFactoryType;
	}

	@SuppressWarnings("static-access")
	public void setConfigurationFactoryType(ConfigurationFactoryType configurationFactoryType) {
		this.configurationFactoryType = configurationFactoryType;
	}

	protected static void removeConfiguration(String instanceKeyName) {
		configurationFactoryCache.remove(instanceKeyName);
	}

}
