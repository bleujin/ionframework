package net.ion.framework.util;

import java.io.File;

public interface FileClosure<V> {
	public V walk(File file) ;
}
