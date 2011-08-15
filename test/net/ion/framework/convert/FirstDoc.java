package net.ion.framework.convert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;
import net.ion.framework.parse.html.HTag;
import net.ion.framework.parse.html.NotFoundTagException;
import net.ion.framework.parse.html.TagVisitor;
import net.ion.framework.util.Debug;
import net.ion.framework.util.IOUtil;
import net.ion.framework.util.StringUtil;

import org.apache.commons.lang.CharEncoding;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

public class FirstDoc extends TestCase {

	public void testFont() throws Exception {

	}

	public void testHangul() throws IOException, DocumentException {
		String inputFile = "data/firstdoc.htm";
		String url = new File(inputFile).toURI().toURL().toString();
		System.out.println(url);
		String outputFile = "data/firstdoc.pdf";
		OutputStream output = new FileOutputStream(outputFile);

		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver resolver = renderer.getFontResolver();
		resolver.addFont("C:\\WINDOWS\\Fonts\\ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

		renderer.setDocument(url);
		renderer.layout();
		renderer.createPDF(output);

		output.close();
	}

	public void testSilverLightToXHTML() throws Exception {
		String inputFileName = new String("data/www.silverlight.net.htm");

		String xhtmlFileName = createXHTML(inputFileName) ;
		URL uri = new File(xhtmlFileName).toURL() ;
		
//		String xhtmlFileName = "data/www.silverlight.net.html.xhtml" ;
//		URL uri = new File(xhtmlFileName).toURL() ;
//		String outputFileName = xhtmlFileName + ".pdf";
//		createPDFFile(uri, outputFileName);
	}

	
	public void testSilverLightToPDF() throws Exception {
		String xhtmlFileName = "data/www.silverlight.net.htm.xhtml" ;
		URL uri = new File(xhtmlFileName).toURL() ;
		String outputFileName = xhtmlFileName + ".pdf";
		createPDFFile(uri, outputFileName);
	}

	
	public void testWeekDayList() throws Exception {
		String inputFileName = "data/weekdayList.nhn" ;
		String xhtmlFileName = createXHTML(inputFileName) ;
		
		URL uri = new File(xhtmlFileName).toURL() ;
		String outputFileName = xhtmlFileName + ".pdf";
		createPDFFile(uri, outputFileName);
	}
	
	public void testToXHTML() throws Exception {
		HTag tag = HTag.createGeneral(new FileReader("data/weekdayList.nhn"), "html") ;
		
		tag.visit(new TagVisitor() {
			public void visit(HTag tag) throws NotFoundTagException, IOException {
				System.out.println(StringUtil.repeat("\t", tag.getDepth()) + tag.getTagName() + ":" + tag.getChildren().size()) ; 
			}
		}) ;
	}
	
	public void testTidyXHTML() throws Exception {
		String inputFileName = "data/weekdayList.nhn" ;
		String xhtmlFileName = createXHTML(inputFileName) ;
		createXHTML(inputFileName) ;
	}
	
	public String createXHTML(String inputFileName) throws Exception {
		String outputfileName = inputFileName + ".xhtml" ;
		FileInputStream fis = null ;
		FileOutputStream fos = null ;
		try {
			fis = new FileInputStream(inputFileName);
			fos = new FileOutputStream(outputfileName);
			Tidy tidy = new Tidy();
			tidy.setCharEncoding(3) ;
			// tidy.s(true) ;
			tidy.setWraplen(5000) ;
			tidy.setSmartIndent(true) ;
			tidy.setFixBackslash(true) ;
			tidy.setQuoteAmpersand(false) ;
			tidy.setQuoteNbsp(true) ;
			tidy.parseDOM(fis, fos);
		} catch (IOException e) {
			e.printStackTrace() ;
		} finally {
			IOUtil.close(fis, fos) ;
		}
		
		return outputfileName ;
	}
	
	public void testPDF() throws Exception {
		URL uri = new File("data/weekdayList.nhn.xhtml").toURL() ;
		String outputFileName = "data/weekdayList.nhn.pdf" ;
		createPDFFile(uri, outputFileName);
	}
	
	private void createPDFFile(URL uri, String outputFileName) throws FileNotFoundException, DocumentException, IOException, MalformedURLException {
		OutputStream output = new FileOutputStream(outputFileName);

		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver resolver = renderer.getFontResolver();
		resolver.addFont("C:\\WINDOWS\\Fonts\\ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

		renderer.setDocument(uri.toString());
		renderer.layout();
		renderer.createPDF(output);

		output.close();
	}
	
	
	
	
}