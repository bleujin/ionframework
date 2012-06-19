package net.ion.framework.parse.html;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;

import org.apache.http.impl.cookie.DateUtils;

import junit.framework.TestCase;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.Tag;
import net.ion.framework.util.DateUtil;
import net.ion.framework.util.Debug;
import net.ion.framework.util.FileUtil;
import net.ion.framework.util.IOUtil;

public class HTMLTest extends TestCase {

	public void testInsensitive() throws Exception {

		String str = "<Hello Attr='Tag'>Hello</hello>";
		StringReader reader = new StringReader(str);

		HTag root = GeneralParser.parseHTML(reader);

		assertEquals("hello", root.getTagName());
		assertEquals("Tag", root.getAttributeValue("attr"));
	}

	public void testEmpty() throws Exception {
		String str = "";
		StringReader reader = new StringReader(str);

		HTag root = GeneralParser.parseHTML(reader);
	}

	private String html = "<!DOCTYPE html>" + "<script atrr='a1'>Bla Bla</script>" + "<html><head><title>TITLE</title></head><body></body></html>";

	public void testDocType() throws Exception {
		HTag root = GeneralParser.parseHTML(new StringReader(html));
		assertEquals("TITLE", root.getChild("head/title").getOnlyText());

	}

	public void testOnlyText() throws Exception {
		HTag root = GeneralParser.parseHTML(new StringReader(html));
		assertEquals("TITLE", root.getOnlyText());
		assertEquals("TITLE", root.getChild("head").getOnlyText());
		assertEquals("TITLE", root.getChild("head/title").getOnlyText());
	}

	public void testPrefix() throws Exception {
		HTag root = GeneralParser.parseHTML(new StringReader(html));
		assertEquals(true, root.getPrefixTag().hasChild());
	}

	public void testContent() throws Exception {
		HTag root = GeneralParser.parseHTML(new StringReader(html));
		HTag script = root.getPrefixTag().getChildren().get(1);

		assertEquals("<script atrr='a1'>Bla Bla</script>", script.getContent());
		assertEquals(false, script.hasChild());
		assertEquals(0, script.getChildren().size());
	}

	public void testTagText() throws Exception {
		HTag root = GeneralParser.parseHTML(new StringReader(html));
		HTag script = root.getPrefixTag().getChildren().get(1);

		assertEquals("Bla Bla", script.getTagText());
		assertEquals("<head><title>TITLE</title></head><body></body>", root.getTagText());
	}

	public void testDepth() throws Exception {
		HTag root = GeneralParser.parseHTML(new StringReader(html));
		assertEquals(0, root.getDepth());
	}

	public void testGetValue() throws Exception {
		String s = "<root abcd='d'><abc><center atr='3'>gacdd</center><br><br><br></abc><pd></pd></root>";
		HTag root = HTag.createGeneral(new StringReader(s), "root");

		assertEquals("gacdd", root.getValue("abc/center"));
		assertEquals("3", root.getValue("abc/center@atr"));
		assertEquals("d", root.getValue("@abcd"));
		// assertEquals("3", root.getValue("p/center/@a")) ;
	}

	public void testChild() throws Exception {
		String s = "<root><p><center><img src=''/>gacdd</center><br><br><br></p><p></p></root>";
		HTag root = GeneralParser.parseHTML(new StringReader(s));

		Debug.debug(DateUtils.formatDate(new Date(), "yyyyMMdd") + "/DEFAULT");

		for (HTag child : root.getChildren()) {
			Element celement = child.getElement();
			Debug.debug(child, celement.getDebugInfo(), celement.getEndTag(), celement.getBegin(), celement.getEnd());

		}
	}

	public void xtestBigParse() throws Exception {
		try {
			HTag root = HTag.createGeneral(new BufferedReader(new FileReader(new File("c:/temp/BookItem2nd.aspx"))), "html");
			Debug.line(root.getChild("/html/head"));

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void xtestStack() throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 900; i++) {
			sb.append("<span href=\"" + i + "\">");
			sb.append("0123456789ABCDEF ");
			sb.append("<span href=\"" + i + ".1\">");
			sb.append("first");
			sb.append("</span>");
			sb.append("<span href=\"" + i + ".2\">");
			sb.append("second");
			sb.append("</span>");
			sb.append("<span href=\"" + i + ".3\"/>");
			sb.append("</span>");
		}
		Source source = new Source(sb);

//		Debug.line(source.getMaxDepthIndicator());
//
//		int maxDepth=0;
//		int depth=0;
//		for (Tag tag : source.getAllTags()) {
//			if (tag instanceof StartTag) {
//				if (!((StartTag) tag).isEmptyElementTag()) {
//					depth++;
//					if (depth > maxDepth)
//						maxDepth++;
//				}
//			} else {
//				depth--;
//			}
//			
//		}
//		Debug.line(maxDepth, depth) ;
		
		HTag root = HTag.createGeneral(new StringReader(sb.toString()), "html");
		// Debug.line(source.getAllElements().size());
	}

}
