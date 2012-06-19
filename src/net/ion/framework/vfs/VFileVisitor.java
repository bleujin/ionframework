package net.ion.framework.vfs;

import org.apache.commons.vfs2.FileSystemException;

/**
 * <p>Title: VFileVisitor.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: I-ON Communications</p>
 * <p>Date : 2011. 2. 9.</p>
 * @author novision
 * @version 1.0
 */
public interface VFileVisitor {
	public void visitVFile(VFile vf) throws FileSystemException ;
}
