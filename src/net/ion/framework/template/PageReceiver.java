package net.ion.framework.template;

/**
 * TemplateRuntime 이 page를 generationg 한 후에 완성된 text를 넘겨줄 때 사용
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */
public interface PageReceiver {
	/**
	 * TemplateRuntime에 의해 불러진다.
	 * 
	 * @param page
	 *            Page 생성된 page
	 */
	void receivePage(Page page);
}
