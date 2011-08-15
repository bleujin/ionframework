package net.ion.framework.vfs;

import org.apache.commons.vfs.FileSystemException;

public interface RenamePolicy {

	public final static RenamePolicy OverWritePolicy = new RenamePolicy() {
		public VFile rename(VFile preDefineFile, FileSystemEntry fentry) {
			return preDefineFile;
		}
	} ;
	public final static RenamePolicy DefaultPolicy = new DefaultRenamePolicy();

	public VFile rename(VFile preDefineFile, FileSystemEntry fentry) throws FileSystemException ;

}
