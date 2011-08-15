package net.ion.framework.configuration;

/**
 * ȯ�漳���� �ʿ��� class �� implement �Ѵ�.
 * 
 * @author Choi sei hwan<a href="mailto:sehan@i-on.net">sehan@i-on.net</a>
 * @version 1.0
 */

public interface Configurable {
	/**
	 * Configuration �� �Ѱ��ش�. �� �޼ҵ�� �����ڰ� ȣ����Ŀ� �ٸ��޼ҵ尡 ȣ��Ǳ����� �ݵ�� ����Ǿ���Ѵ�.
	 * 
	 * @param configuration
	 *            ������ Configuration class
	 * @throws ConfigurationException
	 *             configure �����߿� ������ ��������
	 */
	void configure(Configuration configuration) throws ConfigurationException;
}
