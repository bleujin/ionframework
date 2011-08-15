package net.ion.framework.vfs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.ion.framework.util.IOUtil;

import org.apache.commons.vfs.FileSystemException;

public class VFS {

	private static VFS SELF = new VFS();
	private Map<URL, FileSystemEntry> cache = new HashMap<URL, FileSystemEntry>();

	public static FileSystemEntry DEFAULT = createEmpty();

	public static FileSystemEntry createEmpty() {
		try {
			File file = IOUtil.createTempFile("vfs_provider") ;
			FileWriter writer = new FileWriter(file) ;
			writer.write("<providers></providers>") ;
			writer.close() ;
			FileSystemEntry entry = getManger(file.toURL()) ;
			return entry;
		} catch (FileSystemException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private FileSystemEntry getInstance(URL url) throws FileSystemException {
		FileSystemEntry entry = cache.get(url);
		if (entry != null) {
			return entry;
		} else {
			synchronized (this) {
				MyFileSystemManager fsm = new MyFileSystemManager(url);
				fsm.init();
				entry = FileSystemEntry.create(fsm);
				cache.put(url, entry);
			}
		}
		return entry;
	}

	public static FileSystemEntry getManger(URL url) throws FileSystemException {
		return SELF.getInstance(url);
	}
}
