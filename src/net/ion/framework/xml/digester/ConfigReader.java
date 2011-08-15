package net.ion.framework.xml.digester;

import java.io.IOException;

import org.xml.sax.SAXException;

/**
 * XML파일을 통하여 config정보를 읽어들이는 모든 Class들은 이 Interface를 implement 해야한다. readConfig() 하나의 메소드를 가지고 있으며, 이것은 ServerConfig 인스턴스를 return 한다.
 * 
 * @author bleujin
 * @version 1.0
 */

public interface ConfigReader {
	public ServerConfig readConfig() throws IOException, SAXException;
}
