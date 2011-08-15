package net.ion.framework.configuration;

/**
 * @author Choi sei hwan<a href="mailto:sehan@i-on.net">sehan@i-on.net</a>
 * @version 1.0
 * 
 *          <pre>
 * ConfigurationFactory�� ���� Type �� ����
 * 
 * DEFAULT =  net.ion.framework.configuration.DefaultConfigurationFactory
 * 
 * ConfigurationFactory�� ������ implement �� Class�� ��Ÿ����.
 * �⺻���� DEFAULT ���� ����ϸ� net.ion.framework.configuration.DefaultConfigurationFactory Ŭ������ �����Ѵ�.
 * ���Ŀ� ���ο� ConfigurationFactory�� �߰��Ѵٸ� static final ������ ���� �߰��ϸ� �ȴ�.
 * 
 * </pre>
 */

public class ConfigurationFactoryType {
	private String className = null;

	public static final ConfigurationFactoryType DEFAULT = new ConfigurationFactoryType("net.ion.framework.configuration.DefaultConfigurationFactory");

	public ConfigurationFactoryType(String className) {
		this.className = className;
	}

	/**
	 * Ÿ�Ը��� �����´�.
	 * 
	 * @return Ÿ�Ը�
	 */
	public String getClassName() {
		return className;
	}

}
