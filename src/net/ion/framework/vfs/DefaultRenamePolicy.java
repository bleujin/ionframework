package net.ion.framework.vfs;

import net.ion.framework.util.StringUtil;

import org.apache.commons.vfs2.FileSystemException;

public class DefaultRenamePolicy implements RenamePolicy {

	public DefaultRenamePolicy() {
	}

	public VFile rename(final VFile f, FileSystemEntry fentry) throws FileSystemException {
		VFile newFile = f ;
		if (createNewFile(newFile))
			return newFile;
		String name = newFile.getName().getBaseName() ;
		String body = StringUtil.substringBeforeLast(name, ".");
		String ext = StringUtil.defaultIfEmpty(StringUtil.substringAfterLast(name, "."), "");
		if(StringUtil.isNotBlank(ext)){
			ext = "." + ext; 
		}
		
		for (int count = 0; !createNewFile(newFile) && count < 9999; ) {
			count++ ;
			String newBaseName = body + count + ext;
			
			newFile = fentry.resolveFile(f.getName().getScheme() + ":" + f.getParent().getName().getPath() + "/" + newBaseName) ;
		}

		return newFile;
	}

	private boolean createNewFile(VFile f) {
		try {
			if (f.exists()) return false ;
			f.createFile() ;
			return true ;
		} catch (FileSystemException ignored) {
			return false;
		}
	}

}
