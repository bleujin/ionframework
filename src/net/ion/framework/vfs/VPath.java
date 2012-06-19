package net.ion.framework.vfs;

public class VPath {

	private String path ;
	private VPath(String path) {
		this.path = path ;
	}

	public static VPath create(String path) {
		return new VPath(path);
	}

	public String stringPath() {
		return path;
	}

}
