package net.ion.framework.vfs;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import net.ion.framework.util.HashFunction;
import net.ion.framework.util.ListUtil;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.operations.FileOperations;

public class VFile implements Closeable {

	private FileObject fo;
	private FileSystemEntry fse;

	private VFile(FileObject fo, FileSystemEntry fse) {
		this.fo = fo;
		this.fse = fse ;
	}

	public static VFile create(FileObject fo, FileSystemEntry fse) {
		return new VFile(fo, fse);
	}

	public void createFile() throws FileSystemException {
		fo.createFile();
	}

	public InputStream getInputStream() throws FileSystemException {
		return fo.getContent().getInputStream();
	}

	public OutputStream getOutputStream() throws FileSystemException {
		return fo.getContent().getOutputStream();
	}

	public void close() throws FileSystemException {
		fo.close();
	}

	public boolean delete() throws FileSystemException {
		return fo.delete();
	}

	public int deleteSub() throws FileSystemException {
		return fo.delete(new AllFileSelector());
	}

	public VFileName getName() {
		return VFileName.create(fo.getName());
	}

	public VFile getParent() throws FileSystemException {
		return create(fo.getParent(), fse);
	}

	public VFile getChild(String childName) throws FileSystemException {
		return fse.resolveFile(this, childName) ;
	}

	public List<VFile> getChildren() throws FileSystemException {
		List<VFile> result = ListUtil.newList();
		FileObject[] fos = fo.getChildren();
		if (fos == null || fos.length== 0) return Collections.<VFile>emptyList();
		for (FileObject fileObject : fos) {
			result.add(create(fileObject, fse));
		}

		return result;
	}

	public void visit(VFileVisitor vfv) throws FileSystemException{
		vfv.visitVFile(this) ;
		if (this.isFile()) return ;
		List<VFile> children = getChildren() ;
		for (VFile child : children) {
			child.visit(vfv) ;
		}
	}
	
	public FileType getType() throws FileSystemException {
		return fo.getType();
	}

	public URL getURL() throws FileSystemException {
		return fo.getURL();
	}

	public boolean exists() throws FileSystemException {
		return fo.exists();
	}

	public boolean equals(Object that) {
		if (!(that instanceof VFile))
			return false;
		return fo.equals(((VFile) that).fo);
	}

	public int hashCode() {
		return fo.hashCode();
	}

	public VFileContent getContent() throws FileSystemException {
		return VFileContent.create(fo.getContent());
	}

	public String toString() {
		return "vfile[name:" + getName().getPath() + "]";
	}

	public void write(InputStream input) throws FileSystemException, IOException {
		final OutputStream output = getOutputStream();
		try {
			IOUtils.copy(input, output);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

	public int getLengthAsInt() throws FileSystemException {
		return new Long(getContent().getSize()).intValue();
	}

	public void createFolder() throws FileSystemException {
		if(!fo.exists() && isDir()){
			fo.createFolder();
		}
	}

	public boolean rename(String rename) throws FileSystemException {
		VFile target = VFile.create(fo.getFileSystem().resolveFile(getParent().getName().getPath() + "/" + rename), fse) ; 
		return moveTo(target);
	}
	
	public boolean moveTo(VFile target) {
		try {
			if (fo.exists()) {
				target.createFolder();
				FileObject tfo = target.getFileObject();
				fo.moveTo(tfo);
				fo = tfo;
				return true;
			}
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void copyFrom(VFile source) throws FileSystemException {
		fo.copyFrom(source.getFileObject(), new AllFileSelector());
	}

	
	public void copy(VFile target) throws FileSystemException {
		target.copyFrom(this);
	}
	
	public boolean isFile() throws FileSystemException {
		return FileType.FILE.equals(getType());
	}

	
	public boolean isDir() throws FileSystemException {
		return !isFile() ;
	}

	public FileObject getFileObject() {
		return fo ;
	}
	
	public FileOperations getFileOperations() throws FileSystemException{
		return fo.getFileOperations() ;
	}

	public long getETag() throws FileSystemException {
		return Math.abs(HashFunction.hashGeneral(getName().getPath()) + (isFile() ? getContent().getSize() : 0));
	}

	public void setAttribute(String key, Object value) throws FileSystemException {
		fo.getFileSystem().setAttribute(key, AttributeObject.create(fo.getName(), value)) ;
	}

	public VFile[] findFiles(FileSelector selector) throws FileSystemException {
		FileObject[] fos = fo.findFiles(selector) ;
		if (fos == null) return new VFile[0] ;
		
		List<VFile> result = ListUtil.newList() ;
		for (FileObject fo : fos) {
			result.add(new VFile(fo, fse)) ;
		}

		return result.toArray(new VFile[0]);
	}

	public void copyFrom(VFile src, FileSelector selector) throws FileSystemException {
		fo.copyFrom(src.fo, selector) ;
	}

	public int delete(FileSelector selector) throws FileSystemException {
		return fo.delete(selector) ;
	}

}
