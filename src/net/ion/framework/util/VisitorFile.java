package net.ion.framework.util;

import java.io.File;

public interface VisitorFile<T> {

	public T visit(File file) ;
}
