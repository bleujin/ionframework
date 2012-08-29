package net.ion.framework.configuration;

/**
 * ȯ�溯������ �������� �����ϴ� �⺻���� ���� �޼ҵ���� �����ϴ� Interface
 * 
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 */

public interface Configuration {
	/**
	 * ���� Configuration ���� childElementName�̸��� �ڽ� Element �� �ϳ������´�. childElementName�� ������ �ڽ��� �ϳ��̻��ϰ�쿡�� ����ó���� �߰ߵǴ� �ڽĸ��� �����´�.
	 * 
	 * @param childElementName
	 *            String �ڽ� Element �� �̸�
	 * @throws NotFoundXmlTagException
	 *             childElementName�� �������� �ʴ� ��� �߻�
	 * @throws NotBuildException
	 *             ȯ�漳������(.xml)�� ���� Build �Ǿ����� ���� ��� �߻�
	 * @throws ConfigurationException
	 *             ȯ�漳������ ó���� ������ �߻��Ͽ��� ��� �߻�
	 * @return Configuration �ش� Child Element�� Configuration �ν��Ͻ�
	 */
	Configuration getChild(String childElementName) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

	/**
	 * ���� Configuration ���� childElementName�̸��� �ڽ� Element �� ��� �����´�.
	 * 
	 * @param childElementName
	 *            String �ڽ� Element �� �̸�
	 * @throws NotFoundXmlTagException
	 *             childElementName�� �������� �ʴ� ��� �߻�
	 * @throws NotBuildException
	 *             ȯ�漳������(.xml)�� ���� Build �Ǿ����� ���� ��� �߻�
	 * @throws ConfigurationException
	 *             ȯ�漳������ ó���� ������ �߻��Ͽ��� ��� �߻�
	 * @return Configuration[]
	 */
	Configuration[] getChildren(String childElementName) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

	// Configuration[] getChildren();

	// String getLocation();

	/**
	 * ���� Configuration �� Tag �̸��� �����´�.
	 * 
	 * @return ���� Tag �� �̸�
	 */
	String getTagName();

	/**
	 * Attribute ���� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @return String Attribute ��
	 */
	String getAttribute(String attributeName);

	/**
	 * Attribute ���� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @param defaultValue
	 *            String Attribute ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return String Attribute ��
	 */
	String getAttribute(String attributeName, String defaultValue);

	/**
	 * Attribute ���� int������ casting �Ͽ� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @throws ConfigurationException
	 *             Attribute ���� �������� �ʾҰų�, �������� �������, �Ǵ� ���� int ������ casting�� �Ұ��� ��� �߻�
	 * @return int Attribute ��
	 */
	int getAttributeAsInt(String attributeName) throws ConfigurationException;

	/**
	 * Attribute ���� int������ casting �Ͽ� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @param defaultValue
	 *            int Attribute ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return int Attribute ��
	 */
	int getAttributeAsInt(String attributeName, int defaultValue);

	/**
	 * Attribute ���� float������ casting �Ͽ� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @throws ConfigurationException
	 *             Attribute ���� �������� �ʾҰų�, �������� �������, �Ǵ� ���� float ������ casting�� �Ұ��� ��� �߻�
	 * @return float Attribute ��
	 */
	float getAttributeAsFloat(String attributeName) throws ConfigurationException;

	/**
	 * Attribute ���� float ������ casting �Ͽ� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @param defaultValue
	 *            float Attribute ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return float Attribute ��
	 */
	float getAttributeAsFloat(String attributeName, float defaultValue);

	/**
	 * Attribute ���� Boolean ������ casting �Ͽ� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @throws ConfigurationException
	 *             Attribute ���� �������� �ʾҰų�, �������� ������� �߻�
	 * @return boolean Attribute ��
	 */
	boolean getAttributeAsBoolean(String attributeName) throws ConfigurationException;

	/**
	 * Attribute ���� Boolean ������ casting �Ͽ� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @param defaultValue
	 *            boolean Attribute ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return boolean Attribute ��
	 */
	boolean getAttributeAsBoolean(String attributeName, boolean defalutValue);

	/**
	 * �±��� ���� �����´�.
	 * 
	 * @return String �±� ��
	 */
	String getValue();

	String getValue(String defaultValue);

	/**
	 * �±��� ���� int������ casting �Ͽ� �����´�.
	 * 
	 * @throws ConfigurationException
	 *             ���� �����Ǿ� ���� ���� ���, �Ǵ� ���� int ������ casting�� �Ұ��� ��� �߻�
	 * @return int �±� ��
	 */
	int getValueAsInt() throws ConfigurationException;

	/**
	 * �±��� ���� int������ casting �Ͽ� �����´�.
	 * 
	 * @param defaultValue
	 *            int ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return int �±� ��
	 */
	int getValueAsInt(int defaultValue);

	/**
	 * �±��� ���� float������ casting �Ͽ� �����´�.
	 * 
	 * @throws ConfigurationException
	 *             ���� �����Ǿ� ���� ���� ���, �Ǵ� ���� float ������ casting�� �Ұ��� ��� �߻�
	 * @return float �±� ��
	 */
	float getValueAsFloat() throws ConfigurationException;

	/**
	 * �±��� ���� float������ casting �Ͽ� �����´�.
	 * 
	 * @param defaultValue
	 *            float ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return float �±� ��
	 */
	float getValueAsFloat(float defaultValue);

	/**
	 * �±��� ���� boolean������ casting �Ͽ� �����´�.
	 * 
	 * @throws ConfigurationException
	 *             ���� �����Ǿ� ���� ���� ��� �߻�
	 * @return boolean �±� ��
	 */
	boolean getValueAsBoolean() throws ConfigurationException;

	/**
	 * �±��� ���� boolean������ casting �Ͽ� �����´�.
	 * 
	 * @param defaultValue
	 *            boolean ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return boolean �±� ��
	 */
	boolean getValueAsBoolean(boolean defaultValue);

	/**
	 * ���� Tag �� attributeName�� ������ attribute�� �����ϸ� ���� attributeValue �� �����ϰ� �������� ������ attribute �� �߰��Ѵ�.
	 * 
	 * @param attributeName
	 * @param attributeValue
	 * @throws ConfigurationException
	 *             attributeName �� illegal character�� ���ԵǾ�������
	 */
	void setAttribute(String attributeName, String attributeValue) throws ConfigurationException;

	// void setValue( String value );

	String getXML() throws ConfigurationException;
}
