package net.ion.framework.configuration;

/**
 * @author Choi sei hwan<a href="mailto:sehan@i-on.net">sehan@i-on.net</a>
 * @version 1.0
 * 
 *          <pre>
 * ConfigurationFactory에 대한 Type 을 정의
 * 
 * DEFAULT =  net.ion.framework.configuration.DefaultConfigurationFactory
 * 
 * ConfigurationFactory가 실제로 implement 된 Class를 나타낸다.
 * 기본으로 DEFAULT 값을 사용하며 net.ion.framework.configuration.DefaultConfigurationFactory 클래스를 제공한다.
 * 추후에 새로운 ConfigurationFactory를 추가한다면 static final 변수로 새로 추가하면 된다.
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
	 * 타입명을 가져온다.
	 * 
	 * @return 타입명
	 */
	public String getClassName() {
		return className;
	}

}
