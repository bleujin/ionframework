package net.ion.framework.xml;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;

/**
 * 디렉토리단위로 java파일을 compile하는 메소드를 제공한다.
 * 
 * @author not attributable
 * @version 1.0
 */
public class DirectoryJavaComplier {
	public DirectoryJavaComplier() {
	}

	/**
	 * srcDir이하 모든 java파일들을 compile한다.
	 * 
	 * @param srcDir
	 *            File
	 */
	public static void compileDirectory(File srcDir) {

		if (srcDir == null) {
			throw new IllegalArgumentException("The argument 'srcDir' must not be null.");
		}

		compileDirectory(srcDir, srcDir);

	} // -- compileDirectory

	protected static void compileDirectory(File srcDir, File destDir) {
		if (srcDir == null) {
			throw new IllegalArgumentException("The argument 'srcDir' must not be null.");
		}

		if (destDir == null) {
			destDir = srcDir;

		}
		Project project = new Project();
		project.init();
		project.setBasedir(".");
		Javac compiler = new Javac();
		compiler.setProject(project);
		compiler.setFork(true);
		compiler.setDestdir(destDir.getAbsoluteFile());
		Path classpath = compiler.createClasspath();
		classpath.setPath(System.getProperty("java.class.path"));
		compiler.setClasspath(classpath);

		compileDirectory(srcDir, srcDir, compiler);

	} // -- compileDirectory

	private static void compileDirectory(File srcDir, File root, Javac compiler) {

		if (compiler == null) {
			Project project = new Project();
			project.init();
			project.setBasedir(".");
			compiler = new Javac();
			compiler.setProject(project);
			compiler.setFork(true);
			compiler.setDestdir(srcDir.getAbsoluteFile());
			Path classpath = compiler.createClasspath();
			classpath.setPath(System.getProperty("java.class.path"));
			compiler.setClasspath(classpath);
		}

		if (root == null) {
			root = srcDir;

			// --no argument checking
		}
		File[] entries = srcDir.listFiles();

		for (int i = 0; i < entries.length; i++) {
			File entry = entries[i];
			if (entry.isDirectory() && !entry.getName().endsWith("CVS")) {
				compileDirectory(entry, root, compiler);
			}
		}
		entries = null;

		Path sourcepath = compiler.createSrc();
		// --Are we compiling nested packages?
		if (srcDir.equals(root)) {
			sourcepath.setLocation(srcDir);
		} else {
			sourcepath.setLocation(root);
		}
		compiler.setSrcdir(sourcepath);
		compiler.execute();
	}

}
