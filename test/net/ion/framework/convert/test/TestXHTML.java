package net.ion.framework.convert.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;

import junit.framework.TestCase;
import net.ion.framework.convert.MyHtmlSerializer;
import net.ion.framework.convert.html.CleanerProperties;
import net.ion.framework.convert.html.ContentNode;
import net.ion.framework.convert.html.HtmlCleaner;
import net.ion.framework.convert.html.HtmlNode;
import net.ion.framework.convert.html.HtmlSerializer;
import net.ion.framework.convert.html.PrettyHtmlSerializer;
import net.ion.framework.convert.html.PrettyXmlSerializer;
import net.ion.framework.convert.html.TagNode;
import net.ion.framework.convert.html.TagNodeVisitor;
import net.ion.framework.convert.pdf.XHTMLForPDFNodeVisitor;
import net.ion.framework.util.Debug;
import net.ion.framework.util.StringUtil;


public class TestXHTML extends TestCase{

	
	public void testCreatePdf() throws Exception {
		CleanerProperties props = getProp();
		final URL url = new URL("http://comic.naver.com/webtoon/weekday.nhn") ;
		final String filename = "data/" + StringUtil.substringAfterLast(url.toString(), "/");

//		final URL url = new URL("http://www.daum.net/?nil_profile=daum&nil_src=sports") ;
//		final String filename = "data/daum" ;
		
		TagNode node = parseNode(props, url) ;
		
		TagNodeVisitor visitor = new XHTMLForPDFNodeVisitor(url);
		node.traverse(visitor) ;
		new PrettyHtmlSerializer(props).writeToStream(
			    node, new FileOutputStream(new File(filename + ".html")), "utf-8"
			);
		
		createPDF(filename) ;
	}
	

	public void testMakeXHTML() throws Exception {
		CleanerProperties props = getProp();
		final URL url = new URL("http://search.daum.net/search?w=tot&nil_no=170975&q=%EA%B5%B0%EC%82%B0+%ED%95%B4%EC%96%91%ED%85%8C%EB%A7%88%EA%B3%B5%EC%9B%90&guide=content&t__nil_searchcontent=txt&nil_id=4") ;
		final String filename = "data/search.html";
		TagNode node = parseNode(props, url) ;
		new PrettyHtmlSerializer(props).writeToStream(
			    node, new FileOutputStream(new File(filename + ".html")), "utf-8"
			);
	}
	

	public void createPDF(String filename) throws Exception {
		URL uri = new File(filename + ".html").toURL() ;
		String outputFileName = filename + ".pdf" ;
		createPDFFile(uri, outputFileName);
	}
	
	private void createPDFFile(URL uri, String outputFileName) throws FileNotFoundException, DocumentException, IOException, MalformedURLException {
		OutputStream output = new FileOutputStream(outputFileName);

		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver resolver = renderer.getFontResolver();
//		resolver.addFontDirectory("C:\\WINDOWS\\Fonts\\", false) ;
		resolver.addFont("C:\\WINDOWS\\Fonts\\ARIALUNI.TTF", 	BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//		resolver.addFont("C:\\WINDOWS\\Fonts\\TAHOMA.TTF", 		BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//		resolver.addFont("C:\\WINDOWS\\Fonts\\TAHOMABD.TTF",	BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

		Document doc = new Document(PageSize.A4) ;
		
		renderer.setDocument(uri.toString());
		renderer.layout();
		renderer.createPDF(output);

		output.close();
	}
	
	public void testTagRename() throws Exception {
		CleanerProperties props = getProp();
		TagNode tagNode = parseNode(props, new File("data/firstdoc.htm").toURL());

		tagNode.traverse(new TagNodeVisitor() {
			
			public boolean visit(TagNode parentNode, HtmlNode htmlNode) {
				
				if (parentNode != null && (! parentNode.getName().startsWith("DAV:"))) parentNode.setName("DAV:" + parentNode.getName()) ;
				return true;
			}
		}) ;
		
		// serialize to xml file
		new PrettyXmlSerializer(props).writeXmlToStream(
		    tagNode, System.out, "utf-8"
		);
	}

	public void testBasic() throws Exception {
		CleanerProperties props = getProp();
		TagNode tagNode = parseNode(props, new URL("http://www.chinadaily.com.cn/"));

		
		// serialize to xml file
		new PrettyXmlSerializer(props).writeToFile(
		    tagNode, "data/chinadaily.xml", "utf-8"
		);
	}

	private TagNode parseNode(CleanerProperties props, URL url) throws IOException, MalformedURLException {
		TagNode tagNode = new HtmlCleaner(props).clean(url);
		return tagNode;
	}
	

	private CleanerProperties getProp() {
		CleanerProperties props = new CleanerProperties();
		 
		// set some properties to non-default values
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setUseCdataForScriptAndStyle(true);
		props.setOmitComments(true);
		props.setOmitXmlDeclaration(true) ;
		return props;
	}
	
}
