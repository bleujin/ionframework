package net.ion.framework.file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;
import net.ion.framework.parse.gson.JsonObject;
import net.ion.framework.util.Debug;
import net.ion.framework.util.IOUtil;
import net.ion.framework.util.ObjectUtil;
import net.ion.framework.util.StringUtil;

import org.restlet.data.MediaType;
import org.restlet.service.MetadataService;

import com.qbox.summary.PropertyManager;

public class TestPropFile extends TestCase {

	public void testFileModify() throws Exception {

		String hangulName = "한글이름.jpg";
		JsonObject meta = JsonObject.create();
		meta.addProperty("filename", hangulName);
		String metaString = meta.toString();

		FileInputStream src = new FileInputStream(new File("./resource/temp/한글이름.jpg"));

		FileOutputStream output = new FileOutputStream(new File("./resource/temp/output.jpg"));

		byte[] metaBytes = metaString.getBytes("UTF-8");
		int length = metaBytes.length;

		DataOutputStream dout = new DataOutputStream(output);
		dout.writeInt(length);
		dout.write(metaBytes);
		// IOUtil.copyNClose(new FileInputStream(file), dout) ;
	}

	public void testReadFile() throws Exception {
		DataInputStream dinput = new DataInputStream(new FileInputStream(new File("./resource/temp/output.jpg")));
		int length = dinput.readInt();
		byte[] bytes = new byte[length];
		dinput.read(bytes);
		String meta = new String(bytes, "UTF-8");
		Debug.line(meta);

		IOUtil.copyNClose(dinput, new FileOutputStream("./resource/temp/copy.jpg"));
	}

	public void testHash() throws Exception {
		String str = "resource/temp/한글파일.jpg";
		byte[] bytes = str.getBytes("UTF-8");
		String hexString = HexUtil.toHex(bytes);
		
		Debug.line(PropFileManager.toHexSplit(bytes, 3)) ;
		
		for (int i = 1; i < 10; i++) {
			String[] splitHexString = PropFileManager.toHexSplit(bytes, i) ;
			Debug.line(splitHexString, i, hexString.length()) ;
			assertEquals(splitHexString[0].length(), i) ; 
		}
	}
	
	private InputStream getSrcStream() throws FileNotFoundException{
		return new FileInputStream(new File("./resource/temp/한글이름.jpg"));
	}

	public void testMetaService() throws Exception {
		MetadataService mservice = new MetadataService();
		Debug.line(mservice.getMediaType("jpg")) ;
	}
	
	public void testReadNWrite() throws Exception {
		PropFileManager pfm = PropFileManager.create("./resource/temp");
		pfm.save("123/고양고양이.jpg", JsonObject.create().put("prop1", "고양이").put("동작", "하품").put("mediatype", findMediaType("jpg")), getSrcStream()) ;
		pfm.save("123/22/야옹야옹이.jpg", JsonObject.create().put("prop1", "고양이").put("동작", "하품").put("mediatype", findMediaType("jpg")), getSrcStream()) ;

		PropFile readInfo = pfm.read("123/고양고양이.jpg") ;
		
		assertEquals(true, readInfo.exist()) ;
		assertEquals("123/고양고양이.jpg", readInfo.filePath()) ;
		assertEquals(getSrcStream().available(), readInfo.inputStream().available()) ;
		assertEquals("하품", JsonObject.fromString(readInfo.propJson()).asString("동작")) ;
		
	}
	
	public void testListFile() throws Exception {
		PropFileManager pfm = PropFileManager.create("./resource/temp");
		pfm.save("456/1.jpg", JsonObject.create().put("prop1", "고양이").put("동작", "하품").put("mediatype", findMediaType("jpg")), getSrcStream()) ;
		pfm.save("456/12/1.jpg", JsonObject.create().put("prop1", "고양이").put("동작", "하품").put("mediatype", findMediaType("jpg")), getSrcStream()) ;

		//		List<PropFile> pfiles = pfm.listFile("123/") ;
//		assertEquals(2, pfiles.size()) ;
//		assertEquals("123/고양고양이.jpg", pfiles.get(1).filePath()) ;
//		assertEquals("123/22/야옹야옹이.jpg", pfiles.get(0).filePath()) ;

		List<PropFile> pfiles = pfm.listFile("456/12/");
		assertEquals(1, pfiles.size()) ;
		assertEquals("456/12/1.jpg", pfiles.get(0).filePath()) ;
	}
	

	
	private static MetadataService mservice = new MetadataService() ;
	private static String findMediaType(String fileName) {
		String extension = StringUtil.substringAfterLast(fileName, ".") ;
		if (StringUtil.isBlank(extension)) {
			MediaType mediaType = mservice.getMediaType(fileName);
			return ObjectUtil.toString(mediaType) ;
		}
		MediaType mediaType = mservice.getMediaType(extension);
		return ObjectUtil.toString(mediaType) ;
	}



	
	
}
