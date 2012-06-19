package net.ion.framework.xml.digester;

import java.util.HashMap;

/**
 * ���������� ������ �ִ� Ŭ����. ��Ʈ, �����̸�, DocumentRoot ���� �����ϰ� �ִ�.
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
	 * �����̸�, documentRoot, ������Ʈ�� �����Ѵ�.
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
	 * ������Ʈ�� �����´�.
	 * 
	 * @return int
	 */
	public int getPort() {
		return port;
	}

	/**
	 * ������Ʈ�� �����Ѵ�.
	 * 
	 * @param port
	 *            int
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * �����̸��� �����´�.
	 * 
	 * @return String
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * �����̸��� �����Ѵ�.
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
	 * DocumentRoot�� �����Ѵ�.
	 * 
	 * @param documentRoot
	 *            String
	 */
	public void setDocumentRoot(String documentRoot) {
		this.documentRoot = documentRoot;
	}

	/**
	 * VirtualMapping�� �߰��Ѵ�.
	 * 
	 * @param map
	 *            VirtualMapping
	 */
	public void addMapping(VirtualMapping map) {
		mapping.put(map.getVirtualURI(), map);
	}

	/**
	 * uri�� ������ VirtualMapping�� �����´�.
	 * 
	 * @param uri
	 *            String
	 * @return VirtualMapping
	 */
	public VirtualMapping getMapping(String uri) {
		return (VirtualMapping) mapping.get(uri);
	}

	/**
	 * ��� Mapping���� HashMap���� �����´�.
	 * 
	 * @return HashMap
	 */
	public HashMap<String, VirtualMapping> getMappings() {
		return mapping;
	}

}
