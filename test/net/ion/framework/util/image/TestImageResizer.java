package net.ion.framework.util.image;

import java.io.File;
import java.io.FileOutputStream;

import junit.framework.TestCase;
import net.ion.framework.util.Debug;

public class TestImageResizer extends TestCase {

	
	public void testFirst() throws Exception {
		Debug.line(new File("./").getAbsolutePath());
		ImageResizer r = ImageResizer.create("./resource/temp/한글이름.jpg", 150, 100) ;
		
		r.resizeTo(new FileOutputStream(new File("./resource/temp/cat.jpg")));
	}
}
