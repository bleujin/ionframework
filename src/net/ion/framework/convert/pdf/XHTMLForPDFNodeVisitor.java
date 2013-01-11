package net.ion.framework.convert.pdf;

import static net.htmlparser.jericho.HTMLElementName.A;
import static net.htmlparser.jericho.HTMLElementName.B;
import static net.htmlparser.jericho.HTMLElementName.BODY;
import static net.htmlparser.jericho.HTMLElementName.BR;
import static net.htmlparser.jericho.HTMLElementName.DD;
import static net.htmlparser.jericho.HTMLElementName.DIV;
import static net.htmlparser.jericho.HTMLElementName.DL;
import static net.htmlparser.jericho.HTMLElementName.DT;
import static net.htmlparser.jericho.HTMLElementName.EM;
import static net.htmlparser.jericho.HTMLElementName.H1;
import static net.htmlparser.jericho.HTMLElementName.H2;
import static net.htmlparser.jericho.HTMLElementName.H3;
import static net.htmlparser.jericho.HTMLElementName.H4;
import static net.htmlparser.jericho.HTMLElementName.H5;
import static net.htmlparser.jericho.HTMLElementName.H6;
import static net.htmlparser.jericho.HTMLElementName.I;
import static net.htmlparser.jericho.HTMLElementName.LEGEND;
import static net.htmlparser.jericho.HTMLElementName.LI;
import static net.htmlparser.jericho.HTMLElementName.P;
import static net.htmlparser.jericho.HTMLElementName.SPAN;
import static net.htmlparser.jericho.HTMLElementName.STRONG;
import static net.htmlparser.jericho.HTMLElementName.TD;
import static net.htmlparser.jericho.HTMLElementName.UL;

import java.net.MalformedURLException;
import java.net.URL;

import net.ion.framework.convert.html.CommentNode;
import net.ion.framework.convert.html.ContentNode;
import net.ion.framework.convert.html.HtmlNode;
import net.ion.framework.convert.html.TagNode;
import net.ion.framework.convert.html.TagNodeVisitor;
import net.ion.framework.util.StringUtil;

import org.apache.commons.lang.ArrayUtils;

public class XHTMLForPDFNodeVisitor implements TagNodeVisitor {

	private URL url;

	public XHTMLForPDFNodeVisitor(URL url) {
		this.url = url;
	}

	public boolean visit(TagNode parentNode, HtmlNode node) {
		if (node instanceof TagNode) {
			TagNode tagNode = (TagNode) node;
			String name = tagNode.getName();

			if ("head".equals(name)) {
				tagNode.addChild(makeStyleTag());
			} else if ("script".equals(name)) {
				parentNode.removeChild(node);
				// tagNode.removeAllChildren();
			} else if ("link".equals(name) && tagNode.hasAttribute("href")) {
				tagNode.setAttribute("href", makeAbsolutePath(tagNode.getAttributeByName("href")));
			}
		} else if (node instanceof ContentNode) {
			ContentNode cnode = (ContentNode) node;
			if (!isCharZone(parentNode))
				return true;
			if (StringUtil.isBlank(cnode.getContent().toString()))
				return true;
			parentNode.removeChild(cnode);
			parentNode.addChild(makeStdFontTag().addChild(cnode));
		} else if (node instanceof CommentNode) {
		}
		return true;
	}

	private String makeAbsolutePath(String link) {
		try {
			String protocol = url.getProtocol();

			if (link.startsWith(protocol + ":")) {
				return link;
			} else if (link.startsWith("/")) {
				return new URL(url.getProtocol(), url.getHost(), url.getPort(), link).toString();
			} else if (link.startsWith("..") || link.startsWith(".")) {
				return new URL(url, link).toString();
			} else {
				throw new MalformedURLException(url.toString() + ", " + link);
			}
		} catch (MalformedURLException ex) {
			return link ;
		}
	}

	private boolean isCharZone(TagNode tnode) {
		return ArrayUtils.contains(CHAR_ZONE_NODE, tnode.getName());
	}

	private TagNode makeStdFontTag() {
		TagNode node = new TagNode("font");
		node.setAttribute("class", "stdfont");
		return node;
	}

	private TagNode makeStyleTag() {
		TagNode node = new TagNode("style");
		node.setAttribute("type", "text/css");
		node.addChild(new ContentNode(".stdfont{font-family: \"Arial Unicode MS\";}"));
		return node;
	}

	private static String[] CHAR_ZONE_NODE = new String[] { TD, DIV, A, BODY, SPAN, H1, H2, H3, H4, H5, H6, UL, LI, DL, DD, DT, I, B, STRONG, BR, P, LEGEND, EM };
}
