package net.ion.framework.convert;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import net.ion.framework.convert.html.CleanerProperties;
import net.ion.framework.convert.html.CommentNode;
import net.ion.framework.convert.html.ContentNode;
import net.ion.framework.convert.html.HtmlSerializer;
import net.ion.framework.convert.html.TagNode;
import net.ion.framework.convert.html.Utils;
import net.ion.framework.util.ListUtil;


public class MyHtmlSerializer extends HtmlSerializer {

	public MyHtmlSerializer(CleanerProperties props) {
		this(props, "\t");
	}

	public MyHtmlSerializer(CleanerProperties props, String indentString) {
		super(props);
		this.indentString = "\t";
		indents = ListUtil.newList() ;
		this.indentString = indentString;
	}

	protected void serialize(TagNode tagNode, Writer writer) throws IOException {
		serializePrettyHtml(tagNode, writer, 0, false, true);
	}

	private synchronized String getIndent(int level) {
		int size = indents.size();
		if (size <= level) {
			String prevIndent = size != 0 ? (String) indents.get(size - 1) : null;
			for (int i = size; i <= level; i++) {
				String currIndent = prevIndent != null ? (new StringBuilder()).append(prevIndent).append(indentString).toString() : "";
				indents.add(currIndent);
				prevIndent = currIndent;
			}

		}
		return (String) indents.get(level);
	}

	private String getIndentedText(String content, int level) {
		String indent = getIndent(level);
		StringBuilder result = new StringBuilder(content.length());
		StringTokenizer tokenizer = new StringTokenizer(content, "\n\r");
		do {
			if (!tokenizer.hasMoreTokens())
				break;
			String line = tokenizer.nextToken().trim();
			if (!"".equals(line))
				result.append(indent).append(line).append("\n");
		} while (true);
		return result.toString();
	}

	private String getSingleLineOfChildren(List children) {
		StringBuilder result = new StringBuilder();
		Iterator childrenIt = children.iterator();
		for (boolean isFirst = true; childrenIt.hasNext(); isFirst = false) {
			Object child = childrenIt.next();
			if (!(child instanceof ContentNode))
				return null;
			String content = child.toString();
			if (isFirst)
				content = Utils.ltrim(content);
			if (!childrenIt.hasNext())
				content = Utils.rtrim(content);
			if (content.indexOf("\n") >= 0 || content.indexOf("\r") >= 0)
				return null;
			result.append(content);
		}

		return result.toString();
	}

	protected void serializeOpenTag(TagNode tagNode, Writer writer, boolean newLine) throws IOException {
		super.serializeOpenTag(tagNode, writer, newLine) ; 

		writer.write("!--" + tagNode.getName()) ;
	}

	protected void serializeEndTag(TagNode tagNode, Writer writer, boolean newLine) throws IOException {
		
		writer.write("--!") ;
		
		super.serializeEndTag(tagNode, writer, newLine) ;
	}

	protected void serializePrettyHtml(TagNode tagNode, Writer writer, int level, boolean isPreserveWhitespaces, boolean isLastNewLine) throws IOException {
		List tagChildren = tagNode.getChildren();
		String tagName = tagNode.getName();
		boolean isHeadlessNode = Utils.isEmptyString(tagName);
		String indent = isHeadlessNode ? "" : getIndent(level);
		if (!isPreserveWhitespaces) {
			if (!isLastNewLine)
				writer.write("\n");
			writer.write(indent);
		}
		serializeOpenTag(tagNode, writer, true);
		boolean preserveWhitespaces = isPreserveWhitespaces || "pre".equalsIgnoreCase(tagName);
		boolean lastWasNewLine = false;
		if (!isMinimizedTagSyntax(tagNode)) {
			String singleLine = getSingleLineOfChildren(tagChildren);
			boolean dontEscape = dontEscape(tagNode);
			if (!preserveWhitespaces && singleLine != null) {
				writer.write(dontEscape(tagNode) ? singleLine : escapeText(singleLine));
			} else {
				Iterator childIterator = tagChildren.iterator();
				do {
					if (!childIterator.hasNext())
						break;
					Object child = childIterator.next();
					if (child instanceof TagNode) {
						serializePrettyHtml((TagNode) child, writer, isHeadlessNode ? level : level + 1, preserveWhitespaces, lastWasNewLine);
						lastWasNewLine = false;
					} else if (child instanceof ContentNode) {
						String content = dontEscape ? child.toString() : escapeText(child.toString());

						if (content.length() > 0)
							if (dontEscape || preserveWhitespaces)
								writer.write(content);
							else if (Character.isWhitespace(content.charAt(0))) {
								if (!lastWasNewLine) {
									writer.write("\n");
									lastWasNewLine = false;
								}
								if (content.trim().length() > 0)
									writer.write(getIndentedText(Utils.rtrim(content), isHeadlessNode ? level : level + 1));
								else
									lastWasNewLine = true;
							} else {
								if (content.trim().length() > 0)
									writer.write(Utils.rtrim(content));
								if (!childIterator.hasNext()) {
									writer.write("\n");
									lastWasNewLine = true;
								}
							}
					} else if (child instanceof CommentNode) {
						if (!lastWasNewLine && !preserveWhitespaces) {
							writer.write("\n");
							lastWasNewLine = false;
						}
						CommentNode commentNode = (CommentNode) child;
						String content = commentNode.getCommentedContent();
						writer.write(dontEscape ? content : getIndentedText(content, isHeadlessNode ? level : level + 1));
					}
				} while (true);
			}
			if (singleLine == null && !preserveWhitespaces) {
				if (!lastWasNewLine)
					writer.write("\n");
				writer.write(indent);
			}
			
			
			
			serializeEndTag(tagNode, writer, false);
		}
	}

	private static final String DEFAULT_INDENTATION_STRING = "\t";
	private String indentString;
	private List indents;
}
