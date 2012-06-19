package net.ion.framework.vfs;

import java.util.Date;

import junit.framework.TestCase;
import net.ion.framework.util.Debug;

import org.apache.tools.ant.filters.StringInputStream;

public class TestVFSCreate extends TestCase {

	FileSystemEntry entry;

	protected void setUp() throws Exception {
		super.setUp();
		entry = VFS.DEFAULT;
		LocalSubDirFileProvider aprovider = new LocalSubDirFileProvider();
		aprovider.setPrefixDir("c:/working/") ;
		if (! entry.hasProvider("afield")) entry.addProvider("afield", aprovider);
		if (! entry.hasProvider("temp")) entry.addProvider("temp", new org.apache.commons.vfs2.provider.temp.TemporaryFileProvider());
		if (! entry.hasProvider("ram")) entry.addProvider("ram", new org.apache.commons.vfs2.provider.ram.RamFileProvider());
	}

	public void testCreate() throws Exception {
		VFile vf = entry.resolveFile("afield:/imsi/temp/myfile.txt");
		vf.write(new StringInputStream("Hello Bleujin" + new Date()));
		vf.close();
		
	}

	public void testyRead() throws Exception {
		VFile vf = entry.resolveFile("afield:/imsi/temp/myfile.txt");
		Debug.debug(UTF8TextFile.create(vf).readAsString());
	}

	public void testConfirm() throws Exception {
		VFile vf = entry.resolveFile("afield:/imsi/temp/myfile.txt");
		Debug.debug(vf.getType(), vf.getURL(), vf.getName().getPath(), vf.getName().getScheme(), vf.getName().getExtension(), vf.getName().getDepth());
		VFile parent = vf.getParent();

		Debug.debug(parent.getType(), parent.getURL(), parent.getName().getPath(), parent.getName().getScheme(), parent.getName().getExtension(), parent
				.getName().getDepth());
		Debug.debug(parent.getParent().getParent().getChildren());
	}

	public void testDuplicateFileName() throws Exception {
		VFile vf = entry.write(new StringInputStream("Hello Bleujin" + new Date()), "afield:/imsi/temp/myfile.txt", new DefaultRenamePolicy());
		Debug.debug(vf.getName());
	}

}
