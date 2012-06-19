package net.ion.framework.xml.digester;

import java.io.File;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public class StaticConfigReader implements ConfigReader {

	String configFile;

	public StaticConfigReader(String configFile) {
		this.configFile = configFile;
	}

	public ServerConfig readConfig() throws IOException, SAXException {
		Digester digester = new Digester();
		digester.setValidating(false);

		digester.addObjectCreate("server", ServerConfig.class);
		// digester.addSetProperties("server", "name", "serverName" );
		// digester.addBeanPropertySetter("server/documentRoot", "documentRoot");
		// digester.addBeanPropertySetter("server/port", "port");
		Class<?>[] paramType = { String.class, String.class, Integer.TYPE };
		digester.addCallMethod("server", "setInformation", 3, paramType);
		digester.addCallParam("server", 0, "name");
		digester.addCallParam("server/documentRoot", 1);
		digester.addCallParam("server/port", 2);

		digester.addObjectCreate("server/virtual-mapping-list/virtual-mapping", VirtualMapping.class);
		digester.addBeanPropertySetter("server/virtual-mapping-list/virtual-mapping/uri", "virtualURI");
		digester.addBeanPropertySetter("server/virtual-mapping-list/virtual-mapping/path", "realPath");
		digester.addSetNext("server/virtual-mapping-list/virtual-mapping", "addMapping");

		File file = new File(configFile);
		ServerConfig config = (ServerConfig) digester.parse(file);

		return config;
	}

}
