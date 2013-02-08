package net.ion.framework.template;

import net.ion.framework.template.tagext.PageInfo;

/**
 * template renderer의 결과
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class Page {
	private PageInfo pageInfo = null;
	private String pageText = null;

	/**
	 * @param pageInfo
	 *            PageInfo rendering된 page에 대한 정보
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
	 * @return PageInfo page rendering 정보
	 */
	public PageInfo getPageInfo() {
		return this.pageInfo;
	}

	/**
	 * @return String rendering 된 page 결과
	 */
	public String getPageText() {
		return this.pageText;
	}
}
