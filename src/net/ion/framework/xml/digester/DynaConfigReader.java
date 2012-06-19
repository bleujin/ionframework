package net.ion.framework.xml.digester;

import java.io.File;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.xml.sax.SAXException;

/**
 * config Rule과 config 파일을 읽어들여서 동적으로 config 정보를 설정한다.
 * 
 * @author not attributable
 * @version 1.0
 */
public class DynaConfigReader implements ConfigReader {

	String configFile;
	String ruleXmlFile;

	public DynaConfigReader(String configFile, String ruleXmlFile) {
		this.configFile = configFile;
		this.ruleXmlFile = ruleXmlFile;
	}

	/**
	 * config Rule파일과 config 파일을 읽어들여 ServerConfig 인스턴스를 생성하고 생성한 인스턴스를 return 한다.
	 * 
	 * @throws IOException
	 * @throws SAXException
	 * @return ServerConfig
	 */
	public ServerConfig readConfig() throws IOException, SAXException {
		File ruleFile = new File(ruleXmlFile);
		Digester digester = DigesterLoader.createDigester(ruleFile.toURL());

		File file = new File(configFile);
		ServerConfig config = (ServerConfig) digester.parse(file);

		return config;
	}
}
