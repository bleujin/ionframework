package net.ion.framework.util.image;

import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageResizer {
	private String sourceFile;
	private int width;
	private int height;
	
	public static ImageResizer create(String sourceFile, int width, int height){
		return new ImageResizer(sourceFile, width, height);
	} 
		
	private ImageResizer(String sourceFile, int width, int height) {
		this.sourceFile = sourceFile;
		this.width = width;
		this.height = height;
	}

//	// thread not safe.. -_)
//	public String resizeTo() throws IOException {
//		String filePath = StringUtil.substringBeforeLast(sourceFileFullPath, "/");
//		String fileName = StringUtil.substringAfterLast(sourceFileFullPath, "/");
//		String outFileName = getEnableFilePath(filePath, fileName);
//
//		OutputStream out = new FileOutputStream(outFileName);
//		resizeTo(out);
//		out.flush();
//		out.close();
//
//		return outFileName;
//	}

	public void resizeTo(OutputStream out) throws IOException {
		Opener opener = new Opener();
		ImagePlus imp = opener.openImage(sourceFile);
		if (imp == null) {
			throw new FileNotFoundException(sourceFile + " Not Founded");
		}

		ImageProcessor ip = getResizeProcessor(imp);
		BufferedImage bImage = new BufferedImage(ip.getWidth(), ip.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = bImage.createGraphics();
		g.drawImage(ip.createImage(), 0, 0, null);
		g.dispose();

		int typeCd = opener.getFileType(sourceFile);

		if (Opener.GIF == typeCd) {
			GifEncoder encoder = new GifEncoder(bImage, out);
			encoder.encode();
		} else if (Opener.JPEG == typeCd || Opener.PNG == typeCd) {
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bImage);
			param.setQuality(10f, true);
			encoder.encode(bImage, param);
		} else {
			throw new IllegalArgumentException("not supported type");
		}

		if (Opener.GIF == typeCd) {
			ImagePlus impx = new ImagePlus("", bImage);
			ImageConverter converter = new ImageConverter(impx);
			converter.convertRGBtoIndexedColor(256);
			GifEncoder encoder = new GifEncoder(impx.getImage(), out);
			encoder.encode();
		} else if (Opener.JPEG == typeCd) {
			ImageIO.write(bImage, "jpg", out);
		} else if (Opener.PNG == typeCd) {
			ImageIO.write(bImage, "png", out);
		} else {
			throw new IllegalArgumentException("not supported type");
		}
	}

	private ImageProcessor getResizeProcessor(ImagePlus imp) {
		ImageProcessor bip = imp.getProcessor();
		if (height <= 0 || width <= 0)
			return bip; // no resize..

		// int size = bip.getWidth() > bip.getHeight() ? bip.getWidth() : bip.getHeight();
		ImageProcessor ip = bip.resize(width, height);
		return ip;
	}

//	private String getEnableFilePath(String uploadPath, String fileName) {
//		int i = 1;
//		String realFileName = getAsciiFileName(fileName);
//		File chkfile = new File(PathMaker.getFilePath(uploadPath, realFileName));
//		while (chkfile.exists()) {
//			realFileName = String.valueOf(i) + "_" + getAsciiFileName(fileName);
//			chkfile = new File(PathMaker.getFilePath(uploadPath, realFileName));
//			i++;
//		}
//		return PathMaker.getFilePath(uploadPath, realFileName);
//	}

//	private String getAsciiFileName(String fileName) {
//		String fname = StringUtil.substringBeforeLast(fileName, ".");
//		String ext = StringUtil.substringAfterLast(fileName, ".");
//		if (StringUtil.isAlphanumericUnderbar(fname) && StringUtil.isAlphanumericUnderbar(ext)) {
//			return fileName;
//		} else {
//			return RandomStringUtils.randomAlphanumeric(12) + "." + ext;
//		}
//	}
}
