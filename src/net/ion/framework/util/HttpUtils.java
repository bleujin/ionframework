package net.ion.framework.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

/**
 * HttpServlet���� �����ϰ� ����� �� �ִ� ��� ����
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public class HttpUtils {
	// private static String START_DELIM = "@#s#@";
	// private static String END_DELIM = "@#e#@";

	private HttpUtils() {
	}

//	/**
//	 * JSP capsulation
//	 * 
//	 * <%response.setContentType("text/html");%> <%response.setLocale(java.util.Locale.KOREA);%> <%session.setAttribute(org.apache.struts.Globals.LOCALE_KEY,java.util.Locale.KOREA);%>
//	 * 
//	 * @param locale
//	 *            HTTP responselocale, character set.
//	 * @param pageContext
//	 */
//	public static void setHttpResponseLocale(Locale locale, PageContext pageContext) {
//		setHttpResponseLocale(locale, pageContext.getSession(), pageContext.getResponse());
//	}

	/**
	 * http reponse�� locale�� �����Ѵ�.
	 * 
	 * @param locale
	 *            Locale ������ locale
	 * @param session
	 *            HttpSession �۾� http session
	 * @param response
	 *            ServletResponse ������ reponse
	 */
	public static void setHttpResponseLocale(Locale locale, HttpSession session, ServletResponse response) {
		response.setLocale(locale);

		String charset = "UTF-8"; // 2004.11.3
		setHttpResponseCharset(response, charset);

	}

	/**
	 * http response�� charse
	 * 
	 * @param response
	 *            ServletResponse
	 * @param charset
	 *            String
	 */
	public static void setHttpResponseCharset(ServletResponse response, String charset) {
		if (charset == null)
			response.setContentType("text/html");
		else
			response.setContentType("text/html; charset=" + charset);
	}

	/**
	 * html tag�� �����Ѵ�.
	 * 
	 * @param value
	 *            String
	 * @return String
	 */

	public static String removeHTMLEntity(String value) {
		if (value == null) {
			return (null);
		}

		value = StringUtil.replace(value, "&nbsp;", " ");
		value = value.replaceAll("<!--.*?-->", "");
		value = replaceBlockAll("<", ">", value, "");
		return value.trim();
	}

	public static String extractText(String value) {
		if (value == null) {
			return (null);
		}

		try {
			Source source = new Source(new StringReader(value));
			Segment segment = new Segment(source, source.getBegin(), source.getEnd());
			Writer writer = new StringWriter();
			segment.getTextExtractor().writeTo(writer);
			return writer.toString();
		} catch (IOException ex) {
			return "";
		}
	}

	public static String htmlEntityDecode(String value) {
		if (value == null) {
			return (null);
		}

		value = StringUtil.chomp(value);
		value = StringUtil.replace(value, "&nbsp;", " ");
		value = value.replaceAll("<!--.*?-->", "");
		value = replaceBlockAll("<", ">", value, " ");
		value = value.replaceAll("\\s+", " "); //

		return value.trim();
	}

	public static String[] getBlockString(String startBlock, String endBlock, String value) {
		if (value == null || StringUtil.isEmpty(startBlock) || StringUtil.isEmpty(endBlock)) {
			return new String[0];
		}

		ArrayList<String> list = new ArrayList<String>();

		int beginIndex = 0;
		int endIndex = 0;
		int tmpIndex = 0;
		while (true) {
			beginIndex = value.indexOf(startBlock, beginIndex);
			if (beginIndex == -1) {
				break;
			}
			endIndex = value.indexOf(endBlock, beginIndex);
			if (endIndex == -1) {
				break;
			}

			tmpIndex = value.indexOf(startBlock, beginIndex + 1);
			if (tmpIndex != -1 && tmpIndex < endIndex) {
				beginIndex = tmpIndex;
				continue;
			}

			list.add(value.substring(beginIndex + startBlock.length(), endIndex));
			beginIndex = endIndex + endBlock.length();
		}
		return list.toArray(new String[0]);
	}

	public static String replaceBlock(String startBlock, String endBlock, String value, String startReplaceStr, String endReplaceStr) {
		if (value == null) {
			return (null);
		}

		int beginIndex = 0;
		int endIndex = 0;
		int tmpIndex = 0;
		while (true) {
			beginIndex = value.indexOf(startBlock, beginIndex);
			if (beginIndex == -1) {
				break;
			}
			endIndex = value.indexOf(endBlock, beginIndex);
			if (endIndex == -1) {
				break;
			}

			tmpIndex = value.indexOf(startBlock, beginIndex + 1);
			if (tmpIndex != -1 && tmpIndex < endIndex) {
				beginIndex = tmpIndex;
				continue;
			}

			StringBuffer strReturn = new StringBuffer();
			strReturn.append(value.substring(0, beginIndex));
			strReturn.append(startReplaceStr);
			strReturn.append(value.substring(beginIndex + startBlock.length(), endIndex));
			strReturn.append(endReplaceStr);
			strReturn.append(value.substring(endIndex + endBlock.length(), value.length()));
			beginIndex = beginIndex + startReplaceStr.length() + (endIndex - (beginIndex + startBlock.length())) + endReplaceStr.length();
			value = strReturn.toString();
		}
		return value;
	}

	public static String replaceBlockAll(String startBlock, String endBlock, String value, String replaceStr) {
		if (value == null) {
			return (null);
		}

		int beginIndex = 0;
		int endIndex = 0;
		int tmpIndex = 0;
		StringBuffer result = new StringBuffer(value);
		while (true) {
			beginIndex = result.indexOf(startBlock, beginIndex);
			if (beginIndex == -1) {
				break;
			}
			endIndex = result.indexOf(endBlock, beginIndex);
			if (endIndex == -1) {
				break;
			}
			tmpIndex = result.indexOf(startBlock, beginIndex + 1);
			if (tmpIndex != -1 && tmpIndex < endIndex) {
				beginIndex = tmpIndex;
				continue;
			}

			StringBuffer strReturn = new StringBuffer();
			strReturn.append(result.substring(0, beginIndex));

			strReturn.append(replaceStr);
			strReturn.append(result.substring(endIndex + endBlock.length(), result.length()));
			beginIndex = beginIndex + replaceStr.length();
			result = strReturn;
		}
		return result.toString();
	}

	/**
	 * '\n' �� html BR tag�� ġȯ
	 * 
	 * @param value
	 *            String
	 * @return String
	 */
	public static String replaceNewLine(String value) {
		if (value == null) {
			return (null);
		}

		StringBuffer buf = new StringBuffer();
		int length = value.length();
		for (int i = 0; i < length; ++i) {
			if (value.charAt(i) == '\n') {
				buf.append("<br/>");
			} else {
				buf.append(value.charAt(i));
			}
		}
		return buf.toString().replaceAll("\r", "");
	}

	public static String removeString(String value, String[] removes) {
		if (value == null) {
			return (null);
		}

		for (int i = 0; i < removes.length; i++) {
			value = StringUtil.replace(value, removes[i], "");
		}
		return value;
	}

	/**
	 * ���ڿ��� length ��ŭ ���̷� �ڸ��� ...�� ���δ�.
	 * 
	 * @param value
	 *            String
	 * @param length
	 *            int
	 * @return String
	 */
	public static String cutString(String value, int length) {
		if (value == null) {
			return (null);
		}

		if (value.length() > length)
			return value.substring(0, length) + "...";
		else
			return value;
	}

	/**
	 * html ���� ���ڸ� &amp �� �����ϴ� ��ȣ�� ġȯ�Ѵ�.
	 * 
	 * @param value
	 *            String
	 * @return String
	 */
	public static String filterHTML(String value) {
		if (value == null) {
			return (null);
		}

		char content[] = new char[value.length()];
		value.getChars(0, value.length(), content, 0);
		StringBuffer result = new StringBuffer(content.length + 50);
		for (int i = 0; i < content.length; i++) {
			switch (content[i]) {
			case '&':
				result.append("&amp;");
				break;
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '"':
				result.append("&quot;");
				break;
			case '\'':
				result.append("&#39;");
				break;
			case ' ':
				result.append("&nbsp;");
				break;
			default:
				result.append(content[i]);
			}
		}
		return (result.toString());
	}

	/**
	 * space ���� ó���� �����ϰ� filterHTML() �� ����
	 * 
	 * @param value
	 *            String
	 * @return String
	 */
	public static String filterHTMLNotSpace(String value) {
		if (value == null) {
			return (null);
		}

		char content[] = new char[value.length()];
		value.getChars(0, value.length(), content, 0);
		StringBuffer result = new StringBuffer(content.length + 50);
		for (int i = 0; i < content.length; i++) {
			switch (content[i]) {
			case '&':
				result.append("&amp;");
				break;
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '"':
				result.append("&quot;");
				break;
			case '\'':
				result.append("&#39;");
				break;
			default:
				result.append(content[i]);
			}
		}
		return (result.toString());
	}

	/**
	 * value���� startBlock�� endBlock ���ڿ��� �ش�Ǵ� ���� �κ��� ������ ���ڿ��� �����Ѵ�.
	 * 
	 * @param value
	 *            String
	 * @param startBlock
	 *            String
	 * @param endBlock
	 *            String
	 * @return String
	 */
	public static String selectDeleteString(String value, String startBlock, String endBlock) {
		if (value == null) {
			return (null);
		}

		int spos = value.indexOf(startBlock);
		if (spos != -1) {
			String tmpString = "";
			tmpString = value.substring(0, spos);
			tmpString += value.substring(value.indexOf(endBlock, spos) + endBlock.length());
			return tmpString;
		} else {
			return value;
		}
	}

	/**
	 * &amp �� �����ϴ� ��ȣ�� �� ���ڷ� �ǵ�����.
	 * 
	 * @param value
	 *            String
	 * @return String
	 */
	public static String filterHTMLdecode(String value) {
		if (value == null) {
			return (null);
		}

		value = StringUtil.replace(value, "&lt;", "<");
		value = StringUtil.replace(value, "&gt;", ">");
		value = StringUtil.replace(value, "&quot;", "\"");
		value = StringUtil.replace(value, "&amp;", "&");

		return (value);
	}

	public static String toRemotePath(String baseUrl, String targetUrl) {
		String[] baseUrls = StringUtil.split(baseUrl, "/");
		String[] targetUrls = StringUtil.split(targetUrl, "/");

		int defIdx = 0;
		if (targetUrls.length > 0) {
			for (int i = 0; i < baseUrls.length; i++) {
				if (baseUrls[i].equals(targetUrls[i])) {
					defIdx++;
				} else {
					break;
				}
			}
		}

		int cnt = baseUrls.length - defIdx;
		String result = "";
		for (int i = 0; i < cnt; i++) {
			result += "../";
		}

		for (int i = defIdx; i < targetUrls.length; i++) {
			result += (defIdx == i ? "" : "/") + targetUrls[i];
		}

		if (!result.startsWith(".")) {
			result = "./" + result;
		}

		return result;
	}

	public static void main(String[] args) {
		String baseUrl = "/par/cp/";
		String targetUrl = "/com/over/index.html";
		System.out.println(HttpUtils.toRemotePath(baseUrl, targetUrl));
		// ../../com/over/index.html

		baseUrl = "/par/cp/";
		targetUrl = "/";
		System.out.println(HttpUtils.toRemotePath(baseUrl, targetUrl));
		// ../../

		baseUrl = "/par/cp/";
		targetUrl = "/pro/overview/index.html";
		System.out.println(HttpUtils.toRemotePath(baseUrl, targetUrl));
		// ../../pro/overview/index.html

		baseUrl = "/par/cp/";
		targetUrl = "/par/cp/overview/index.html";
		System.out.println(HttpUtils.toRemotePath(baseUrl, targetUrl));
		// overview/index.html

		// //
		// for(int i=0; i<100; i++)
		// {
		// value += "dfghdgsdhhhh gfdsgdfs gdgggg \n";
		// }
		// value += "dfghdgsdhhhh gfds-->gdfs gdgggg \n";
		// value=value.replaceAll("<[p|P].*?>",START_DELIM+"br"+END_DELIM+START_DELIM+"br"+END_DELIM);
		// System.out.println(value);

		// String value= "xx xc xc [[--ArtInImage,fileLoc:/asdf/asdf/xcv.gif--]]a af a ad\nxx xc xc [[--ArtInImage,fileLoc:/asdf/asdf/xcv2.gif--]]a af a ad\n" ;
		// String[] xx = HttpUtils.getBlockString("[[--ArtInImage,fileLoc:", "--]]", value);
		// for(int i=0; i<xx.length; i++) {
		// System.out.println(xx[i]);
		// }

	}

}
