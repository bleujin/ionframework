package net.ion.framework.xml.digester;

import java.util.HashMap;

/**
 * 서버정보를 가지고 있는 클래스. 포트, 서버이름, DocumentRoot 등을 저장하고 있다.
 * 
 * @author not attributable
 * @version 1.0
 */
public class ServerConfig {

	private int port;
	private String serverName;
	private String documentRoot;
	private HashMap<String, VirtualMapping> mapping = new HashMap<String, VirtualMapping>();

	/**
	 * 서버이름, documentRoot, 서버포트를 설정한다.
	 * 
	 * @param serverName
	 *            String
	 * @param documentRoot
	 *            String
	 * @param port
	 *            int
	 */
	public void setInformation(String serverName, String documentRoot, int port) {
		this.setServerName(serverName);
		this.setPort(port);
		this.setDocumentRoot(documentRoot);
	}

	/**
	 * 서버포트를 가져온다.
	 * 
	 * @return int
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 서버포트를 설정한다.
	 * 
	 * @param port
	 *            int
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * 서버이름을 가져온다.
	 * 
	 * @return String
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * 서버이름을 설정한다.
	 * 
	 * @param serverName
	 *            String
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * DocumentRoot를 가져온다.
	 * 
	 * @return String
	 */
	public String getDocumentRoot() {
		return documentRoot;
	}

	/**
	 * DocumentRoot를 설정한다.
	 * 
	 * @param documentRoot
	 *            String
	 */
	public void setDocumentRoot(String documentRoot) {
		this.documentRoot = documentRoot;
	}

	/**
	 * VirtualMapping을 추가한다.
	 * 
	 * @param map
	 *            VirtualMapping
	 */
	public void addMapping(VirtualMapping map) {
		mapping.put(map.getVirtualURI(), map);
	}

	/**
	 * uri를 가지는 VirtualMapping을 가져온다.
	 * 
	 * @param uri
	 *            String
	 * @return VirtualMapping
	 */
	public VirtualMapping getMapping(String uri) {
		return (VirtualMapping) mapping.get(uri);
	}

	/**
	 * 모든 Mapping들을 HashMap으로 가져온다.
	 * 
	 * @return HashMap
	 */
	public HashMap<String, VirtualMapping> getMappings() {
		return mapping;
	}

}
