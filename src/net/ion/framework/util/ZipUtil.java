package net.ion.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

/*
 * Created on 02-sep-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

/**
 * @author ivan
 * 
 *         To change the template for this generated type comment go to Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ZipUtil {
	private UnZipFilter unzipFilter = null;
	private ZipFilter zipFilter = null;

	public ZipUtil() {

	}

	public void setUnZipFilter(UnZipFilter unzipFilter) {
		this.unzipFilter = unzipFilter;
	}

	public void setZipFilter(ZipFilter zipFilter) {
		this.zipFilter = zipFilter;
	}

	public void unzip(File srcZipfile, String targetDir) throws IOException {
		ZipFile zf = new ZipFile(srcZipfile);
		Enumeration entries = zf.entries();

		while (entries.hasMoreElements()) {
			InputStream bis = null;
			OutputStream bos = null;
			try {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (!isAllowUnzip(entry))
					continue;

				File newFile = new File(PathMaker.getFilePath(targetDir, entry.getName()));

				if (entry.isDirectory()) {
					newFile.mkdir();
				} else {
					File dir = new File(newFile.getParent());
					dir.mkdirs();
					bis = new BufferedInputStream(zf.getInputStream(entry));
					bos = new BufferedOutputStream(new FileOutputStream(newFile));

					IOUtils.copy(bis, bos);
				}
			} finally {
				IOUtils.closeQuietly(bis);
				IOUtils.closeQuietly(bos);
			}
		}

	}

	public int zip(File src, File targetZipfile) throws IOException {
		int entryCount = 0;
		ZipOutputStream zout = null;
		try {
			zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(targetZipfile)));
			entryCount = addEntry(src, zout);
			zout.close();
		} finally {
			IOUtils.closeQuietly(zout);
		}
		return entryCount;
	}

	private int addEntry(File src, ZipOutputStream zout) throws IOException {

		int result = 0;
		if (src.isDirectory()) {
			for (File sfile : src.listFiles()) {
				result += addEntry(sfile, zout);
			}
		} else {
			if (!isAllowZip(src))
				return 0;
			InputStream fis = null;
			try {
				fis = new BufferedInputStream(new FileInputStream(src));
				ZipEntry zipEntry = new ZipEntry(src.getPath());
				zout.putNextEntry(zipEntry);

				IOUtils.copy(fis, zout);
				Debug.debug(src);
				result++;
			} finally {
				IOUtils.closeQuietly(fis);
			}
		}
		return result;
	}

	private boolean isAllowUnzip(ZipEntry entry) {
		if (unzipFilter == null)
			return true;
		else
			return unzipFilter.allow(entry);
	}

	private boolean isAllowZip(File file) {
		if (zipFilter == null)
			return true;
		else
			return zipFilter.allow(file);
	}

	public interface ZipFilter {
		public boolean allow(File file);
	}

	public interface UnZipFilter {
		public boolean allow(ZipEntry entry);
	}

}
