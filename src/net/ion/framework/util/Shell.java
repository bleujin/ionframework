package net.ion.framework.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;

import org.apache.commons.io.FileUtils;

/**
 * File System 을 다루는 기본 기능 구현
 * 
 * @author Kim, Sanghoon (wizest@i-on.net)
 * @version 1.0
 */

public class Shell {
	/**
	 * 파일(디렉토리) 을 옮긴다. (하위 디렉토리 포함)
	 * 
	 * @param source
	 *            File
	 * @param target
	 *            File
	 * @throws IOException
	 * @return boolean
	 */
	public static boolean move(File source, File target) throws IOException {
		if (source.isDirectory())
			return rename(source, target);
		else
			return renameForce(source, target);

	}

	/**
	 * 이름을 바꾼다.
	 * 
	 * @param source
	 *            File
	 * @param target
	 *            File
	 * @throws IOException
	 * @return boolean
	 */
	public static boolean rename(File source, File target) throws IOException {
		if (source.exists()) {
			source = source.getCanonicalFile();

			File parent = target.getParentFile();
			if (parent != null && !parent.exists()) {
				forceMkdir(parent);
			}

			return source.renameTo(target);
		} else {
			return false;
		}
	}

	/**
	 * The renameTo() method of java.io.File behaves differently under Solaris and Win32 when a file with the target name already exists.
	 */
	public static boolean renameForce(File source, File target) {
		try {
			moveFile(source, target);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 파일(디렉토리)을 복사한다. (하위 디렉토리 포함)
	 * 
	 * @param source
	 *            File
	 * @param target
	 *            File
	 * @throws IOException
	 */
	public static void copy(File source, File target) throws IOException {
		source = source.getCanonicalFile();
		if (source.exists()) {
			if (source.isDirectory()) {
				if (target.exists() && !target.isDirectory())
					throw new IOException("the target is not a directory.");

				String sPath = source.getAbsolutePath();
				int sPathLength = sPath.length();

				Stack<File> s = new Stack<File>();
				File src = source;
				s.push(src);
				while (!s.empty()) {
					src = (File) s.pop();
					if (src.isDirectory()) {
						File[] fs = src.listFiles();
						for (int i = 0, length = fs.length; i < length; ++i) {
							s.push(fs[i]);
						}
					} else {
						File dst = new File(target, src.getAbsolutePath().substring(sPathLength));
						copyFile(src, dst);
					}
				}
			} else {
				copyFile(source, target);
			}
		}
	}

	private static void copyFile(File source, File target) throws IOException {
		if (!source.isFile()) {
			throw new IOException("the source is not a file.");
		}

		forceMkdir(target.getParentFile());

		if (!target.exists()) {
			target.createNewFile();
		}

		FileUtils.copyFile(source, target);
	}

	/**
	 * 디렉토리를 선택하면 하위 디렉토리까지 모두 삭제
	 * 
	 * @param target
	 *            File
	 */
	public static void remove(File target) {
		if (!target.isDirectory())
			target.delete();
		else {
			Stack<File> s = new Stack<File>();
			s.addAll(Arrays.asList(target.listFiles()));

			while (true) {
				try {
					remove((File) s.pop());
				} catch (EmptyStackException ex) {
					break;
				}
			}
			target.delete();
		}
	}

	public static boolean forceMkdir(File directory) {
		if (!directory.exists()) {
			synchronized (Shell.class) {
				if (!directory.exists()) {
					return directory.mkdirs();
				}
			}
		} else {
			if(directory.isFile()){
				return false;
			}
		}
		return true;
	}

	/**
	 * Moves a file.
	 * <p>
	 * When the destination file is on another file system, do a "copy and delete".
	 * 
	 * @param srcFile
	 *            the file to be moved
	 * @param destFile
	 *            the destination file
	 * @throws NullPointerException
	 *             if source or destination is <code>null</code>
	 * @throws IOException
	 *             if source or destination is invalid
	 * @throws IOException
	 *             if an IO error occurs moving the file
	 * @since Commons IO 1.4
	 */
	public static void moveFile(File srcFile, File destFile) throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' is a directory");
		}
		// if (destFile.exists()) {
		// throw new IOException("Destination '" + destFile + "' already exists");
		// }
		if (destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile + "' is a directory");
		}
		boolean rename = srcFile.renameTo(destFile);
		if (!rename) {
			copyFile(srcFile, destFile);
			if (!srcFile.delete()) {
				deleteQuietly(destFile);
				throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
			}
		}
	}

	/**
	 * Deletes a file, never throwing an exception. If file is a directory, delete it and all sub-directories.
	 * <p>
	 * The difference between File.delete() and this method are:
	 * <ul>
	 * <li>A directory to be deleted does not have to be empty.</li>
	 * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
	 * </ul>
	 * 
	 * @param file
	 *            file or directory to delete, can be <code>null</code>
	 * @return <code>true</code> if the file or directory was deleted, otherwise <code>false</code>
	 * 
	 * @since Commons IO 1.4
	 */
	public static boolean deleteQuietly(File file) {
		if (file == null) {
			return false;
		}
		try {
			if (file.isDirectory()) {
				cleanDirectory(file);
			}
		} catch (Exception e) {
		}

		try {
			return file.delete();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Cleans a directory without deleting it.
	 * 
	 * @param directory
	 *            directory to clean
	 * @throws IOException
	 *             in case cleaning is unsuccessful
	 */
	public static void cleanDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory()) {
			String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			try {
				forceDelete(file);
			} catch (IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception) {
			throw exception;
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Deletes a file. If file is a directory, delete it and all sub-directories.
	 * <p>
	 * The difference between File.delete() and this method are:
	 * <ul>
	 * <li>A directory to be deleted does not have to be empty.</li>
	 * <li>You get exceptions when a file or directory cannot be deleted. (java.io.File methods returns a boolean)</li>
	 * </ul>
	 * 
	 * @param file
	 *            file or directory to delete, must not be <code>null</code>
	 * @throws NullPointerException
	 *             if the directory is <code>null</code>
	 * @throws FileNotFoundException
	 *             if the file was not found
	 * @throws IOException
	 *             in case deletion is unsuccessful
	 */
	public static void forceDelete(File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			boolean filePresent = file.exists();
			if (!file.delete()) {
				if (!filePresent) {
					throw new FileNotFoundException("File does not exist: " + file);
				}
				String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Deletes a directory recursively.
	 * 
	 * @param directory
	 *            directory to delete
	 * @throws IOException
	 *             in case deletion is unsuccessful
	 */
	public static void deleteDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			return;
		}

		cleanDirectory(directory);
		if (!directory.delete()) {
			String message = "Unable to delete directory " + directory + ".";
			throw new IOException(message);
		}
	}
}
