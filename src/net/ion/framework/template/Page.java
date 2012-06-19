package net.ion.framework.template;

import net.ion.framework.template.tagext.PageInfo;

/**
 * template renderer�� ���
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class Page {
	private PageInfo pageInfo = null;
	private String pageText = null;

	/**
	 * @param pageInfo
	 *            PageInfo rendering�� page�� ���� ����
	 * @param pageText
	 *            String page text
	 */
	public Page(PageInfo pageInfo, String pageText) {
		this.pageInfo = pageInfo;
		this.pageText = pageText;
	}

	public String toString() {
		String text = (this.pageText.length() > 30) ? this.pageText.substring(0, 30) + "..." : this.pageText;
		return "{pageInfo=" + this.pageInfo + ",pageText=" + text.getClass().getName() + "@" + Integer.toHexString(text.hashCode()) + "}";
	}

	/**
	 * @return PageInfo page rendering ����
	 */
	public PageInfo getPageInfo() {
		return this.pageInfo;
	}

	/**
	 * @return String rendering �� page ���
	 */
	public String getPageText() {
		return this.pageText;
	}
}
