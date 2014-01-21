package net.ion.framework.xml.digester;

import java.util.HashMap;

public class ServerConfig {

	private int port;
	private String serverName;
	private String documentRoot;
	private HashMap<String, VirtualMapping> mapping = new HashMap<String, VirtualMapping>();

	/**
	 * 
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
	 * 
	 * 
	 * @return int
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 
	 * 
	 * @param port
	 *            int
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * 
	 * 
	 * @return String
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * 
	 * 
	 * @param serverName
	 *            String
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * DocumentRoot�� �����´�.
	 * 
	 * @return String
	 */
	public String getDocumentRoot() {
		return documentRoot;
	}

	/**
	 * 
	 * 
	 * @param documentRoot
	 *            String
	 */
	public void setDocumentRoot(String documentRoot) {
		this.documentRoot = documentRoot;
	}

	/**
	 * 
	 * 
	 * @param map
	 *            VirtualMapping
	 */
	public void addMapping(VirtualMapping map) {
		mapping.put(map.getVirtualURI(), map);
	}

	/**
	 * 
	 * 
	 * @param uri
	 *            String
	 * @return VirtualMapping
	 */
	public VirtualMapping getMapping(String uri) {
		return (VirtualMapping) mapping.get(uri);
	}

	/**
	 * 
	 * 
	 * @return HashMap
	 */
	public HashMap<String, VirtualMapping> getMappings() {
		return mapping;
	}

}
