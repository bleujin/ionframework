package net.ion.framework.convert;

import java.io.*; //import java.net.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

public class HtmlToImage {

	public static void main(String[] ar) throws Exception {
		Runtime.getRuntime().exec("cmd /c @start \"\" /b \"C:\\Program Files\\Internet Explorer\\iexplore.exe\" www.gmail.com").waitFor();
		Thread.sleep(8000); // load till the page loads depends on you Internet speed

		Robot r = new Robot();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle rct = new Rectangle(0, 0, d.width, d.height);
		BufferedImage bimg = r.createScreenCapture(rct);
		ImageIO.write(bimg, "jpeg", new File("a.jpg"));

	}

}
