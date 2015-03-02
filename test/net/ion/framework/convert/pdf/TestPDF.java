package net.ion.framework.convert.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import net.ion.framework.convert.html.CleanerProperties;
import net.ion.framework.convert.html.HtmlCleaner;
import net.ion.framework.convert.html.PrettyXmlSerializer;
import net.ion.framework.convert.html.TagNode;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

import junit.framework.TestCase;

public class TestPDF extends TestCase {

	public void testPDF() throws Exception {
		URL uri = new File("./resource/data/AChristmasCarol.txt").toURL() ;
		// URL uri = new URL("http://61.250.201.157:9000/admin/craken/") ;
		String outputFileName = "./resource/data/mlbnation.pdf" ;
		createPDFFile(uri, outputFileName);
	}
	
	public void testFirstPDF() throws Exception {
		createPDFFile(new File("data/firstdoc.htm").toURL(), "data/firstdoc.htm.pdf");
	}
	
	private void createPDFFile(URL uri, String outputFileName) throws FileNotFoundException, DocumentException, IOException, MalformedURLException {
		OutputStream output = new FileOutputStream(outputFileName);

		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver resolver = renderer.getFontResolver();
//		resolver.addFontDirectory("C:\\WINDOWS\\Fonts\\", false) ;
		resolver.addFont("C:\\WINDOWS\\Fonts\\ARIALUNI.TTF", 	BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//		resolver.addFont("C:\\WINDOWS\\Fonts\\TAHOMA.TTF", 		BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//		resolver.addFont("C:\\WINDOWS\\Fonts\\TAHOMABD.TTF",	BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

		renderer.setDocument(uri.toString());
		renderer.layout();
		renderer.createPDF(output);

		output.close();
	}

	
	
	public void testToImage() throws Exception {
		PDDocument pdoc = PDDocument.load("data/weekdayList.nhn.pdf") ;
		
		pdoc.removePage(1) ;
		
		PDPage page = (PDPage) pdoc.getDocumentCatalog().getAllPages().get(0) ;
		
		BufferedImage image = page.convertToImage() ;
		ImageOutputStream output = ImageIO.createImageOutputStream(new File("data/weekdayList.nhn.pdf.jpg")) ;
		ImageWriter iw = ImageIO.getImageWritersByFormatName("jpg").next() ;
		iw.setOutput(output) ;
		ImageWriteParam writerParams = iw.getDefaultWriteParam();
		iw.write(null, new IIOImage(image, null, null), writerParams) ;
		
		iw.dispose() ;
		output.flush() ;
		output.close() ;
		pdoc.close() ;
	}
	
}
