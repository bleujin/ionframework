package net.ion.framework.configuration;

/**
 * Configuration �� �߻� Ŭ����
 * 
 * <pre>
 * getAttribute( ), getValue( ), getChild( ), getChildren( ) �żҵ带 ������ ������ �޼ҵ���� �����ϰ� �ִ�.
 * 
 * </pre>
 * 
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 */

public abstract class AbstractConfiguration implements Configuration {
	protected String tagName = null;

	/**
	 * �±��̸��� �����´�.
	 * 
	 * @return String �±��̸�
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * Attribute ���� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @param defaultValue
	 *            String Attribute ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return String Attribute ��
	 */
	final public String getAttribute(String attributeName, String defaultValue) {
		if (getAttribute(attributeName) == "")
			return defaultValue;
		else
			return getAttribute(attributeName);
	}

	/**
	 * Attribute ���� int������ casting �Ͽ� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @throws ConfigurationException
	 *             Attribute ���� �������� �ʾҰų�, �������� �������, �Ǵ� ���� int ������ casting�� �Ұ��� ��� �߻�
	 * @return int Attribute ��
	 */
	final public int getAttributeAsInt(String attributeName) throws ConfigurationException {
		try {
			return Integer.parseInt(getAttribute(attributeName));
		} catch (NumberFormatException ex) {
			throw new ConfigurationException("Attribute value is not set OR does not exist : " + attributeName, ex);
		}
	}

	/**
	 * Attribute ���� int������ casting �Ͽ� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @param defaultValue
	 *            int Attribute ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return int Attribute ��
	 */
	final public int getAttributeAsInt(String attributeName, int defaultValue) {
		try {
			return getAttributeAsInt(attributeName);
		} catch (ConfigurationException ex) {
			return defaultValue;
		}
	}

	/**
	 * Attribute ���� float������ casting �Ͽ� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @throws ConfigurationException
	 *             Attribute ���� �������� �ʾҰų�, �������� �������, �Ǵ� ���� float ������ casting�� �Ұ��� ��� �߻�
	 * @return float Attribute ��
	 */
	final public float getAttributeAsFloat(String attributeName) throws ConfigurationException {
		try {
			return Float.parseFloat(getAttribute(attributeName));
		} catch (NumberFormatException ex) {
			throw new ConfigurationException("Attribute value is not set OR does not exist : " + attributeName, ex);
		}
	}

	/**
	 * Attribute ���� float ������ casting �Ͽ� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @param defaultValue
	 *            float Attribute ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return float Attribute ��
	 */
	final public float getAttributeAsFloat(String attributeName, float defaultValue) {
		try {
			return getAttributeAsFloat(attributeName);
		} catch (ConfigurationException ex) {
			return defaultValue;
		}
	}

	/**
	 * Attribute ���� Boolean ������ casting �Ͽ� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @throws ConfigurationException
	 *             Attribute ���� �������� �ʾҰų�, �������� ������� �߻�
	 * @return boolean Attribute ��
	 */
	final public boolean getAttributeAsBoolean(String attributeName) throws ConfigurationException {
		if (getAttribute(attributeName) == "")
			throw new ConfigurationException("Attribute value is not set OR does not exist : " + attributeName);
		else
			return Boolean.valueOf(getAttribute(attributeName)).booleanValue();
	}

	/**
	 * Attribute ���� Boolean ������ casting �Ͽ� �����´�.
	 * 
	 * @param attributeName
	 *            String Attribute �̸�
	 * @param defaultValue
	 *            boolean Attribute ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return boolean Attribute ��
	 */
	final public boolean getAttributeAsBoolean(String attributeName, boolean defaultValue) {
		try {
			return getAttributeAsBoolean(attributeName);
		} catch (ConfigurationException ex) {
			return defaultValue;
		}
	}

	/**
	 * �±��� ���� �����´�.
	 * 
	 * @param defaultValue
	 *            String ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return String �±� ��
	 */
	final public String getValue(String defaultValue) {
		if (getValue().length() == 0) {
			return defaultValue;
		} else {
			try {
				return getValue();
			} catch (Exception ex) {
				return defaultValue;
			}
		}
	}

	/**
	 * �±��� ���� int������ casting �Ͽ� �����´�.
	 * 
	 * @throws ConfigurationException
	 *             ���� �����Ǿ� ���� ���� ���, �Ǵ� ���� int ������ casting�� �Ұ��� ��� �߻�
	 * @return int �±� ��
	 */
	final public int getValueAsInt() throws ConfigurationException {
		try {
			return Integer.parseInt(getValue());
		} catch (NumberFormatException ex) {
			throw new ConfigurationException("Tag value is not set : " + this.getTagName());
		}
	}

	/**
	 * �±��� ���� int������ casting �Ͽ� �����´�.
	 * 
	 * @param defaultValue
	 *            int ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return int �±� ��
	 */
	final public int getValueAsInt(int defaultValue) {
		try {
			return getValueAsInt();
		} catch (ConfigurationException ex) {
			return defaultValue;
		}
	}

	/**
	 * �±��� ���� float������ casting �Ͽ� �����´�.
	 * 
	 * @throws ConfigurationException
	 *             ���� �����Ǿ� ���� ���� ���, �Ǵ� ���� float ������ casting�� �Ұ��� ��� �߻�
	 * @return float �±� ��
	 */
	final public float getValueAsFloat() throws ConfigurationException {
		try {
			return Float.parseFloat(getValue());
		} catch (NumberFormatException ex) {
			throw new ConfigurationException("Tag value is not set : " + this.getTagName());
		}
	}

	/**
	 * �±��� ���� float������ casting �Ͽ� �����´�.
	 * 
	 * @param defaultValue
	 *            float ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return float �±� ��
	 */
	final public float getValueAsFloat(float defaultValue) {
		try {
			return getValueAsFloat();
		} catch (ConfigurationException ex) {
			return defaultValue;
		}

	}

	/**
	 * �±��� ���� boolean������ casting �Ͽ� �����´�.
	 * 
	 * @throws ConfigurationException
	 *             ���� �����Ǿ� ���� ���� ��� �߻�
	 * @return boolean �±� ��
	 */
	final public boolean getValueAsBoolean() throws ConfigurationException {
		if (getValue().length() == 0) {
			throw new ConfigurationException("Tag value is not set : " + this.getTagName());
		}
		return Boolean.valueOf(getValue()).booleanValue();
	}

	/**
	 * �±��� ���� boolean������ casting �Ͽ� �����´�.
	 * 
	 * @param defaultValue
	 *            boolean ���� �����Ǿ� ���� ���� ��� �����Ǵ� �⺻��
	 * @return boolean �±� ��
	 */
	final public boolean getValueAsBoolean(boolean defaultValue) {
		try {
			return getValueAsBoolean();
		} catch (ConfigurationException ex) {
			return defaultValue;
		}
	}
}
