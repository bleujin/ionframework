package net.ion.framework.util.image;

import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import net.ion.framework.util.IOUtil;

public class ImageWaterMarker {
	public static int RESULT_SUCCESS = 1;
	public static int RESULT_ERROR = 0;
	public static int RESULT_NOT_SUPPORTED_TYPE = -1;
	public static int RESULT_NOT_SUPPORTED_SIZE = -2;
	public static int RESULT_NOT_OPENED_FILE = -3;
	public static int RESULT_NOT_FOUND_FILE = -4;

	public static String LEFT_TOP = "left_top";
	public static String RIGHT_TOP = "rigth_top";
	public static String LEFT_BOTTOM = "left_bott";
	public static String RIGHT_BOTTOM = "rigth_bott";

	private File maskFile;
	private float alpha;
	private String position;

	public ImageWaterMarker(File maskFile, int alpha, String position) {
		this.maskFile = maskFile;
		this.alpha = ((float) alpha) / 100;
		this.position = position;
	}

//	public int waterMarkTo(String sourceFileName) throws IOException {
//		return waterMarkTo(sourceFileName, 100, 100);
//	}

//	public int waterMarkTo(String sourceFileName, int minWidth, int minHeight) throws IOException {
//		File sourceFile = new File(sourceFileName);
//
//		File tmpfile = IOUtil.createTempFile("water_" + (new Long(new Date().getTime())).toString());
//		tmpfile.deleteOnExit();
//
//		int result = RESULT_ERROR;
//		try {
//			result = waterMarkTo(sourceFile, new FileOutputStream(tmpfile), minWidth, minHeight);
//			if (result == 1) {
//				if (tmpfile.exists()) {
//					if (!Shell.renameForce(tmpfile, sourceFile)) {
//						System.out.println("### not rename [" + tmpfile.getAbsolutePath() + " --> " + sourceFile + "]");
//						FileUtils.copyFile(tmpfile, sourceFile);
//					}
//				} else {
//					System.out.println("### not exists " + tmpfile.getAbsolutePath());
//				}
//			}
//		} finally {
//			tmpfile.delete();
//		}	
//		return result;
//	}

	public int waterMarkTo(File sourceFile, OutputStream out) throws IOException {
		return waterMarkTo(sourceFile, out, 100, 100);
	}

	public int waterMarkTo(File sourceFile, OutputStream out, int minWidth, int minHeight) throws IOException  {
		int result = RESULT_ERROR;
		try {
			if (maskFile.exists()) {
				Image maskImage = new ImageIcon(maskFile.getAbsolutePath()).getImage();
				if (maskImage != null) {
					Opener opener = new Opener();

					int fileType = opener.getFileType(sourceFile.getAbsolutePath());
					ImagePlus imp = opener.openImage(sourceFile.getAbsolutePath());
					if (imp != null) {
						try {
							ImageProcessor ip = imp.getProcessor();
							if ((ip.getWidth() >= minWidth && ip.getHeight() >= minHeight) && (ip.getWidth() >= maskImage.getWidth(null) && ip.getHeight() >= maskImage.getHeight(null))) {
								BufferedImage bImage = new BufferedImage(ip.getWidth(), ip.getHeight(), BufferedImage.TYPE_INT_RGB);
								Graphics2D g = bImage.createGraphics();
								g.drawImage(ip.createImage(), 0, 0, null);
	
								int x = 0;
								int y = 0;
								if (RIGHT_TOP.equals(position)) {
									x = ip.getWidth() - maskImage.getWidth(null);
								} else if (LEFT_BOTTOM.equals(position)) {
									y = ip.getHeight() - maskImage.getHeight(null);
								} else if (RIGHT_BOTTOM.equals(position)) {
									x = ip.getWidth() - maskImage.getWidth(null);
									y = ip.getHeight() - maskImage.getHeight(null);
								}
								g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
								g.drawImage(maskImage, x, y, null);
								g.dispose();
								
								if (Opener.GIF == fileType) {
									ImagePlus impx = new ImagePlus("", bImage);
									ImageConverter converter = new ImageConverter(impx);
									converter.convertRGBtoIndexedColor(256);
									GifEncoder encoder = new GifEncoder(impx.getImage(), out);
									encoder.encode();
									result = RESULT_SUCCESS;
								} else if (Opener.JPEG == fileType) {
									ImageIO.write(bImage, "jpg", out);
									result = RESULT_SUCCESS;
								} else if (Opener.PNG == fileType) {
									ImageIO.write(bImage, "png", out);
									result = RESULT_SUCCESS;
								} else {
									//StreamUtils.copy(new FileInputStream(sourceFile), out);
									System.out.println("[" + fileType + "] not supported type " + sourceFile.getAbsolutePath() + " ");
									result = RESULT_NOT_SUPPORTED_TYPE;
								}
	
							} else {
								//StreamUtils.copy(new FileInputStream(sourceFile), out);
								System.out.println("[" + ip.getWidth() + " : " + ip.getHeight() + " " + sourceFile.getAbsolutePath() + "] not supported size");
								result = RESULT_NOT_SUPPORTED_SIZE;
							}
						} catch (IOException e) {
							e.printStackTrace();
							//StreamUtils.copy(new FileInputStream(sourceFile), out);
							System.out.println("[" + fileType + "] not supported type " + sourceFile.getAbsolutePath() + " ");
							result = RESULT_NOT_SUPPORTED_TYPE;
						} finally {
							imp.close();
						}
					} else {
						//StreamUtils.copy(new FileInputStream(sourceFile), out);
						System.out.println("[" + sourceFile.getAbsolutePath() + "] not opened file");
						result = RESULT_NOT_OPENED_FILE;
					}
				} else {
					System.out.println("[" + maskFile.getAbsolutePath() + "] not supported mask type ");
					result = RESULT_NOT_SUPPORTED_TYPE;
				}
			} else {
				System.out.println("[" + maskFile.getAbsolutePath() + "] not fount mask file");
				result = RESULT_NOT_FOUND_FILE;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result = RESULT_NOT_FOUND_FILE;
		} finally {
			IOUtil.closeSilent(out);
		}
		return result;
	}
}
