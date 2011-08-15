package net.ion.framework.xml.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import net.ion.framework.util.StringUtil;

import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

public class HtmlConverter {
	private StringWriter sw;
	private boolean xmlOut;

	public HtmlConverter() {
		this.xmlOut = true;
		sw = new StringWriter();
	}

	public String convert(String artContent) {
		String mataStr = "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">";
		artContent = StringUtil.replace(artContent, mataStr, "");

		Tidy tidy = new Tidy();
		tidy.setXmlOut(xmlOut);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		String convertStr = "";
		try {
			tidy.setErrout(new PrintWriter(sw, true));
			tidy.setCharEncoding(Configuration.UTF8);
			tidy.parse(new ByteArrayInputStream(artContent.getBytes("UTF-8")), bos);

			convertStr = new String(bos.toByteArray(), "UTF-8");
			String trimStr0 = "<html>\n<head>\n<meta content=\"HTML Tidy, see www.w3.org\" name=\"generator\">\n</meta>\n<title>\n</title>\n</head>\n<body>";
			String trimStr1 = "<html>\r\n<head>\r\n<meta name=\"generator\" content=\"HTML Tidy, see www.w3.org\" />\r\n<title></title>\r\n</head>\r\n<body>";
			String trimStr2 = "</body>\r\n</html>";
			convertStr = StringUtil.replace(convertStr, trimStr0, "");
			convertStr = StringUtil.replace(convertStr, trimStr1, "");
			convertStr = StringUtil.replace(convertStr, trimStr2, "");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertStr;
	}

	public String getErrMessage() {
		return sw.toString();
	}

	// public void convert(String inFileName, String outFileName, String errFileName) {
	// Tidy tidy = new Tidy();
	// tidy.setXmlOut(xmlOut);
	//
	// try {
	// tidy.setErrout(new PrintWriter(new FileWriter(errFileName), true));
	// tidy.setCharEncoding(Configuration.UTF8);
	// tidy.parse(new FileInputStream(inFileName), new FileOutputStream(outFileName));
	// } catch (Exception e) {
	// e.printStackTrace();
	// System.out.println(this.toString() + e.getMessage());
	// }
	// }

	// public String getXmlString(Node node) {
	// if (node == null) {
	// return "";
	// }
	//
	// StringBuffer xmlString = new StringBuffer();
	//
	// int type = node.getNodeType();
	// switch (type) {
	// case Node.DOCUMENT_NODE:
	// xmlString.append(getXmlString(((Document) node).getDocumentElement()));
	// break;
	// case Node.ELEMENT_NODE:
	// xmlString.append('<');
	// xmlString.append(node.getNodeName());
	// NamedNodeMap attrs = node.getAttributes();
	//
	// for (int i = 0; i < attrs.getLength(); i++) {
	// xmlString.append(' ');
	// xmlString.append(attrs.item(i).getNodeName());
	// xmlString.append("=\"");
	//
	// xmlString.append(attrs.item(i).getNodeValue());
	// xmlString.append('"');
	// }
	// xmlString.append('>');
	// xmlString.append("\n");
	//
	// NodeList children = node.getChildNodes();
	// if (children != null) {
	// int len = children.getLength();
	// for (int i = 0; i < len; i++) {
	// xmlString.append(getXmlString(children.item(i)));
	// }
	// }
	// break;
	// case Node.TEXT_NODE:
	// xmlString.append(node.getNodeValue());
	// break;
	// }
	//
	// if (type == Node.ELEMENT_NODE) {
	// xmlString.append("</");
	// xmlString.append(node.getNodeName());
	// xmlString.append('>');
	// xmlString.append("\n");
	// }
	//
	// return xmlString.toString();
	// }
}
