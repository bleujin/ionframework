package net.ion.framework.configuration;

/**
 * Configuration 의 추상 클래스
 * 
 * <pre>
 * getAttribute( ), getValue( ), getChild( ), getChildren( ) 매소드를 제외한 나머지 메소드들을 구현하고 있다.
 * 
 * </pre>
 * 
 * @author Choi sei hwan <a href="mailto:sehan@i-on.net">Choi sei hwan</a>
 * @version 1.0
 */

public abstract class AbstractConfiguration implements Configuration {
	protected String tagName = null;

	/**
	 * 태그이름을 가져온다.
	 * 
	 * @return String 태그이름
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * Attribute 값을 가져온다.
	 * 
	 * @param attributeName
	 *            String Attribute 이름
	 * @param defaultValue
	 *            String Attribute 값이 설정되어 있지 않은 경우 지정되는 기본값
	 * @return String Attribute 값
	 */
	final public String getAttribute(String attributeName, String defaultValue) {
		if (getAttribute(attributeName) == "")
			return defaultValue;
		else
			return getAttribute(attributeName);
	}

	/**
	 * Attribute 값을 int형으로 casting 하여 가져온다.
	 * 
	 * @param attributeName
	 *            String Attribute 이름
	 * @throws ConfigurationException
	 *             Attribute 값이 설정되지 않았거나, 존재하지 않을경우, 또는 값이 int 형으로 casting이 불가할 경우 발생
	 * @return int Attribute 값
	 */
	final public int getAttributeAsInt(String attributeName) throws ConfigurationException {
		try {
			return Integer.parseInt(getAttribute(attributeName));
		} catch (NumberFormatException ex) {
			throw new ConfigurationException("Attribute value is not set OR does not exist : " + attributeName, ex);
		}
	}

	/**
	 * Attribute 값을 int형으로 casting 하여 가져온다.
	 * 
	 * @param attributeName
	 *            String Attribute 이름
	 * @param defaultValue
	 *            int Attribute 값이 설정되어 있지 않은 경우 지정되는 기본값
	 * @return int Attribute 값
	 */
	final public int getAttributeAsInt(String attributeName, int defaultValue) {
		try {
			return getAttributeAsInt(attributeName);
		} catch (ConfigurationException ex) {
			return defaultValue;
		}
	}

	/**
	 * Attribute 값을 float형으로 casting 하여 가져온다.
	 * 
	 * @param attributeName
	 *            String Attribute 이름
	 * @throws ConfigurationException
	 *             Attribute 값이 설정되지 않았거나, 존재하지 않을경우, 또는 값이 float 형으로 casting이 불가할 경우 발생
	 * @return float Attribute 값
	 */
	final public float getAttributeAsFloat(String attributeName) throws ConfigurationException {
		try {
			return Float.parseFloat(getAttribute(attributeName));
		} catch (NumberFormatException ex) {
			throw new ConfigurationException("Attribute value is not set OR does not exist : " + attributeName, ex);
		}
	}

	/**
	 * Attribute 값을 float 형으로 casting 하여 가져온다.
	 * 
	 * @param attributeName
	 *            String Attribute 이름
	 * @param defaultValue
	 *            float Attribute 값이 설정되어 있지 않은 경우 지정되는 기본값
	 * @return float Attribute 값
	 */
	final public float getAttributeAsFloat(String attributeName, float defaultValue) {
		try {
			return getAttributeAsFloat(attributeName);
		} catch (ConfigurationException ex) {
			return defaultValue;
		}
	}

	/**
	 * Attribute 값을 Boolean 형으로 casting 하여 가져온다.
	 * 
	 * @param attributeName
	 *            String Attribute 이름
	 * @throws ConfigurationException
	 *             Attribute 값이 설정되지 않았거나, 존재하지 않을경우 발생
	 * @return boolean Attribute 값
	 */
	final public boolean getAttributeAsBoolean(String attributeName) throws ConfigurationException {
		if (getAttribute(attributeName) == "")
			throw new ConfigurationException("Attribute value is not set OR does not exist : " + attributeName);
		else
			return Boolean.valueOf(getAttribute(attributeName)).booleanValue();
	}

	/**
	 * Attribute 값을 Boolean 형으로 casting 하여 가져온다.
	 * 
	 * @param attributeName
	 *            String Attribute 이름
	 * @param defaultValue
	 *            boolean Attribute 값이 설정되어 있지 않은 경우 지정되는 기본값
	 * @return boolean Attribute 값
	 */
	final public boolean getAttributeAsBoolean(String attributeName, boolean defaultValue) {
		try {
			return getAttributeAsBoolean(attributeName);
		} catch (ConfigurationException ex) {
			return defaultValue;
		}
	}

	/**
	 * 태그의 값을 가져온다.
	 * 
	 * @param defaultValue
	 *            String 값이 설정되어 있지 않은 경우 지정되는 기본값
	 * @return String 태그 값
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
	 * 태그의 값을 int형으로 casting 하여 가져온다.
	 * 
	 * @throws ConfigurationException
	 *             값이 설정되어 있지 않은 경우, 또는 값이 int 형으로 casting이 불가할 경우 발생
	 * @return int 태그 값
	 */
	final public int getValueAsInt() throws ConfigurationException {
		try {
			return Integer.parseInt(getValue());
		} catch (NumberFormatException ex) {
			throw new ConfigurationException("Tag value is not set : " + this.getTagName());
		}
	}

	/**
	 * 태그의 값을 int형으로 casting 하여 가져온다.
	 * 
	 * @param defaultValue
	 *            int 값이 설정되어 있지 않은 경우 지정되는 기본값
	 * @return int 태그 값
	 */
	final public int getValueAsInt(int defaultValue) {
		try {
			return getValueAsInt();
		} catch (ConfigurationException ex) {
			return defaultValue;
		}
	}

	/**
	 * 태그의 값을 float형으로 casting 하여 가져온다.
	 * 
	 * @throws ConfigurationException
	 *             값이 설정되어 있지 않은 경우, 또는 값이 float 형으로 casting이 불가할 경우 발생
	 * @return float 태그 값
	 */
	final public float getValueAsFloat() throws ConfigurationException {
		try {
			return Float.parseFloat(getValue());
		} catch (NumberFormatException ex) {
			throw new ConfigurationException("Tag value is not set : " + this.getTagName());
		}
	}

	/**
	 * 태그의 값을 float형으로 casting 하여 가져온다.
	 * 
	 * @param defaultValue
	 *            float 값이 설정되어 있지 않은 경우 지정되는 기본값
	 * @return float 태그 값
	 */
	final public float getValueAsFloat(float defaultValue) {
		try {
			return getValueAsFloat();
		} catch (ConfigurationException ex) {
			return defaultValue;
		}

	}

	/**
	 * 태그의 값을 boolean형으로 casting 하여 가져온다.
	 * 
	 * @throws ConfigurationException
	 *             값이 설정되어 있지 않은 경우 발생
	 * @return boolean 태그 값
	 */
	final public boolean getValueAsBoolean() throws ConfigurationException {
		if (getValue().length() == 0) {
			throw new ConfigurationException("Tag value is not set : " + this.getTagName());
		}
		return Boolean.valueOf(getValue()).booleanValue();
	}

	/**
	 * 태그의 값을 boolean형으로 casting 하여 가져온다.
	 * 
	 * @param defaultValue
	 *            boolean 값이 설정되어 있지 않은 경우 지정되는 기본값
	 * @return boolean 태그 값
	 */
	final public boolean getValueAsBoolean(boolean defaultValue) {
		try {
			return getValueAsBoolean();
		} catch (ConfigurationException ex) {
			return defaultValue;
		}
	}
}
