package net.ion.framework.res;

public class MockResources extends Resources {
	public MockResources() {
	}

	public String get(String key) {
		return key;
	}

}
