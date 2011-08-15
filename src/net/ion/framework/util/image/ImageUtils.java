package net.ion.framework.util.image;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.media.jai.PlanarImage;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

public class ImageUtils {
	private int width = -1;
	private int height = -1;
	private HashMap<String, String> metaInfo;

	public ImageUtils(String filePath) {
		metaInfo = new HashMap<String, String>();
		try {
			init(filePath);
		} catch (Exception e) {
		}
	}

	private void init(String filePath) throws IOException, JpegProcessingException, MetadataException {

		PlanarImage image = null;

		SeekableStream stream = new FileSeekableStream(filePath);
		String imgType = ImageCodec.getDecoderNames(stream)[0];
		ImageDecoder dec = ImageCodec.createImageDecoder(imgType, stream, null);
		RenderedImage im = dec.decodeAsRenderedImage();
		image = PlanarImage.wrapRenderedImage(im);
		this.width = image.getWidth();
		this.height = image.getHeight();

		metaInfo.put("Image Format", imgType);

		if ("jpeg".equalsIgnoreCase(imgType) || "jpg".equalsIgnoreCase(imgType)) {
			initJpegMeta(filePath);
		} else {
			metaInfo.put("Image Width", this.width + " pixels");
			metaInfo.put("Image Height", this.height + " pixels");
		}
	}

	private void setMetaValue(Tag tag) throws MetadataException {
		if ("Image Width".equals(tag.getTagName())) {
			metaInfo.put("Image Width", tag.getDescription());
		} else if ("Image Height".equals(tag.getTagName())) {
			metaInfo.put("Image Height", tag.getDescription());
		} else if ("X Resolution".equals(tag.getTagName())) {
			metaInfo.put("X Resolution", tag.getDescription());
		} else if ("Y Resolution".equals(tag.getTagName())) {
			metaInfo.put("Y Resolution", tag.getDescription());
		} else if ("Make".equals(tag.getTagName())) {
			metaInfo.put("Make", tag.getDescription());
		} else if ("Model".equals(tag.getTagName())) {
			metaInfo.put("Model", tag.getDescription());
		} else if ("Color Space".equals(tag.getTagName())) {
			metaInfo.put("Color Space", tag.getDescription());
		} else if ("Shutter Speed Value".equals(tag.getTagName())) {
			metaInfo.put("Shutter Speed Value", tag.getDescription());
		} else if ("Aperture Value".equals(tag.getTagName())) {
			metaInfo.put("Aperture Value", tag.getDescription());
		} else if ("Flash".equals(tag.getTagName())) {
			metaInfo.put("Flash", tag.getDescription());
		} else if ("Focal Length".equals(tag.getTagName())) {
			metaInfo.put("Focal Length", tag.getDescription());
		} else if ("F-Number".equals(tag.getTagName())) {
			metaInfo.put("F-Number", tag.getDescription());
		} else if ("Exposure Time".equals(tag.getTagName())) {
			metaInfo.put("Exposure Time", tag.getDescription());
		} else if ("ISO Speed Ratings".equals(tag.getTagName())) {
			metaInfo.put("ISO Speed Ratings", tag.getDescription());
		} else if ("Metering Mode".equals(tag.getTagName())) {
			metaInfo.put("Metering Mode", tag.getDescription());
		} else if ("Exposure Mode".equals(tag.getTagName())) {
			metaInfo.put("Exposure Mode", tag.getDescription());
		} else if ("White Balance".equals(tag.getTagName())) {
			metaInfo.put("White Balance", tag.getDescription());
		} else if ("Exif Image Width".equals(tag.getTagName())) {
			metaInfo.put("Exif Image Width", tag.getDescription());
		} else if ("Exif Image Height".equals(tag.getTagName())) {
			metaInfo.put("Exif Image Height", tag.getDescription());
		} else if ("Date/Time".equals(tag.getTagName())) {
			metaInfo.put("Date/Time", tag.getDescription());
		}
	}

	@SuppressWarnings("unchecked")
	private void initJpegMeta(String filePath) throws JpegProcessingException, MetadataException {
		File jpegFile = new File(filePath);

		Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);

		Iterator<Directory> directories = metadata.getDirectoryIterator();
		while (directories.hasNext()) {
			Directory directory = (Directory) directories.next();
			// iterate through tags and print to System.out
			Iterator<Tag> tags = directory.getTagIterator();
			while (tags.hasNext()) {
				Tag tag = (Tag) tags.next();
				if ("Exif".equals(tag.getDirectoryName()) || "Jpeg".equals(tag.getDirectoryName())) {
					setMetaValue(tag);
					// use Tag.toString()
					// System.out.println(tag.getDirectoryName());
					// System.out.println(tag.getTagName());
					// System.out.println(tag.getDescription());
					// System.out.println(tag);
				}
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getMetaValueXML() {
		StringBuffer sb = new StringBuffer("<root>\n");
		Set<String> keys = metaInfo.keySet();
		Iterator<String> i = keys.iterator();
		while (i.hasNext()) {
			String key = i.next();
			sb.append("\t<meta name=\"").append(key).append("\">").append(metaInfo.get(key)).append("</meta>\n");
		}
		sb.append("</root>");

		return sb.toString();
	}

	public static void main(String[] args) throws JpegProcessingException, MetadataException {
		ImageUtils iu = new ImageUtils("I:/icon3_l.gif");
		System.out.println(iu.getMetaValueXML());

		Vector<String> v = new Vector<String>();
		v.add("0");
		v.add("1");
		v.add("2");
		v.add("3");

		System.out.println(v.size());
		for (int i = 0; i < 4; i++) {
			if (i == 2) {
				v.removeElementAt(i);
			} else {
				System.out.println(v.get(i));
			}
		}
		System.out.println(v.size());

	}
}
