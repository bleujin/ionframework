package net.ion.framework.xml.digester;

import java.io.IOException;

import org.xml.sax.SAXException;

/**
 * XML������ ���Ͽ� config������ �о���̴� ��� Class���� �� Interface�� implement �ؾ��Ѵ�. readConfig() �ϳ��� �޼ҵ带 ������ ������, �̰��� ServerConfig �ν��Ͻ��� return �Ѵ�.
 * 
 * @author bleujin
 * @version 1.0
 */

public interface ConfigReader {
	public ServerConfig readConfig() throws IOException, SAXException;
}
