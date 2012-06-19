package net.ion.framework.xml.digester;

/**
 * virtualURI, realPath를 멤버로 가지는 Bean
 * 
 * @author bleujin
 * @version 1.0
 */

public class VirtualMapping {

	private String virtualURI;
	private String realPath;

	public String getVirtualURI() {
		return virtualURI;
	}

	public void setVirtualURI(String virtualURI) {
		this.virtualURI = virtualURI;
	}

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}
}
