package net.ion.framework.res;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import net.ion.framework.util.StringUtil;

/**
 * �� / ������ �޼����� �����´�. �޼��� ���ҽ� ���� ���´� java.util.PropertyResourceBundle �� ���İ� �����ϴ�. .properties �� ����ϴ� �ٱ��� ���ڴ� �ݵ�� <b>UNICODE ��</b>���� ����� ���ڼ¿��� �߻��ϴ� ������ �ذ��� �� �ִ�. <br>
 * <br>
 * <i>net.ion.framework.res.StringConverter �� �����ϸ� UNICODE�Է��� ���� �� �� �ִ�.<br>
 * java -classpath . net.ion.framework.res.StringConverter [ENTER]</i>
 * 
 * <pre>
 * ex)
 *  ���� .properties ���Ͽ� ������ ������ ��� ���� ���
 * 
 *      net/ion/ics/res/text_en_US.properties
 *          welcome = welcome to {0}.
 * 
 *      net/ion/ics/res/text_ko_KR.properties
 *          welcome = {0} \uC5D0 \uC624\uC2E0 \uAC83\uC744 \uD658\uC601\uD569\uB2C8\uB2E4.
 *              ����� ���� ���ڴ� "�� ���� ���� ȯ���մϴ�." �� <b>UNICODE ��</b>���� ���� ���̴�.
 * 
 *      net/ion/ics/res/text_ja_JP.properties
 *          welcome = {0} �Ϻ���� �� ���� �����ڵ�ȭ ��Ų ���ڿ� ��.��
 * 
 * 
 *  Resources m = Resources.getResources("net.ion.ics.res.text",Locale.KOREA);
 *  m.getResources("welcome","�� Ȩ������");
 *      -> �� Ȩ�������� ���� ���� ȯ���մϴ�.
 * 
 * 
 * 
 *  Resources m = Resources.getResources("net.ion.ics.res.text",Locale.US);
 *  m.getResources("welcome","My homepage");
 *      -> welcome to my homepage.
 * 
 * 
 *  Resources m = Resources.getResources("net.ion.ics.res.text",Locale.JAPAN);
 *  m.getResources("welcome","�͵��ÿ� ��.��");
 *      -> �͵��ÿ� ��.�� [�Ϻ���� ��¼�� ��¼��...]
 * </pre>
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 * 
 * @see java.util.ResourceBundle
 * @see java.util.PropertyResourceBundle
 * @see java.text.MessageFormat
 */
public class Resources {
	private ResourceBundle bundle = null;

	protected Resources() {
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	/**
	 * ResourceBundle �� Key�� ��ȯ
	 * 
	 * @return Enumeration
	 */
	public Enumeration<String> getKeys() {
		return bundle.getKeys();
	}

	/**
	 * ResourceBundle �� Locale�� �����´�.
	 * 
	 * @return Locale
	 */
	public Locale getLocale() {
		return bundle.getLocale();
	}

	/**
	 * key ���� �����´�.
	 * 
	 * @param key
	 *            String
	 * @return String
	 */
	public String get(String key) {
		return get(key, new Object[0]);
	}

	/**
	 * Key ���� �����´�.
	 * 
	 * key�� �ش��ϴ� ���ڿ��� "max ? ~ min ?" ���, "?" �κп� �� ������ args�� �ȴ�. �� args={ 10, 0 } �̶�� "max 10 ~ min 0"�� return �Ѵ�.
	 * 
	 * @param key
	 *            String
	 * @param args
	 *            Object[]
	 * @return String
	 */
	public String get(String key, Object[] args) {
		try {
			String s = bundle.getString(key);
			return MessageFormat.format(s, args);
		} catch (MissingResourceException e) {
			// String s="[[key="+key+",value=";
			return "[[key=" + key + ",value=" + StringUtil.join(args, " ") + "]]";
		}
	}

	/**
	 * Key ���� �����´�.
	 * 
	 * get( String key, Object[] args )�� �����ϸ�, �ٸ����� argument�� �ϳ��� ��츸�� ó���Ѵ�.
	 * 
	 * @param key
	 *            String
	 * @param arg0
	 *            Object
	 * @return String
	 */
	public String get(String key, Object arg0) {
		return get(key, new Object[] { arg0 });
	}

	/**
	 * get( String key, Object[] args )�� �����ϸ�, �ٸ����� argument�� �ΰ��� ��츸�� ó���Ѵ�.
	 * 
	 * @param key
	 *            String
	 * @param arg0
	 *            Object
	 * @param arg1
	 *            Object
	 * @return String
	 */
	public String get(String key, Object arg0, Object arg1) {
		return get(key, new Object[] { arg0, arg1 });
	}

	/**
	 * get( String key, Object[] args )�� �����ϸ�, �ٸ����� argument�� ������ ��츸�� ó���Ѵ�.
	 * 
	 * @param key
	 *            String
	 * @param arg0
	 *            Object
	 * @param arg1
	 *            Object
	 * @param arg2
	 *            Object
	 * @return String
	 */
	public String get(String key, Object arg0, Object arg1, Object arg2) {
		return get(key, new Object[] { arg0, arg1, arg2 });
	}

	/**
	 * Resources Instance�� �����´�.
	 * 
	 * <pre>
	 * ex)
	 *  Resources m = Resources.getResources("net.ion.ics.res.text");
	 * </pre>
	 * 
	 * @param baseName
	 *            String java.util.ResourceBundle�� getBundle�� �����ϴ�.
	 * @return Resources
	 */
	public static Resources getResources(String baseName) {
		// Resources messages = new Resources();
		// messages.bundle = PropertyResourceBundle.getBundle( baseName );
		// return messages;
		return XMLResourceFactory.getResource(baseName);
	}

	/**
	 * Resources Instance�� �����´�.
	 * 
	 * <pre>
	 * ex)
	 *  Resources m = Resources.getResources("net.ion.ics.res.text",new Locale("ko","KR"));
	 *  �� ��� /net/ion/ics/res/text_ko_KR.properties ���� ResourceBundle�� �����´�.
	 * </pre>
	 * 
	 * @param baseName
	 *            String java.util.ResourceBundle�� getBundle�� �����ϴ�.
	 * @param locale
	 *            Locale ������ resource bundle�� Locale
	 * @return Resources
	 */
	public static Resources getResources(String baseName, Locale locale) {
		// Resources messages = new Resources();
		// messages.bundle = PropertyResourceBundle.getBundle( baseName, locale );
		//
		// return messages;
		return XMLResourceFactory.getResource(baseName, locale.getLanguage());
	}

	/**
	 * Resources Instance�� �����´�.
	 * 
	 * @param baseName
	 *            String
	 * @param locale
	 *            Locale
	 * @param loader
	 *            ClassLoader
	 * @return Resources
	 */
	public static Resources getResources(String baseName, Locale locale, ClassLoader loader) {
		// Resources messages = new Resources();
		// messages.bundle = PropertyResourceBundle.getBundle( baseName, locale, loader );
		// return messages;
		return getResources(baseName, locale);
	}

	/**
	 * Resource Instance�� �����´�.
	 * 
	 * @param baseName
	 *            String
	 * @param response
	 *            HttpServletResponse HttpServletResponse �� Locale�� �����Ͽ� Resources�� �����´�.
	 * @return Resources
	 */
	public static Resources getResources(String baseName, HttpServletResponse response) {
		// Locale locale = response.getLocale();
		// Resources messages = new Resources();
		// messages.bundle = PropertyResourceBundle.getBundle( baseName, locale );
		// return messages;
		return getResources(baseName, response.getLocale());
	}
}
