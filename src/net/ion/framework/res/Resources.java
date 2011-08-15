package net.ion.framework.res;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

import net.ion.framework.util.StringUtil;

/**
 * 언어별 / 국가별 메세지를 가져온다. 메세지 리소스 저장 형태는 java.util.PropertyResourceBundle 의 형식과 동일하다. .properties 에 기록하는 다국어 문자는 반드시 <b>UNICODE 값</b>으로 적어야 문자셋에서 발생하는 문제를 해결할 수 있다. <br>
 * <br>
 * <i>net.ion.framework.res.StringConverter 를 실행하면 UNICODE입력을 쉽게 할 수 있다.<br>
 * java -classpath . net.ion.framework.res.StringConverter [ENTER]</i>
 * 
 * <pre>
 * ex)
 *  각각 .properties 파일에 다음의 내용이 들어 있을 경우
 * 
 *      net/ion/ics/res/text_en_US.properties
 *          welcome = welcome to {0}.
 * 
 *      net/ion/ics/res/text_ko_KR.properties
 *          welcome = {0} \uC5D0 \uC624\uC2E0 \uAC83\uC744 \uD658\uC601\uD569\uB2C8\uB2E4.
 *              참고로 위의 문자는 "에 오신 것을 환영합니다." 을 <b>UNICODE 값</b>으로 적은 것이다.
 * 
 *      net/ion/ics/res/text_ja_JP.properties
 *          welcome = {0} 일본어로 쓸 글을 유니코드화 시킨 문자열 ㅡ.ㅡ
 * 
 * 
 *  Resources m = Resources.getResources("net.ion.ics.res.text",Locale.KOREA);
 *  m.getResources("welcome","내 홈페이지");
 *      -> 내 홈페이지에 오신 것을 환영합니다.
 * 
 * 
 * 
 *  Resources m = Resources.getResources("net.ion.ics.res.text",Locale.US);
 *  m.getResources("welcome","My homepage");
 *      -> welcome to my homepage.
 * 
 * 
 *  Resources m = Resources.getResources("net.ion.ics.res.text",Locale.JAPAN);
 *  m.getResources("welcome","와따시와 ㅡ.ㅡ");
 *      -> 와따시와 ㅡ.ㅡ [일본어로 어쩌구 저쩌구...]
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
	 * ResourceBundle 의 Key를 반환
	 * 
	 * @return Enumeration
	 */
	public Enumeration<String> getKeys() {
		return bundle.getKeys();
	}

	/**
	 * ResourceBundle 의 Locale을 가져온다.
	 * 
	 * @return Locale
	 */
	public Locale getLocale() {
		return bundle.getLocale();
	}

	/**
	 * key 값을 가져온다.
	 * 
	 * @param key
	 *            String
	 * @return String
	 */
	public String get(String key) {
		return get(key, new Object[0]);
	}

	/**
	 * Key 값을 가져온다.
	 * 
	 * key에 해당하는 문자열이 "max ? ~ min ?" 라면, "?" 부분에 들어갈 값들이 args가 된다. 즉 args={ 10, 0 } 이라면 "max 10 ~ min 0"을 return 한다.
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
	 * Key 값을 가져온다.
	 * 
	 * get( String key, Object[] args )와 동일하며, 다른점은 argument가 하나인 경우만을 처리한다.
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
	 * get( String key, Object[] args )와 동일하며, 다른점은 argument가 두개인 경우만을 처리한다.
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
	 * get( String key, Object[] args )와 동일하며, 다른점은 argument가 세개인 경우만을 처리한다.
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
	 * Resources Instance를 가져온다.
	 * 
	 * <pre>
	 * ex)
	 *  Resources m = Resources.getResources("net.ion.ics.res.text");
	 * </pre>
	 * 
	 * @param baseName
	 *            String java.util.ResourceBundle의 getBundle과 동일하다.
	 * @return Resources
	 */
	public static Resources getResources(String baseName) {
		// Resources messages = new Resources();
		// messages.bundle = PropertyResourceBundle.getBundle( baseName );
		// return messages;
		return XMLResourceFactory.getResource(baseName);
	}

	/**
	 * Resources Instance를 가져온다.
	 * 
	 * <pre>
	 * ex)
	 *  Resources m = Resources.getResources("net.ion.ics.res.text",new Locale("ko","KR"));
	 *  이 경우 /net/ion/ics/res/text_ko_KR.properties 에서 ResourceBundle을 가져온다.
	 * </pre>
	 * 
	 * @param baseName
	 *            String java.util.ResourceBundle의 getBundle과 동일하다.
	 * @param locale
	 *            Locale 가져올 resource bundle의 Locale
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
	 * Resources Instance를 가져온다.
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
	 * Resource Instance를 가져온다.
	 * 
	 * @param baseName
	 *            String
	 * @param response
	 *            HttpServletResponse HttpServletResponse 의 Locale을 적용하여 Resources를 가져온다.
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
