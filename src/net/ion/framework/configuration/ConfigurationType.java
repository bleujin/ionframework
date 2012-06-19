package net.ion.framework.configuration;

/**
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 * 
 *          <pre>
 * Configuration에 대한 Type 을 정의
 * 
 * DEFAULT = net.ion.framework.configuration.DefaultConfiguration
 * 
 * </pre>
 * 
 */

public class ConfigurationType {
	private String className = null;

	public static final ConfigurationType DEFAULT = new ConfigurationType("net.ion.framework.configuration.DefaultConfiguration");

	public ConfigurationType(String className) {
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
