package net.ion.framework.res;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

import junit.framework.TestCase;
import net.ion.framework.util.StringUtil;

@SuppressWarnings("unused")
public class TestXMlResource extends TestCase {

	private String en = "D:/RV/src/net/ion/cms/res/messages_en.properties";
	private String ko = "D:/RV/src/net/ion/cms/res/messages_ko.properties";
	private String ja = "D:/RV/src/net/ion/cms/res/messages_ja.properties";
	private String zh = "D:/RV/src/net/ion/cms/res/messages_zh.properties";

	private String zh_tw = "D:/RV/src/net/ion/cms/res/messages_zh_tw.properties";

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDefaultLocale() throws Exception {
		Properties propertie_en = new Properties();
		propertie_en.load(new FileInputStream(new File(en)));

		Properties propertie_ko = new Properties();
		propertie_ko.load(new FileInputStream(new File(ko)));

		Properties propertie_ja = new Properties();
		propertie_ja.load(new FileInputStream(new File(ja)));

		Properties propertie_zh = new Properties();
		propertie_zh.load(new FileInputStream(new File(zh)));

		// Properties propertie_zh_tw= new Properties();
		// propertie_zh_tw.load(new FileInputStream(new File(zh_tw)));

		Iterator<?> iter = propertie_ko.keySet().iterator();

		StringBuffer str = new StringBuffer();
		String key;
		String en_value;
		String ko_value;
		String ja_value;
		String zh_value;
		// String zh_tw_value;
		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		str.append("<messages>\n");
		str.append("\t<default>ko</default>\n");
		while (iter.hasNext()) {
			key = (String) iter.next();
			en_value = StringUtil.defaultString((String) propertie_en.get(key));
			ko_value = StringUtil.defaultString((String) propertie_ko.get(key));
			ja_value = StringUtil.defaultString((String) propertie_ja.get(key));
			zh_value = StringUtil.defaultString((String) propertie_zh.get(key));
			// zh_tw_value = StringUtils.defaultString((String)propertie_zh_tw.get(key)) ;
			str.append("\t<message key=\"" + key + "\">\n");
			str.append("\t\t<en><![CDATA[" + en_value + "]]></en>\n");
			str.append("\t\t<ko><![CDATA[" + ko_value + "]]></ko>\n");
			str.append("\t\t<ja><![CDATA[" + ja_value + "]]></ja>\n");
			str.append("\t\t<zh><![CDATA[" + zh_value + "]]></zh>\n");
			str.append("\t\t<zh_tw><![CDATA[" + en_value + "]]></zh_tw>\n");
			str.append("\t</message>\n");
		}
		str.append("</messages>\n");

		System.out.println(str);

		// File file = new File("D:/RV/ics/WEB-INF/message_resource.xml");
		// OutputStream os = new FileOutputStream(file);
		// WriterToUTF8Buffered writer = new WriterToUTF8Buffered(os);
		// writer.write(str.toString());

		// FileUtils.writeStringToFile(file, str.toString(), "UTF-8");

	}

	public static void main(String[] args) {

	}
}
