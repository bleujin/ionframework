package net.ion.framework.util;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileUtil extends FileUtils{
	private final static class configFilter implements FileFilter {
		public boolean accept(File file) {
			return file.getName().startsWith("plugin") && file.getName().endsWith(".xml");
		}
	}

	private final static class DirFilter implements FileFilter {
		public boolean accept(File file) {
			return file.isDirectory();
		}
	}

	public static FileFilter DIR_FILTER = new DirFilter() ;
	public static FileFilter PLUGIN_CONFIG_FILE_FILTER = new configFilter() ;
	
	
	public static File findFile(File parentDir, FileFilter filter, boolean recursive) {
		if (filter.accept(parentDir)) {
			return parentDir ;
		}

		if (parentDir.isDirectory() && recursive){
			for (File child : parentDir.listFiles()) {
				File found = findFile(child, filter, recursive) ;
				if (found != null) return found ;
			}
		} 
		return null;
	}

	public static File[] findFiles(File parentDir, FileFilter filter, boolean recursive) {
		List<File> result = ListUtil.newList() ;
		if (filter.accept(parentDir)) {
			result.add(parentDir) ;
		}

		if (parentDir.isDirectory() && recursive){
			for (File child : parentDir.listFiles()) {
				for(File find : findFiles(child, filter, recursive)){
					result.add(find) ;
				} ;
			}
		} 
		return result.toArray(new File[0]);
	}

}
