package net.ion.framework.io;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;

import junit.framework.TestCase;
import net.ion.framework.util.Debug;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.FilesCache;
import org.apache.commons.vfs2.VFS;

public class TestVfs extends TestCase {

	FileSystemManager fsm = null;

	protected void setUp() throws Exception {
		super.setUp();
		fsm = VFS.getManager();
	}

	public void testLocal() throws Exception {
		FileObject fo = fsm.resolveFile("file:///c:/a/README.HTM");
		Debug.debug(IOUtils.toString(fo.getContent().getInputStream()), "EUC-KR");
		
		
		FileObject fo2 = fsm.createVirtualFileSystem("file:///c:/a/README.HTM") ;
		Debug.debug(IOUtils.toString(fo2.getContent().getInputStream()), "EUC-KR");
	}

	public void testURLInput() throws Exception {
		final String src = "http://commons.apache.org/vfs/filesystems.html";
		FileObject fo = fsm.resolveFile(src);
		IOUtils.toString(fo.getContent().getInputStream());
	}
	
	
	public void testCache() throws Exception {
		long start = System.nanoTime() ;
		for (int i = 0; i < 10; i++) {
			testURLInput() ;
		}
		Debug.line((System.nanoTime() - start) / 1000) ;
		final String src = "http://commons.apache.org/vfs/filesystems.html";
		FileObject fo = fsm.resolveFile(src);
		final FilesCache fcache = ((FileSystemManager)VFS.getManager()).getFilesCache();
		fcache.putFile(fo) ;
		
		for (int i = 0; i < 10; i++) {
			FileObject fco = fcache.getFile(fo.getFileSystem(), fo.getName()) ;
			IOUtils.toString(fco.getContent().getInputStream());
		}
		Debug.line((System.nanoTime() - start) / 1000) ;
	}
	

	public void testTemporary() throws Exception {

		FileObject fo = fsm.resolveFile("tmp://mydic/abc");
		OutputStream output = fo.getContent().getOutputStream();
		Writer writer = new OutputStreamWriter(output, "UTF-8");
		writer.write("Hi bleujin");
		writer.close();

		InputStream input = fo.getContent().getInputStream();
		Debug.debug(IOUtils.toString(input, "UTF-8"));
	}

	public void testZip() throws Exception {
		FileObject fo = fsm.resolveFile("zip:file:///d:/temp/test.zip!/MyMember.java");
//		Debug.debug(true, fo.exists(), fsm.getCacheStrategy(), fo.getContent().getLastModifiedTime()) ;
		
		
		Debug.debug(fo.isWriteable()) ;
		OutputStream output = fo.getContent().getOutputStream() ;
		Writer writer = new OutputStreamWriter(output) ;
		
		writer.write("Hello bleujin") ;
		writer.close();
	}
	
	
	public void testPercentFilename() throws Exception {
		
		FileObject fo = fsm.resolveFile("file:///c:/abc#.txt");
		fo.createFolder() ;
		
		
		Debug.line(URLEncoder.encode("abc%.txt", "euc-kr")) ;
		
		Debug.debug(fo.getURL(), fo.getName(), new File("abc%d.txt").toURI()) ;
	}
	
	
	private void print(String name) throws FileSystemException {
		FileSystemManager mgr = VFS.getManager();
		System.out.println();
		System.out.println("Parsing: " + name);
		FileObject file = mgr.resolveFile(name);
		System.out.println("URL: " + file.getURL());
		System.out.println("getName(): " + file.getName());
		System.out.println("BaseName: " + file.getName().getBaseName());
		System.out.println("Extension: " + file.getName().getExtension());
		System.out.println("Path: " + file.getName().getPath());
		System.out.println("Scheme: " + file.getName().getScheme());
		System.out.println("URI: " + file.getName().getURI());
		System.out.println("Root URI: " + file.getName().getRootURI());
		System.out.println("Parent: " + file.getName().getParent());
		System.out.println("Type: " + file.getType());
		System.out.println("Exists: " + file.exists());
		System.out.println("Readable: " + file.isReadable());
		System.out.println("Writeable: " + file.isWriteable());
		System.out.println("Root path: " + file.getFileSystem().getRoot().getName().getPath());
		if (file.exists()) {
			if (file.getType().equals(FileType.FILE)) {
				System.out.println("Size: " + file.getContent().getSize() + " bytes");
			} else if (file.getType().equals(FileType.FOLDER) && file.isReadable()) {
				FileObject[] children = file.getChildren();
				System.out.println("Directory with " + children.length + " files");
				for (int iterChildren = 0; iterChildren < children.length; iterChildren++) {
					System.out.println("#" + iterChildren + ": " + children[iterChildren].getName());
					if (iterChildren > 5) {
						break;
					}
				}
			}
			System.out.println("Last modified: " + DateFormat.getInstance().format(new Date(file.getContent().getLastModifiedTime())));
		} else {
			System.out.println("The file does not exist");
		}
		file.close();
	}
}
