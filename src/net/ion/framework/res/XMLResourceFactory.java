package net.ion.framework.res;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration; 
import java.util.HashMap;
import java.util.logging.Logger;

import net.ion.framework.logging.LogBroker;
import net.ion.framework.util.PathMaker;
import net.ion.framework.util.StringUtil;
import net.ion.framework.util.UTF8FileUtils;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public class XMLResourceFactory {
	private final static String[] USABLE_LOCALE = new String[] { "ko", "en", "ja", "zh", "zh_tw" };
	static XMLResourceFactory self = null;

	private HashMap<String, XMLResources> resource = new HashMap<String, XMLResources>();
	private Logger log;

	private String baseDir = null;
	private HashMap<String, String> baseDirMap = new HashMap<String, String>();

	private XMLResourceFactory() {
		this.log = LogBroker.getLogger(this);
	}

	private static XMLResourceFactory getInstance() {
		if (self == null) {
			synchronized (XMLResourceFactory.class) {
				if (self == null)
					self = new XMLResourceFactory();
			}
		}
		return self;
	}

	static XMLResources getResource(String path) {
		return (XMLResources) getResource(path, "en");
	}

	static synchronized XMLResources getResource(String path, String localeStr) {
		String key = path + "_" + localeStr;
		XMLResources res = (XMLResources) getInstance().resource.get(key);
		if (res == null) {
			synchronized (XMLResourceFactory.class) {
				res = getInstance().makeResource(path, localeStr);
			}
		}
		return res;
	}

	private Digester getDigester(String localeStr) {
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.setUseContextClassLoader(true);

		digester.addObjectCreate("messages", Messages.class);
		digester.addObjectCreate("messages/message", Message.class);
		digester.addSetProperties("messages/message");
		digester.addBeanPropertySetter("messages/message/" + localeStr, "value");
		digester.addSetNext("messages/message", "addMessage");
		return digester;
	}

	private XMLResources makeResource(String path, String localeStr) {
		XMLResources res = null;
		try {
			String base = (String) baseDirMap.get(path);
			if (base == null) {
				base = this.baseDir;
			}

			if (base == null)
				throw new IllegalStateException("not initialized baseDir");
			File file = new File(base);
			res = new XMLResources(makeMessages(localeStr, UTF8FileUtils.readFileToReader(file)));
			log.info(localeStr + " resource initialized(" + path + ")..");
		} catch (Exception ex) {
			log.warning(ex.getMessage());
			res = new XMLResources(new Messages());
		}
		String key = path + "_" + localeStr;
		resource.put(key, res);

		return res;
	}

	public static void setBaseDir(String baseDir, String fileLoc) {
		getInstance().baseDir = PathMaker.getFilePath(baseDir, fileLoc);
	}

	public static void setBaseDir(String baseDir, String fileLoc, String key) {
		String dirPath = PathMaker.getFilePath(baseDir, fileLoc);
		if (getInstance().baseDir == null) {
			getInstance().baseDir = dirPath;
		}
		getInstance().baseDirMap.put(key, dirPath);
	}

	public static String getResourceXML() {
		File file = new File(getInstance().baseDir);
		try {
			return UTF8FileUtils.readFileToString(file);
		} catch (IOException ex) {
			return "";
		}
	}

	public synchronized static void setResourceXML(String resourceXML) throws IOException, SAXException {
		HashMap<String, XMLResources> resourceMapTemp = new HashMap<String, XMLResources>();
		for (int i = 0; i < USABLE_LOCALE.length; i++) {
			XMLResources res = new XMLResources(getInstance().makeMessages(USABLE_LOCALE[i], new StringReader(resourceXML)));
			resourceMapTemp.put(USABLE_LOCALE[i], res);
		}

		File file = new File(getInstance().baseDir);
		UTF8FileUtils.writeStringToFile(file, resourceXML, "UTF-8");
		getInstance().resource.clear();
		// getInstance().resource = resourceMapTemp;
	}

	private Messages makeMessages(String localeStr, Reader reader) throws SAXException, IOException {
		Digester digester = getDigester(localeStr);
		Messages messages = (Messages) digester.parse(reader);
		messages.setLocale(localeStr);
		return messages;
	}

	public void extractResourceXml(String resFile, String localeStr) throws FileNotFoundException, IOException, SAXException {
		File file = new File(resFile);

		Messages koMessages = (Messages) getDigester("ko").parse(file);
		koMessages.setLocale("ko");

		Messages enMessages = (Messages) getDigester("en").parse(file);
		koMessages.setLocale("en");

		Messages messages = (Messages) getDigester(localeStr).parse(file);
		messages.setLocale(localeStr);

		String key;
		Enumeration<String> ko = koMessages.getKeys();
		ArrayList<String> list = new ArrayList<String>();
		while (ko.hasMoreElements()) {
			key = (String) ko.nextElement();
			list.add(key);
		}

		String[] keys = list.toArray(new String[0]);
		Arrays.sort(keys, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		StringBuffer str = new StringBuffer();

		String ko_value;
		String etc_value;
		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		str.append("<messages>\n");
		str.append("\t<default>ko</default>\n");

		for (int i = 0; i < keys.length; i++) {
			key = keys[i];
			ko_value = koMessages.get(key);
			etc_value = messages.get(key);
			if (StringUtil.isEmpty(StringUtil.replace(etc_value, "-", ""))) {
				etc_value = StringUtil.defaultIfEmpty(enMessages.get(key), "");
			}
			str.append("\t<message key=\"" + key + "\">\n");
			str.append("\t\t<ko><![CDATA[" + ko_value + "]]></ko>\n");
			str.append("\t\t<" + localeStr + "><![CDATA[" + etc_value + "]]></" + localeStr + ">\n");
			str.append("\t</message>\n");
		}
		str.append("</messages>\n");
		System.out.println(str);

		File refile = new File(resFile + ".txt");
		UTF8FileUtils.writeStringToFile(refile, str.toString(), "UTF-8");

	}

	public void makeResourceXml(String orgFile, String changeFile, String localeStr) throws FileNotFoundException, IOException, SAXException {
		File file = new File(orgFile);

		Messages koMessages = (Messages) getDigester("ko").parse(file);
		koMessages.setLocale("ko");

		Messages enMessages = (Messages) getDigester("en").parse(file);
		enMessages.setLocale("en");

		Messages jaMessages = (Messages) getDigester("ja").parse(file);
		jaMessages.setLocale("ja");

		Messages zhMessages = (Messages) getDigester("zh").parse(file);
		zhMessages.setLocale("zh");

		Messages zhtwMessages = (Messages) getDigester("zh_tw").parse(file);
		zhtwMessages.setLocale("zh_tw");

		File cfile = new File(changeFile);
		Messages messages = (Messages) getDigester(localeStr).parse(cfile);
		messages.setLocale(localeStr);

		if ("zh".equals(localeStr)) {
			zhMessages = messages;
		} else if ("zh_tw".equals(localeStr)) {
			zhtwMessages = messages;
		}

		String key;
		Enumeration<String> ko = koMessages.getKeys();
		ArrayList<String> list = new ArrayList<String>();
		while (ko.hasMoreElements()) {
			key = (String) ko.nextElement();
			list.add(key);
		}

		String[] keys = list.toArray(new String[0]);
		Arrays.sort(keys, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		StringBuffer str = new StringBuffer();

		String ko_value;
		String en_value;
		String ja_value;
		String zh_value;
		String zhtw_value;
		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		str.append("<messages>\n");
		str.append("\t<default>ko</default>\n");

		for (int i = 0; i < keys.length; i++) {
			key = keys[i];
			en_value = StringUtil.defaultIfEmpty(enMessages.get(key), "-------------");
			ko_value = koMessages.get(key);
			ja_value = StringUtil.defaultIfEmpty(jaMessages.get(key), "-------------");
			zh_value = StringUtil.defaultIfEmpty(zhMessages.get(key), "-------------");
			zhtw_value = StringUtil.defaultIfEmpty(zhtwMessages.get(key), "-------------");

			str.append("\t<message key=\"" + key + "\">\n");
			str.append("\t\t<en><![CDATA[" + en_value + "]]></en>\n");
			str.append("\t\t<ko><![CDATA[" + ko_value + "]]></ko>\n");
			str.append("\t\t<ja><![CDATA[" + ja_value + "]]></ja>\n");
			str.append("\t\t<zh><![CDATA[" + zh_value + "]]></zh>\n");
			str.append("\t\t<zh_tw><![CDATA[" + zhtw_value + "]]></zh_tw>\n");
			str.append("\t\t<fr><![CDATA[########]]></fr>\n");
			str.append("\t\t<es><![CDATA[########]]></es>\n");
			str.append("\t</message>\n");
		}
		str.append("</messages>\n");
		System.out.println(str);

		File refile = new File(orgFile + ".txt");
		UTF8FileUtils.writeStringToFile(refile, str.toString(), "UTF-8");

	}

	public static void main(String[] args) {
		try {
			// XMLResourceFactory.getInstance().extractResourceXml("d:/message_resource.xml", "zh");
			// XMLResourceFactory.getInstance().extractResourceXml("d:/message_resource_country.xml", "zh");
			// XMLResourceFactory.getInstance().extractResourceXml("d:/message_resource_error.xml", "zh");
			// XMLResourceFactory.getInstance().extractResourceXml("d:/message_resource_language.xml", "zh");

			XMLResourceFactory.getInstance().makeResourceXml("d:/message_resource.xml", "d:/message_resource2.xml", "zh");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
